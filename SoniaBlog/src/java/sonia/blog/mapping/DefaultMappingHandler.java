/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.Mapping;
import sonia.blog.api.mapping.MappingHandler;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author sdorra
 */
public class DefaultMappingHandler implements MappingHandler
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultMappingHandler.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DefaultMappingHandler()
  {
    mappingMap = new HashMap<String, Class<? extends Mapping>>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param regex
   * @param mapping
   */
  public void add(String regex, Class<? extends Mapping> mapping)
  {
    mappingMap.put(regex, mapping);
  }

  /**
   * Method description
   *
   *
   * @param regex
   *
   * @return
   */
  public boolean contains(String regex)
  {
    return mappingMap.containsKey(regex);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public boolean handleMapping(BlogRequest request, BlogResponse response)
          throws IOException, ServletException
  {
    boolean result = true;
    String uri = request.getRequestURI();

    uri = uri.substring(request.getContextPath().length());

    Class<? extends Mapping> mappingClass = null;
    List<String> params = null;

    for (String regex : mappingMap.keySet())
    {
      Pattern p = Pattern.compile(regex);
      Matcher m = p.matcher(uri);

      if (m.matches())
      {
        params = new ArrayList<String>();
        mappingClass = mappingMap.get(regex);

        int groupCount = m.groupCount();

        if (groupCount > 0)
        {
          params = new ArrayList<String>();

          for (int i = 0; i < groupCount; i++)
          {
            String param = m.group(i + 1);

            if (!Util.isBlank(param))
            {
              params.add(param);
            }
          }
        }

        break;
      }
    }

    if (mappingClass != null)
    {
      if (logger.isLoggable(Level.FINE))
      {
        logger.fine("handle mapping using " + mappingClass.getName());
      }

      Mapping mapping = null;

      try
      {
        mapping = mappingClass.newInstance();
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }

      request.setMapping(mapping);
      setCharacterEncoding(request);
      result = mapping.handleMapping(request, response,
                                     params.toArray(new String[0]));
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  public void load(InputStream in) throws IOException
  {
    try
    {
      Document doc = XmlUtil.buildDocument(in);
      NodeList mappingChildren = doc.getElementsByTagName("mapping");

      if (mappingChildren != null)
      {
        for (int i = 0; i < mappingChildren.getLength(); i++)
        {
          Node mappingChild = mappingChildren.item(i);

          addMapping(mappingChild);
        }
      }
    }
    catch (SAXException ex)
    {
      throw new IOException(ex);
    }
    catch (ParserConfigurationException ex)
    {
      throw new IOException(ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param regex
   */
  public void remove(String regex)
  {
    mappingMap.remove(regex);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param regex
   *
   * @return
   */
  public Class<? extends Mapping> get(String regex)
  {
    return mappingMap.get(regex);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Map<String, Class<? extends Mapping>> getAll()
  {
    return mappingMap;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param mappingChild
   */
  @SuppressWarnings("unchecked")
  private void addMapping(Node mappingChild)
  {
    if (mappingChild.getNodeName().equals("mapping"))
    {
      NodeList children = mappingChild.getChildNodes();

      if (children != null)
      {
        String regex = null;
        Class<? extends Mapping> clazz = null;

        for (int i = 0; i < children.getLength(); i++)
        {
          Node child = children.item(i);
          String name = child.getNodeName();
          String value = child.getTextContent();

          if (!Util.isBlank(value))
          {
            if (name.equals("regex"))
            {
              regex = value.trim();
            }
            else if (name.equals("class"))
            {
              try
              {
                clazz = (Class<? extends Mapping>) Class.forName(value);
              }
              catch (ClassNotFoundException ex)
              {
                logger.log(Level.SEVERE, null, ex);
              }
            }
          }
        }

        if (!Util.isBlank(regex) && (clazz != null))
        {
          if (logger.isLoggable(Level.FINER))
          {
            logger.finer("add mapping " + regex + " - " + clazz.getName());
          }

          mappingMap.put(regex, clazz);
        }
      }
    }
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   */
  private void setCharacterEncoding(BlogRequest request)
  {
    try
    {
      request.getRequest().setCharacterEncoding(request.getCharacterEncoding());
    }
    catch (UnsupportedEncodingException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<String, Class<? extends Mapping>> mappingMap;
}
