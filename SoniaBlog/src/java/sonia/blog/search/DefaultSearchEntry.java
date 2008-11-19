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
   */
  public DefaultSearchEntry(Document document, String searchResult)
  {
    this.document = document;
    this.searchResult = searchResult;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthor()
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
    return document.get("content");
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
  public Object getData()
  {
    Object result = null;
    EntityManager em = BlogContext.getInstance().getEntityManager();

    try
    {
      Class clazz = Class.forName(document.get("type"));
      Long id = Long.parseLong(document.get("id"));

      result = em.find(clazz, id);
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
  public String getSearchResult()
  {
    return searchResult;
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
  private String searchResult;
}
