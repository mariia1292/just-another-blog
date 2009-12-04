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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.entity.Blog;
import sonia.blog.entity.PermaObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 *
 * @param <T>
 */
public abstract class IndexHandler<T extends PermaObject>
{

  /** Field description */
  public static final String FIELD_AUTHOR = "author";

  /** Field description */
  public static final String FIELD_CATEGORY = "category";

  /** Field description */
  public static final String FIELD_CONTENT = "content";

  /** Field description */
  public static final String FIELD_CREATIONDATE = "creationDate";

  /** Field description */
  public static final String FIELD_ID = "id";

  /** Field description */
  public static final String FIELD_PARENTID = "pid";

  /** Field description */
  public static final String FIELD_TITLE = "title";

  /** Field description */
  public static final String FIELD_TYPE = "type";

  /** Field description */
  public static final String FIELD_TYPEID = "tid";

  /** Field description */
  private static Logger logger = Logger.getLogger(IndexHandler.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  protected abstract Document[] createDocuments(T item);

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  protected abstract Term[] createRemoveTerms(T item);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  protected abstract Blog getItemBlog(T item);

  /**
   * Method description
   *
   *
   * @param id
   *
   * @return
   */
  protected abstract PermaObject getObject(long id);

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param object
   */
  public void persits(PermaObject object)
  {
    T item = getItem(object);
    Blog blog = getItemBlog(item);
    File directory = getIndexDirectory(blog);

    persits(directory, blog, item);
  }

  /**
   * Method description
   *
   *
   *
   * @param object
   */
  public void remove(PermaObject object)
  {
    T item = getItem(object);
    Blog blog = getItemBlog(item);
    File directory = getIndexDirectory(blog);

    remove(directory, blog, item);
  }

  /**
   * Method description
   *
   *
   *
   * @param object
   */
  public void update(PermaObject object)
  {
    T item = getItem(object);
    Blog blog = getItemBlog(item);
    File directory = getIndexDirectory(blog);

    update(directory, blog, item);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public Blog getBlog(PermaObject object)
  {
    return getItemBlog(getItem(object));
  }

  /**
   * Method description
   *
   *
   * @param doc
   *
   * @return
   */
  public SearchEntry getSearchEntry(Document doc)
  {

    // TODO return SearchEntry
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @param doc
   *
   * @return
   */
  PermaObject getObject(Document doc)
  {
    PermaObject object = null;
    String id = doc.get(FIELD_ID);

    if (Util.hasContent(id))
    {
      try
      {
        object = getObject(Long.parseLong(id));
      }
      catch (NumberFormatException ex)
      {
        logger.log(Level.WARNING, null, ex);
      }
    }

    return object;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param directory
   * @param blog
   * @param docs
   *
   * @throws IOException
   */
  protected void addDocuments(File directory, Blog blog, Document... docs)
          throws IOException
  {
    if (!directory.exists() &&!directory.mkdirs())
    {
      throw new BlogException("could not create index directory");
    }

    Directory indexDir = null;
    IndexWriter writer = null;
    Lock lock = null;

    try
    {
      lock = IndexHandlerFactory.getInstance().getLock(directory);
      lock.lock();

      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("open index ").append(directory.getPath());
        logger.finest(msg.toString());
      }

      indexDir = FSDirectory.open(directory);
      writer = new IndexWriter(indexDir, getAnalyzer(),
                               IndexWriter.MaxFieldLength.UNLIMITED);

      for (Document doc : docs)
      {
        writer.addDocument(doc);
      }

      writer.commit();
    }
    finally
    {
      try
      {
        if (writer != null)
        {
          writer.close();
        }

        if (indexDir != null)
        {
          indexDir.close();
        }
      }
      finally
      {
        if (lock != null)
        {
          if (logger.isLoggable(Level.FINEST))
          {
            StringBuffer msg = new StringBuffer();

            msg.append("release lock for ").append(directory.getPath());
            logger.finest(msg.toString());
          }

          lock.unlock();
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
   * @param object
   *
   * @return
   */
  protected String buildTypeId(PermaObject object)
  {
    StringBuffer result = new StringBuffer();

    result.append(object.getClass().getName());
    result.append("-").append(object.getId());

    return result.toString();
  }

  /**
   * Method description
   *
   *
   * @param item
   * @param category
   *
   * @return
   */
  protected Document createBaseDocument(T item, String category)
  {
    Document doc = new Document();

    doc.add(new Field(FIELD_TYPEID, buildTypeId(item), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field(FIELD_TYPE, item.getClass().getName(), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field(FIELD_ID, item.getId().toString(), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field(FIELD_CATEGORY, category, Field.Store.YES,
                      Field.Index.NOT_ANALYZED));

    return doc;
  }

  /**
   * Method description
   *
   *
   *
   * @param directory
   * @param blog
   * @param item
   */
  protected void persits(File directory, Blog blog, T item)
  {
    Document[] docs = createDocuments(item);

    if (docs != null)
    {
      try
      {
        addDocuments(directory, blog, docs);

        if (logger.isLoggable(Level.FINEST))
        {
          StringBuffer msg = new StringBuffer();

          msg.append("added ").append(docs.length);
          msg.append(" document(s) to searchindex");
          logger.finest(msg.toString());
        }
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param directory
   * @param blog
   * @param item
   */
  protected void remove(File directory, Blog blog, T item)
  {
    Term[] terms = createRemoveTerms(item);

    try
    {
      int count = removeDocuments(directory, terms);

      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer log = new StringBuffer();

        log.append("delete ").append(count).append(" documents");
        logger.info(log.toString());
      }
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param directory
   * @param terms
   *
   * @return
   *
   * @throws IOException
   */
  protected int removeDocuments(File directory, Term... terms)
          throws IOException
  {
    int count = 0;
    Directory indexDir = null;
    IndexReader reader = null;
    Lock lock = null;

    try
    {
      lock = IndexHandlerFactory.getInstance().getLock(directory);
      lock.lock();

      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("open index ").append(directory.getPath());
        logger.finest(msg.toString());
      }

      indexDir = FSDirectory.open(directory);
      reader = IndexReader.open(indexDir, false);

      for (Term term : terms)
      {
        count += reader.deleteDocuments(term);
      }
    }
    finally
    {
      try
      {
        if (reader != null)
        {
          reader.close();
        }

        if (indexDir != null)
        {
          indexDir.close();
        }
      }
      finally
      {
        if (lock != null)
        {
          if (logger.isLoggable(Level.FINEST))
          {
            StringBuffer msg = new StringBuffer();

            msg.append("release lock for ").append(directory.getPath());
            logger.finest(msg.toString());
          }

          lock.unlock();
        }
      }
    }

    return count;
  }

  /**
   * Method description
   *
   *
   * @param directory
   * @param blog
   * @param item
   */
  protected void update(File directory, Blog blog, T item)
  {
    remove(directory, blog, item);
    persits(directory, blog, item);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @return
   */
  protected Analyzer getAnalyzer()
  {
    if (analyzer == null)
    {
      analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
    }

    return analyzer;
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  private File getIndexDirectory(Blog blog)
  {
    return BlogContext.getInstance().getResourceManager().getDirectory(
        Constants.RESOURCE_INDEX, blog);
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private T getItem(PermaObject object)
  {
    return (T) object;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Analyzer analyzer;
}
