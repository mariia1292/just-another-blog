/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
