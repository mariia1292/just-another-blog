/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.search;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import java.text.ParseException;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

/**
 *
 * @author sdorra
 */
public class DefaultSearchEntry implements SearchEntry
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultSearchEntry.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param document
   * @param searchResult
   * @param hit
   */
  public DefaultSearchEntry(Document document, String searchResult, long hit)
  {
    this.document = document;
    this.searchResult = searchResult;
    this.hit = hit;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param arg0
   *
   * @return
   */
  @Override
  public boolean equals(Object arg0)
  {
    return getData().equals(arg0);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public int hashCode()
  {
    return getData().hashCode();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean renderMacros()
  {
    return false;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorName()
  {
    return document.get("author");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getContent()
  {
    return searchResult;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getCreationDate()
  {
    Date date = null;
    String dateString = document.get("creationDate");

    if (dateString != null)
    {
      try
      {
        date = DateTools.stringToDate(dateString);
      }
      catch (ParseException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return date;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public ContentObject getData()
  {
    ContentObject result = null;
    EntityManager em = BlogContext.getInstance().getEntityManager();

    try
    {
      Class clazz = Class.forName(document.get("type"));
      Long id = Long.parseLong(document.get("id"));

      result = (ContentObject) em.find(clazz, id);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      em.close();
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Long getId()
  {
    return hit;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTeaser()
  {
    return getContent();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTitle()
  {
    return document.get("title");
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Document document;

  /** Field description */
  private Long hit;

  /** Field description */
  private String searchResult;
}
