/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.statistic;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import sonia.blog.webstart.JnlpContext;
import sonia.blog.webstart.repository.FileObject;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultStatisticManager implements StatisticManager
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultStatisticManager.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param statisticFile
   */
  public DefaultStatisticManager(File statisticFile)
  {
    this.statisticFile = statisticFile;
    pathMap = new HashMap<String, String>();
    this.statistic = new Properties();

    if ((statisticFile != null) && statisticFile.exists())
    {
      try
      {
        statistic.load(new FileInputStream(statisticFile));
      }
      catch (IOException ex)
      {
        throw new RuntimeException(ex);
      }
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param path
   */
  public void increase(String path)
  {
    synchronized (statistic)
    {
      String name = pathMap.get(path);

      if (Util.isEmpty(name))
      {
        name = getName(path);
        pathMap.put(path, name);
      }

      String value = statistic.getProperty(name);

      if (Util.isNotEmpty(value))
      {
        value = String.valueOf(Integer.parseInt(value) + 1);
      }
      else
      {
        value = "1";
      }

      statistic.setProperty(name, value);

      OutputStream out = null;

      try
      {
        out = new FileOutputStream(statisticFile);
        statistic.store(out, "Statistic");
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
      finally
      {
        try
        {
          out.close();
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Collection<Statistic> getStatistics()
  {
    List<Statistic> sl = new ArrayList<Statistic>();

    for (Map.Entry<Object, Object> entry : statistic.entrySet())
    {
      String key = entry.getKey().toString();
      String value = entry.getValue().toString();

      sl.add(new Statistic(key, Integer.valueOf(value)));
    }

    return sl;
  }

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   */
  private String getName(String path)
  {
    String result = "Unknown";
    FileObject fo =
      JnlpContext.getInstance().getRepositoryManager().getFileObject(path);

    if (fo != null)
    {
      try
      {
        Document doc = XmlUtil.buildDocument(fo.getInputStream());
        NodeList children = doc.getElementsByTagName("title");

        if (XmlUtil.hasContent(children))
        {
          result = children.item(0).getTextContent();
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private final Properties statistic;

  /** Field description */
  private Map<String, String> pathMap;

  /** Field description */
  private File statisticFile;
}
