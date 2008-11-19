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
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.TextFragment;
import org.apache.lucene.search.highlight.TokenSources;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import org.apache.lucene.queryParser.ParseException;
import sonia.blog.api.search.SearchException;

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
   * @param blog
   */
  public void reIndex(Blog blog)
  {
    if (!locked)
    {
      locked = true;

      Thread thread = new Thread(new ReIndexThread(blog));

      thread.start();
    }
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param search
   *
   * @return
   */
  public List<SearchEntry> search(Blog blog, String search) throws SearchException
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
          Hits hits = searcher.search(query);
          SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
          Highlighter highlighter = new Highlighter(htmlFormatter,
                                      new QueryScorer(query));

          for (int i = 0; i < hits.length(); i++)
          {
            Document doc = hits.doc(i);
            String content = doc.get("content");
            String searchResult = "...";

            if (content != null)
            {
              TokenStream tokenStream = TokenSources.getAnyTokenStream(reader,
                                          hits.id(i), "content", analyzer);
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

            entries.add(new DefaultSearchEntry(doc, searchResult));
          }
        }
        catch (ParseException ex)
        {
          logger.log(Level.FINEST, null, ex);
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
          throw new SearchException( ex.getLocalizedMessage() );
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
  public boolean isLocked()
  {
    return locked;
  }

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
    File resourceDir = BlogContext.getInstance().getResourceDirectory();
    File indexDir = new File(resourceDir, "index");

    return new File(indexDir, blog.getId().toString());
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 08/10/06
   * @author     Enter your name here...
   */
  public class ReIndexThread implements Runnable
  {

    /**
     * Constructs ...
     *
     *
     * @param blog
     */
    public ReIndexThread(Blog blog)
    {
      this.blog = blog;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     */
    @SuppressWarnings("unchecked")
    public void run()
    {
      IndexWriter writer = null;
      EntityManager em = null;

      try
      {
        File file = getDirectory(blog);

        if (!file.exists())
        {
          file.mkdirs();
        }

        writer = new IndexWriter(file, new StandardAnalyzer(), true);
        em = BlogContext.getInstance().getEntityManager();

        javax.persistence.Query q = em.createNamedQuery("Entry.overview");

        q.setParameter("blog", blog);

        List<Entry> entries = q.getResultList();

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
        locked = false;

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

        if (em != null)
        {
          em.close();
        }
      }
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private Blog blog;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean locked = false;
}
