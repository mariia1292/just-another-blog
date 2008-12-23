/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.search;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.listener.EntityListener;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;

/**
 *
 * @author sdorra
 */
public class IndexListener extends EntityListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(IndexListener.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public IndexListener()
  {
    resourceManager = BlogContext.getInstance().getResourceManager();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   */
  @Override
  public void postPersists(Object object)
  {
    Document doc = null;
    File blogDir = null;

    if (object instanceof Entry)
    {
      Entry entry = (Entry) object;

      if (entry.isPublished())
      {
        blogDir = getDirectory(entry);

        if (blogDir != null)
        {
          doc = SearchHelper.buildDocument(entry);
        }
      }
    }

    if (doc != null)
    {
      if (!blogDir.exists())
      {
        blogDir.mkdirs();
      }

      try
      {
        IndexWriter writer = new IndexWriter(blogDir, new StandardAnalyzer());

        writer.addDocument(doc);
        writer.optimize();
        writer.close();
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
   * @param object
   */
  @Override
  public void postRemove(Object object)
  {
    File blogDir = null;

    if (object instanceof Entry)
    {
      Entry entry = (Entry) object;

      if (entry.isPublished())
      {
        blogDir = getDirectory(entry);
      }
    }

    if (blogDir != null)
    {
      String tid = buildTypeId(object);

      if (tid != null)
      {
        try
        {
          IndexReader reader = IndexReader.open(blogDir);
          Term term = new Term("tid", tid);

          reader.deleteDocuments(term);
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
   * @param object
   */
  @Override
  public void postUpdate(Object object)
  {
    postRemove(object);
    postPersists(object);
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  private String buildTypeId(Object object)
  {
    String tid = null;

    if (object instanceof Entry)
    {
      Entry entry = (Entry) object;

      tid = Entry.class.getName() + "-" + entry.getId();
    }

    return tid;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  private File getDirectory(Entry entry)
  {
    File result = null;
    Category catgory = entry.getCategory();

    if (catgory != null)
    {
      Blog blog = catgory.getBlog();

      if (blog != null)
      {
        Long id = blog.getId();

        if (id != null)
        {
          result = resourceManager.getDirectory(Constants.RESOURCE_INDEX, blog);
        }
      }
    }

    return result;
  }

  private ResourceManager resourceManager;

}
