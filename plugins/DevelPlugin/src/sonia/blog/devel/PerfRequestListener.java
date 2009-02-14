/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.devel;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogRequestListener;
import sonia.blog.api.app.BlogRuntimeException;
import sonia.blog.api.app.ResourceManager;

import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author sdorra
 */
public class PerfRequestListener implements BlogRequestListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(PerfRequestListener.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public PerfRequestListener()
  {
    createNewDocument();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   */
  public void afterMapping(BlogRequest request)
  {
    synchronized (doc)
    {
      if (RequestInformation.isRequestComplete(request))
      {
        RequestInformation info = new RequestInformation(request);

        info.toXML(doc, rootEl);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   */
  public void beforeMapping(BlogRequest request)
  {
    RequestInformation.setStartTime(request);
  }

  /**
   * Constructs ...
   *
   */
  synchronized void createNewDocument()
  {
    try
    {
      doc = XmlUtil.createDocument();
      rootEl = doc.createElement("requests");
      doc.appendChild(rootEl);
    }
    catch (ParserConfigurationException ex)
    {
      throw new BlogRuntimeException(ex);
    }
  }

  /**
   * Method description
   *
   *
   * @throws IOException
   * @throws TransformerException
   */
  synchronized void store() throws IOException, TransformerException
  {
    ResourceManager resManager = BlogContext.getInstance().getResourceManager();
    File directory = resManager.getDirectory("plugin" + File.separator
                       + "devel");

    if (!directory.exists())
    {
      if (!directory.mkdirs())
      {
        throw new BlogRuntimeException("cannt create directory");
      }
    }

    File file = new File(directory, System.currentTimeMillis() + ".xml");

    if (file.exists())
    {
      throw new IllegalStateException("file allready exists");
    }

    XmlUtil.writeDocument(doc, file);
    createNewDocument();

    try
    {
      RequestStatistic stat = new RequestStatistic(file);

      System.out.println(stat);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Document doc;

  /** Field description */
  private Element rootEl;
}
