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



package sonia.blog.devel;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogRequestListener;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.exception.BlogException;

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
 * @author Sebastian Sdorra
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
      throw new BlogException(ex);
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
        throw new BlogException("cannt create directory");
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
