/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
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
 * @author Sebastian Sdorra
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