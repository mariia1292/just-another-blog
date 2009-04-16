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
import sonia.blog.api.link.LinkBuilder;
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
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    List<Category> categories = categoryDAO.getAll(blog);

    for (Category category : categories)
    {

      // TODO: replace with real links
      Map<String, String> properties = new HashMap<String, String>();
      String link = linkBuilder.buildLink(blog,
                      "/category/" + category.getId() + "/index.jab");
      String rssLink = linkBuilder.buildLink(blog,
                         "/feed/category/" + category.getId() + ".rss2");

      properties.put("htmlUrl", link);
      properties.put("rssUrl", rssLink);
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
    Entry entry = entryDAO.get(convertId(postId));
    Map<String, Object> result = null;

    if (entry != null)
    {
      result = createPost(entry.getBlog(), entry);
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

    Vector<Map<String, Object>> result = new Vector<Map<String, Object>>();
    LoginContext ctx = login(username, password);
    Blog blog = findBlog(blogId);
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    List<Entry> entries = entryDAO.findAllActivesByBlog(blog, 0, max);

    for (Entry entry : entries)
    {
      result.add(createPost(blog, entry));
    }

    logout(ctx);

    return result;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param entry
   *
   * @return
   */
  protected Map<String, Object> createPost(Blog blog, Entry entry)
  {
    Map<String, Object> result = new HashMap<String, Object>();
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String link = linkBuilder.buildLink(blog,
                    "/list/" + entry.getId() + ".jab");

    // TODO replace with real Link
    result.put("postid", entry.getId().toString());
    result.put("title", entry.getTitle());
    result.put("link", link);
    result.put("description", entry.getContent());
    result.put("userid", entry.getAuthorName());
    result.put("author", entry.getAuthor().getEmail());

    List<Category> categoryList = entry.getCategories();

    if (categoryList != null)
    {
      Vector<String> categories = new Vector<String>();

      for (Category cat : categoryList)
      {
        categories.add(cat.getName());
      }

      result.put("categories", categories);
    }

    return result;
  }
}
