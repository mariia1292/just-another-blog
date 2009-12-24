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



package sonia.blog.search;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

//~--- JDK imports ------------------------------------------------------------

import java.text.ParseException;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
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
  public Blog getBlog()
  {
    Blog blog = null;
    ContentObject object = getData();

    if (object != null)
    {
      blog = object.getBlog();
    }

    return blog;
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
  public ContentObject getData()
  {
    if (data == null)
    {
      try
      {
        Class clazz = Class.forName(document.get("type"));
        Long id = Long.parseLong(document.get("id"));

        if (clazz.equals(Entry.class))
        {
          EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();

          data = entryDAO.get(id);
        }
        else if (clazz.equals(Page.class))
        {
          PageDAO pageDAO = BlogContext.getDAOFactory().getPageDAO();

          data = pageDAO.get(id);
        }
        else if (clazz.equals(Comment.class))
        {
          CommentDAO commentDAO = BlogContext.getDAOFactory().getCommentDAO();
          Comment comment = commentDAO.get(id);

          if (comment != null)
          {
            data = comment.getEntry();
          }
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return data;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayContent()
  {
    return getContent();
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

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isPublished()
  {
    boolean published = false;
    ContentObject object = getData();

    if (object != null)
    {
      published = object.isPublished();
    }

    return published;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param displayContent
   */
  public void setDisplayContent(String displayContent)
  {

    // do nothing
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ContentObject data;

  /** Field description */
  private Document document;

  /** Field description */
  private Long hit;

  /** Field description */
  private String searchResult;
}
