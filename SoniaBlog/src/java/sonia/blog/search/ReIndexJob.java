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
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogJob;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;

import sonia.jobqueue.JobException;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
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

      if (!file.exists() &&!file.mkdirs())
      {
        throw new JobException("could not create index directory");
      }

      Directory directory = FSDirectory.open(file);

      writer = new IndexWriter(directory, SearchHelper.getAnalyzer(blog), true,
                               IndexWriter.MaxFieldLength.UNLIMITED);

      EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
      List<Entry> entries = entryDAO.findAllActivesByBlog(blog);

      addObjects(entries, writer);

      PageDAO pageDAO = BlogContext.getDAOFactory().getPageDAO();
      List<Page> pages = pageDAO.getAllByBlog(blog, true);

      addObjects(pages, writer);
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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param objects
   * @param writer
   */
  private void addObjects(List<? extends ContentObject> objects,
                          IndexWriter writer)
  {
    if (Util.hasContent(objects))
    {
      for (ContentObject co : objects)
      {
        try
        {
          Document doc = SearchHelper.buildDocument(co);

          if (doc != null)
          {
            writer.addDocument(doc);
          }
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;
}
