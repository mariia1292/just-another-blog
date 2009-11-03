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



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sonia.blog.api.exception.BlogException;

import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class PDFImageGallery
{

  /**
   * Constructs ...
   *
   *
   * @param file
   */
  public PDFImageGallery(File file)
  {
    FileInputStream fis = null;

    try
    {
      fis = new FileInputStream(file);

      Document doc = XmlUtil.buildDocument(fis);

      if (doc != null)
      {
        readDocument(doc);
      }
    }
    catch (Exception ex)
    {
      throw new BlogException(ex);
    }
    finally
    {
      if (fis != null)
      {
        try
        {
          fis.close();
        }
        catch (IOException ex)
        {
          throw new BlogException(ex);
        }
      }
    }
  }

  /**
   * Constructs ...
   *
   *
   * @param title
   * @param images
   */
  public PDFImageGallery(String title, List<String> images)
  {
    this.title = title;
    this.images = images;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param file
   */
  public void toXML(File file)
  {
    try
    {
      Document document = XmlUtil.createDocument();
      Element root = document.createElement("pdf-gallery");
      Element titleEl = document.createElement("title");

      titleEl.setTextContent(title);
      root.appendChild(titleEl);

      Element pagesEl = document.createElement("pages");

      for (String image : images)
      {
        Element pageEl = document.createElement("page");

        pageEl.setTextContent(image);
        pagesEl.appendChild(pageEl);
      }

      root.appendChild(pagesEl);
      document.appendChild(root);
      XmlUtil.writeDocument(document, file);
    }
    catch (Exception ex)
    {
      throw new BlogException(ex);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<String> getImages()
  {
    return images;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTitle()
  {
    return title;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param doc
   */
  private void readDocument(Document doc)
  {
    NodeList nodeList = doc.getDocumentElement().getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node node = nodeList.item(i);

      if ("title".equals(node.getNodeName()))
      {
        title = node.getTextContent();
      }
      else if ("pages".equals(node.getNodeName()))
      {
        readPages(node);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param node
   */
  private void readPages(Node node)
  {
    images = new ArrayList<String>();

    NodeList pageNodeList = node.getChildNodes();

    for (int i = 0; i < pageNodeList.getLength(); i++)
    {
      Node pageNode = pageNodeList.item(i);

      if ("page".equals(pageNode.getNodeName()))
      {
        images.add(pageNode.getTextContent());
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<String> images;

  /** Field description */
  private String title;
}
