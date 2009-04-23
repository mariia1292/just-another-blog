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
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.dao.DAOListener;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class IndexListener implements DAOListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(IndexListener.class.getName());

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
    if (item instanceof Entry)
    {
      Entry entry = (Entry) item;

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
   * @param entry
   */
  public void postPersists(Entry entry)
  {
    Document doc = null;
    File blogDir = null;

    if (entry.isPublished())
    {
      blogDir = getDirectory(entry);

      if (blogDir != null)
      {
        doc = SearchHelper.buildDocument(entry);
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
        IndexWriter writer = new IndexWriter(blogDir, new StandardAnalyzer(),
                               IndexWriter.MaxFieldLength.UNLIMITED);

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
   *
   * @param entry
   */
  public void postRemove(Entry entry)
  {
    File blogDir = null;

    if (entry.isPublished())
    {
      blogDir = getDirectory(entry);
    }

    if (blogDir != null)
    {
      String tid = buildTypeId(entry);

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
   *
   * @param entry
   */
  public void postUpdate(Entry entry)
  {
    postRemove(entry);
    postPersists(entry);
  }

  /**
   * Method description
   *
   *
   *
   * @param entry
   *
   * @return
   */
  private String buildTypeId(Entry entry)
  {
    return Entry.class.getName() + "-" + entry.getId();
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
    Blog blog = entry.getBlog();

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

  private File getBlogDirectory( Blog blog )
  {
    if ( resourceManager == null )
    {
      resourceManager = BlogContext.getInstance().getResourceManager();
    }
return resourceManager.getDirectory(Constants.RESOURCE_INDEX, blog);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ResourceManager resourceManager;
}
