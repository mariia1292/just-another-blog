/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.search;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.exception.BlogSecurityException;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.search.SearchException;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class DefaultSearchContext implements SearchContext
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultSearchContext.class.getName());

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
   * @param search
   *
   * @return
   *
   * @throws SearchException
   */
  public List<SearchEntry> search(Blog blog, String search)
          throws SearchException
  {
    List<SearchEntry> entries = new ArrayList<SearchEntry>();

    if ((blog != null) && (search != null))
    {
      File directory = getDirectory(blog);

      if (directory != null)
      {
        IndexReader reader = null;
        IndexSearcher searcher = null;

        try
        {
          reader = IndexReader.open(directory);
          searcher = new IndexSearcher(reader);

          Analyzer analyzer = new StandardAnalyzer();
          QueryParser parser = new MultiFieldQueryParser(new String[] {
                                 "content",
                                 "title" }, analyzer);
          Query query = parser.parse(search);
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
            String searchResult = "...";

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
                  searchResult += frag[j].toString() + "...";
                }
              }
            }

            entries.add(new DefaultSearchEntry(doc, searchResult, i));
          }
        }
        catch (ParseException ex)
        {
          logger.log(Level.FINEST, null, ex);
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
            if (searcher != null)
            {
              searcher.close();
            }

            if (reader != null)
            {
              reader.close();
            }
          }
          catch (IOException ex)
          {
            logger.log(Level.WARNING, null, ex);
          }
        }
      }
    }

    return entries;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isReIndexable()
  {
    return true;
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
}
