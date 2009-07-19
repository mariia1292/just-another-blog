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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sonia.blog.api.app.BlogRequest;

/**
 *
 * @author Sebastian Sdorra
 */
public class RequestInformation
{

  /** Field description */
  public static final String STARTTIME_VAR = "sonia.devel.start-time";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param request
   */
  public RequestInformation(BlogRequest request)
  {
    this.endTime = System.nanoTime();
    this.requestUri = request.getRequestURI();
    this.startTime = (Long) request.getAttribute(STARTTIME_VAR);
  }

  /**
   * Constructs ...
   *
   *
   * @param requestNode
   */
  public RequestInformation(Node requestNode)
  {
    NodeList children = requestNode.getChildNodes();

    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String name = child.getNodeName();
      String value = child.getTextContent();

      if (name.equals("uri"))
      {
        requestUri = value;
      }
      else if (name.equals("start-time"))
      {
        startTime = Long.parseLong(value);
      }
      else if (name.equals("end-time"))
      {
        endTime = Long.parseLong(value);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  public static boolean isRequestComplete(BlogRequest request)
  {
    return request.getAttribute(STARTTIME_VAR) != null;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   */
  public static void setStartTime(BlogRequest request)
  {
    request.setAttribute(STARTTIME_VAR, System.nanoTime());
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param doc
   * @param rootEl
   */
  public void toXML(Document doc, Element rootEl)
  {
    Element requestEl = doc.createElement("request");
    Element uriEl = doc.createElement("uri");

    uriEl.setTextContent(requestUri);
    requestEl.appendChild(uriEl);

    Element startTimeEl = doc.createElement("start-time");

    startTimeEl.setTextContent(Long.toString(startTime));
    requestEl.appendChild(startTimeEl);

    Element endTimeEl = doc.createElement("end-time");

    endTimeEl.setTextContent(Long.toString(endTime));
    requestEl.appendChild(endTimeEl);
    rootEl.appendChild(requestEl);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public long getEndTime()
  {
    return endTime;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getRequestTime()
  {
    System.out.println(endTime + " - " + startTime + " = "
                       + (endTime - startTime));

    return endTime - startTime;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRequestUri()
  {
    return requestUri;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getStartTime()
  {
    return startTime;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private long endTime;

  /** Field description */
  private String requestUri;

  /** Field description */
  private long startTime;
}
