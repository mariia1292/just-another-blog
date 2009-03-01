/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author sdorra
 */
public class PluginReader
{

  /** Field description */
  private static final String PLUGIN_SCHEMA = "/sonia/plugin/plugin-schema.xsd";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param context
   */
  public PluginReader(PluginContext context)
  {
    this.context = context;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param classpath
   *
   * @throws IOException
   */
  public void readClasspath(String classpath) throws IOException
  {
    StringTokenizer tokenizer = new StringTokenizer(classpath, ":");

    while (tokenizer.hasMoreElements())
    {
      String part = tokenizer.nextToken();
      InputStream in = null;

      if (part.endsWith(".jar") || part.endsWith(".zip"))
      {
        File f = new File(part);

        if (f.exists())
        {
          ZipFile file = new ZipFile(part);
          ZipEntry entry = file.getEntry("META-INF/plugin.xml");

          if (entry != null)
          {
            in = file.getInputStream(entry);
          }
        }
      }
      else
      {
        String path = part;

        if (!path.endsWith("/"))
        {
          path += "/";
        }

        path += "META-INF/plugin.xml";

        File file = new File(path);

        if (file.exists())
        {
          in = new FileInputStream(file);
        }
      }

      if (in != null)
      {
        try
        {
          Plugin plugin = readPlugin(in);

          if (plugin != null)
          {
            plugin.setPath(part);
            context.register(plugin);
          }
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   *
   * @return
   *
   * @throws IOException
   */
  public Plugin readPlugin(InputStream in) throws IOException
  {
    Plugin plugin = null;

    try
    {
      Document doc = XmlUtil.buildDocument(in,
                       Util.findResource(PLUGIN_SCHEMA));
      Node root = doc.getDocumentElement();

      plugin = new Plugin();

      NodeList children = root.getChildNodes();

      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        String name = child.getNodeName();
        String value = child.getTextContent();

        if (name.equals("name"))
        {
          plugin.setName(value);
        }
        else if (name.equals("display-name"))
        {
          plugin.setDisplayName(value);
        }
        else if (name.equals("description"))
        {
          plugin.setDescription(value);
        }
        else if (name.equals("version"))
        {
          plugin.setVersion(value);
        }
        else if (name.equals("vendor"))
        {
          plugin.setVendor(value);
        }
        else if (name.equals("autostart"))
        {
          if ("false".equals(value))
          {
            plugin.setAutostart(false);
          }
        }
        else if (name.equals("activator"))
        {
          try
          {
            Activator activator =
              (Activator) Class.forName(value).newInstance();

            if (context.getInjectionProvider() != null)
            {
              context.getInjectionProvider().inject(activator);
            }

            plugin.setActivator(activator);
          }
          catch (Exception ex)
          {
            ex.printStackTrace();

            throw new IOException(ex.getMessage());
          }
        }
      }

      if ((plugin.getName() == null) || (plugin.getVendor() == null)
          || (plugin.getVersion() == null))
      {
        plugin = null;
      }
    }
    catch (SAXException ex)
    {
      throw new IOException(ex.getMessage());
    }
    catch (ParserConfigurationException ex)
    {
      throw new IOException(ex.getMessage());
    }

    return plugin;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private PluginContext context;
}
