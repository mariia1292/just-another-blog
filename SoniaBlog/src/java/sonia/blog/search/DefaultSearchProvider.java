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
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.exception.BlogSecurityException;
import sonia.blog.api.search.SearchCategory;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.api.search.SearchProvider;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultSearchProvider implements SearchProvider
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultSearchProvider.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param blog
   */
  public void reIndex(BlogSession session, Blog blog)
  {
    if (!session.hasRole(blog, Role.ADMIN))
    {
      throw new BlogSecurityException("AdminSession is required");
    }

    BlogContext.getInstance().getJobQueue().add(new ReIndexJob(blog));
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param locale
   * @param queryString
   *
   * @return
   */
  public Collection<SearchCategory> search(Blog blog, Locale locale,
          String queryString)
  {
    Map<String, SearchCategory> categories = new HashMap<String,
                                               SearchCategory>();

    if ((blog != null) && (queryString != null))
    {
      File blogDirectory = getDirectory(blog);

      if ((blogDirectory != null) && blogDirectory.exists())
      {
        IndexReader reader = null;
        IndexSearcher searcher = null;

        try
        {
          Directory directory = FSDirectory.open(blogDirectory);

          reader = IndexReader.open(directory, true);
          searcher = new IndexSearcher(reader);

          Analyzer analyzer = SearchHelper.getAnalyzer(locale);
          QueryParser parser =
            new MultiFieldQueryParser(Version.LUCENE_CURRENT,
                                      new String[] { "content",
                  "title" }, analyzer);
          Query query = parser.parse(queryString);

          if (logger.isLoggable(Level.FINER))
          {
            StringBuffer msg = new StringBuffer();

            msg.append("start search witch query \"").append(query.toString());
            msg.append("\" and analyzer ");
            msg.append(analyzer.getClass().getName());
            logger.finer(msg.toString());
          }

          TopDocs topDocs = searcher.search(query, blog.getEntriesPerPage());
          SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
          Highlighter highlighter = new Highlighter(htmlFormatter,
                                      new QueryScorer(query));
          ScoreDoc[] docs = topDocs.scoreDocs;

          for (int i = 0; i < docs.length; i++)
          {
            int id = docs[i].doc;
            Document doc = reader.document(id);
            String content = doc.get("content");
            StringBuffer resultBuffer = new StringBuffer("...");

            if (content != null)
            {
              TokenStream tokenStream = TokenSources.getAnyTokenStream(reader,
                                          id, "content", analyzer);
              TextFragment[] frag =
                highlighter.getBestTextFragments(tokenStream, content, false,
                                                 10);

              for (int j = 0; j < frag.length; j++)
              {
                if ((frag[j] != null) && (frag[j].getScore() > 0))
                {
                  resultBuffer.append(frag[j].toString()).append("...");
                }
              }
            }

            String categoryName = doc.get("category");

            if (Util.hasContent(categoryName))
            {
              SearchCategory category = categories.get(categoryName);

              if (category == null)
              {
                category = new SearchCategory(categoryName,
                                              getLabel(locale, categoryName));
                categories.put(categoryName, category);
              }

              SearchEntry entry = new DefaultSearchEntry(doc,
                                    resultBuffer.toString(),
                                    category.getEntries().size());

              category.getEntries().add(entry);
            }
          }
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);

          throw new SearchException(ex.getLocalizedMessage());
        }
        finally
        {
          try
          {
            if (reader != null)
            {
              reader.close();
            }

            if (searcher != null)
            {
              searcher.close();
            }
          }
          catch (IOException ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }
      }
    }

    return categories.values();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param session
   * @param blog
   *
   * @return
   */
  public boolean isReindexable(BlogSession session, Blog blog)
  {
    return session.hasRole(blog, Role.ADMIN);
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  private File getDirectory(Blog blog)
  {
    return BlogContext.getInstance().getResourceManager().getDirectory(
        Constants.RESOURCE_INDEX, blog);
  }

  /**
   * Method description
   *
   *
   * @param locale
   * @param name
   *
   * @return
   */
  private String getLabel(Locale locale, String name)
  {
    String label = name;

    try
    {
      label = ResourceBundle.getBundle("sonia.blog.resources.label",
                                       locale).getString(name);
    }
    catch (MissingResourceException ex)
    {
      if (logger.isLoggable(Level.WARNING))
      {
        StringBuffer log = new StringBuffer();

        log.append("missing resource key ").append(name);
        logger.warning(log.toString());
      }
    }

    return label;
  }
}
