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

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.dao.DAOListener;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;
import sonia.blog.api.exception.BlogException;

/**
 *
 * @author Sebastian Sdorra
 */
public class ContentObjectIndexListener implements DAOListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ContentObjectIndexListener.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param action
   * @param item
   */
  public void handleEvent(Action action, Object item)
  {
    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("handle ").append(action).append(" at ").append(item);
      logger.finest(msg.toString());
    }

    if (item instanceof ContentObject)
    {
      ContentObject entry = (ContentObject) item;

      switch (action)
      {
        case POSTADD :
          postPersists(entry);

          break;

        case POSTUPDATE :
          postUpdate(entry);

          break;

        case POSTREMOVE :
          postRemove(entry);

          break;

        default :

        // do nothing
      }
    }
  }

  /**
   * Method description
   *
   *
   *
   *
   * @param co
   */
  public void postPersists(ContentObject co)
  {
    Document doc = null;
    File blogDir = null;

    if (co.isPublished())
    {
      blogDir = getDirectory(co);

      if (blogDir != null)
      {
        doc = SearchHelper.buildDocument(co);
      }
    }

    if (doc != null)
    {
      if (!blogDir.exists() && ! blogDir.mkdirs())
      {
        throw new BlogException( "could not create index directory" );
      }

      try
      {
        Directory directory = FSDirectory.open(blogDir);
        IndexWriter writer = new IndexWriter(directory,
                               SearchHelper.getAnalyzer(co.getBlog()),
                               IndexWriter.MaxFieldLength.UNLIMITED);

        writer.addDocument(doc);
        writer.optimize();
        writer.close();

        if (logger.isLoggable(Level.FINEST))
        {
          logger.finest("added 1 document to searchindex");
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Method description
   *
   *
   *
   *
   * @param co
   */
  public void postRemove(ContentObject co)
  {
    File blogDir = null;

    if (co.isPublished())
    {
      blogDir = getDirectory(co);
    }

    if (blogDir != null)
    {
      String tid = SearchHelper.buildTypeId(co);

      if (tid != null)
      {
        try
        {
          Directory directory = FSDirectory.open(blogDir);
          IndexReader reader = IndexReader.open(directory, false);
          Term term = new Term("tid", tid);
          int delCount = reader.deleteDocuments(term);

          if (logger.isLoggable(Level.INFO))
          {
            StringBuffer log = new StringBuffer();

            log.append("delete ").append(delCount).append(" documents");
            logger.info(log.toString());
          }

          reader.close();
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   *
   *
   * @param co
   */
  public void postUpdate(ContentObject co)
  {
    postRemove(co);
    postPersists(co);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  private File getBlogDirectory(Blog blog)
  {
    if (resourceManager == null)
    {
      resourceManager = BlogContext.getInstance().getResourceManager();
    }

    return resourceManager.getDirectory(Constants.RESOURCE_INDEX, blog);
  }

  /**
   * Method description
   *
   *
   *
   * @param co
   *
   * @return
   */
  private File getDirectory(ContentObject co)
  {
    File result = null;
    Blog blog = co.getBlog();

    if (blog != null)
    {
      Long id = blog.getId();

      if (id != null)
      {
        result = getBlogDirectory(blog);
      }
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ResourceManager resourceManager;
}
