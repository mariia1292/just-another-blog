/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.search;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogJob;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;

import sonia.jobqueue.JobException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class ReIndexJob implements BlogJob
{

  /** Field description */
  private static final long serialVersionUID = 1912641125910359083L;

  /** Field description */
  private static Logger logger = Logger.getLogger(ReIndexJob.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param blog
   */
  public ReIndexJob(Blog blog)
  {
    this.blog = blog;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws JobException
   */
  public void excecute() throws JobException
  {
    IndexWriter writer = null;

    try
    {
      File file = BlogContext.getInstance().getResourceManager().getDirectory(
                      Constants.RESOURCE_INDEX, blog);

      if (!file.exists())
      {
        file.mkdirs();
      }

      writer = new IndexWriter(file, new StandardAnalyzer(), true,
                               IndexWriter.MaxFieldLength.UNLIMITED);

      EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
      List<Entry> entries = entryDAO.findAllActivesByBlog(blog);

      if (entries != null)
      {
        for (Entry e : entries)
        {
          try
          {
            Document doc = SearchHelper.buildDocument(e);

            if (doc != null)
            {
              writer.addDocument(doc);
            }
          }
          catch (Exception ex)
          {
            logger.log(Level.SEVERE, "error during indexing " + e, ex);
          }
        }
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (writer != null)
      {
        try
        {
          writer.optimize();
          writer.close();
        }
        catch (Exception ex)
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
  public Blog getBlog()
  {
    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    StringBuffer log = new StringBuffer();

    log.append("rebuild search context for ").append(blog.getIdentifier());

    return log.toString();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return "reindex";
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;
}
