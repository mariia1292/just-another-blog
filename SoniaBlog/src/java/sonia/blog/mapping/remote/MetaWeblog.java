/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping.remote;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.xmlrpc.XmlRpcException;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.security.auth.login.LoginContext;

/**
 *
 * @author sdorra
 */
public class MetaWeblog extends Blogger
{

  /** Field description */
  public static final String METAWEBLOG_KEY = "metaWeblog";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public MetaWeblog()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blogId
   * @param username
   * @param password
   * @param struct
   *
   * @return
   *
   * @throws Exception
   */
  public Map newMediaObject(String blogId, String username, String password,
                            Map struct)
          throws Exception
  {
    logger.info("metaWeblog.newMediaObject");

    return null;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blogId
   * @param username
   * @param password
   *
   * @return
   *
   * @throws Exception
   */
  public Object getCategories(String blogId, String username, String password)
          throws Exception
  {
    logger.info("metaWeblog.getCategories");

    LoginContext ctx = login(username, password);
    Map<String, Map<String, String>> result = new HashMap<String,
                                                Map<String, String>>();
    Blog blog = findBlog(blogId);
    CategoryDAO categoryDAO = BlogContext.getDAOFactory().getCategoryDAO();
    List<Category> categories = categoryDAO.findAllByBlog(blog);

    for (Category category : categories)
    {

      // TODO: replace with real links
      Map<String, String> properties = new HashMap<String, String>();

      properties.put("htmlUrl",
                     "http://localhost:8080/jab/categories/"
                     + category.getId());
      properties.put("rssUrl",
                     "http://localhost:8080/jab/categories/"
                     + category.getId());
      result.put(category.getName(), properties);
    }

    logout(ctx);

    return result;
  }

  /**
   * Method description
   *
   *
   * @param postId
   * @param username
   * @param password
   *
   * @return
   *
   * @throws Exception
   */
  public Map getPost(String postId, String username, String password)
          throws Exception
  {
    logger.info("metaWeblog.getPost");

    LoginContext ctx = login(username, password);
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    Entry entry = entryDAO.find(convertId(postId));
    Map<String, String> result = null;

    if (entry != null)
    {
      result = createPost(entry);
    }
    else
    {
      throw new XmlRpcException("post not found");
    }

    if (ctx != null)
    {
      logout(ctx);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param blogId
   * @param username
   * @param password
   * @param max
   *
   * @return
   *
   * @throws Exception
   */
  public Object getRecentPosts(String blogId, String username, String password,
                               Integer max)
          throws Exception
  {
    logger.info("metaWeblog.getRecentPosts");

    Vector<Map<String, String>> result = new Vector<Map<String, String>>();
    LoginContext ctx = login(username, password);
    Blog blog = findBlog(blogId);
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    
    List<Entry> entries = entryDAO.findAllActivesByBlog(blog, 0, max);

    for (Entry entry : entries)
    {
      result.add(createPost(entry));
    }

    logout(ctx);

    return result;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  protected Map<String, String> createPost(Entry entry)
  {
    Map<String, String> result = new HashMap<String, String>();

    // TODO replace with real Link
    result.put("postid", entry.getId().toString());
    result.put("title", entry.getTitle());
    result.put("link",
               "http://localhost:8080/jab/list/" + entry.getId() + ".jab");
    result.put("description", entry.getContent());
    result.put("userid", entry.getAuthorName());
    result.put("author", entry.getAuthor().getEmail());

    return result;
  }
}
