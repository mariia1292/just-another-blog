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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author Sebastian Sdorra
 */
public class RequestStatistic
{

  /**
   * Constructs ...
   *
   *
   * @param file
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public RequestStatistic(File file)
          throws IOException, SAXException, ParserConfigurationException
  {
    Document doc = XmlUtil.buildDocument(new FileInputStream(file));

    buildInformationList(doc);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String toString()
  {
    StringBuffer result = new StringBuffer();
    List<RequestStatisticInformation> valueList =
      new ArrayList<RequestStatisticInformation>(statisticMap.values());

    Collections.sort(valueList);

    for (RequestStatisticInformation info : valueList)
    {
      NumberFormat nf = NumberFormat.getInstance();

      result.append(info.getRequestUri() + " : "
                    + nf.format(info.getAverageLoadTime()) + "\n");
    }

    return result.toString();
  }

  /**
   * Method description
   *
   *
   * @param info
   */
  private void addToMap(RequestInformation info)
  {
    String requestUri = info.getRequestUri();
    RequestStatisticInformation statisticInfo = statisticMap.get(requestUri);

    if (statisticInfo == null)
    {
      statisticInfo = new RequestStatisticInformation(requestUri);
      statisticMap.put(requestUri, statisticInfo);
    }

    statisticInfo.add(info.getRequestTime());
  }

  /**
   * Method description
   *
   *
   * @param doc
   */
  private void buildInformationList(Document doc)
  {
    statisticMap = new HashMap<String, RequestStatisticInformation>();

    NodeList children = doc.getElementsByTagName("request");

    if (children != null)
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);

        if (child.getNodeName().equals("request"))
        {
          RequestInformation info = new RequestInformation(child);

          addToMap(info);
        }
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<String, RequestStatisticInformation> statisticMap;
}
