/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.script;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.exception.BlogException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Sebastian Sdorra
 */
public class ScriptManager
{

  /** Field description */
  private static ScriptManager instance;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ScriptManager.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  private ScriptManager() {}

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static ScriptManager getInstance()
  {
    if (instance == null)
    {
      instance = new ScriptManager();
    }

    return instance;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public Script getScript(String name)
  {
    if (scripts == null)
    {
      loadScripts();
    }

    Script script = null;

    for (Script s : scripts)
    {
      if (s.getName().equals(name))
      {
        script = s;

        break;
      }
    }

    return script;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Script> getScripts()
  {
    if (scripts == null)
    {
      loadScripts();
    }

    return scripts;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  File createScriptFile()
  {
    File dir = BlogContext.getInstance().getResourceManager().getDirectory(
                   ScriptConstants.DIRECTORY);

    return new File(dir, System.nanoTime() + ".xml");
  }

  /**
   * Method description
   *
   *
   * @param document
   * @param file
   *
   * @throws TransformerConfigurationException
   * @throws TransformerException
   */
  void store(Document document, File file)
          throws TransformerConfigurationException, TransformerException
  {
    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory.newTransformer();

    transformer.transform(new DOMSource(document), new StreamResult(file));
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  Document getDocument()
  {
    if (builder == null)
    {
      initDocumentBuilder();
    }

    return builder.newDocument();
  }

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   *
   * @throws IOException
   * @throws SAXException
   */
  Document getDocument(File file) throws IOException, SAXException
  {
    if (builder == null)
    {
      initDocumentBuilder();
    }

    return builder.parse(file);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void initDocumentBuilder()
  {
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

      builder = factory.newDocumentBuilder();
    }
    catch (ParserConfigurationException ex)
    {
      throw new BlogException(ex);
    }
  }

  /**
   * Method description
   *
   */
  private synchronized void loadScripts()
  {
    List<Script> temp = new ArrayList<Script>();
    File main = BlogContext.getInstance().getResourceManager().getDirectory(
                    ScriptConstants.DIRECTORY);

    if (main.exists())
    {
      File[] files = main.listFiles(new FilenameFilter()
      {
        public boolean accept(File file, String name)
        {
          return name.endsWith(".xml");
        }
      });

      if (files != null)
      {
        for (File file : files)
        {
          try
          {
            Script script = new Script(file);

            temp.add(script);
          }
          catch (Exception ex)
          {
            logger.log(Level.WARNING, null, ex);
          }
        }
      }
    }

    scripts = temp;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DocumentBuilder builder;

  /** Field description */
  private List<Script> scripts;
}
