/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping.remote;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.common.XmlRpcNotAuthorizedException;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author sdorra
 */
public class Blogger
{

  /** Field description */
  public static final String BLOGGER_KEY = "blogger";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public Blogger()
  {
    logger = Logger.getLogger(getClass().getName());
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param appKey
   * @param postId
   * @param username
   * @param password
   * @param publish
   *
   * @return
   *
   * @throws IOException
   */
  public Boolean deletePost(String appKey, String postId, String username,
                            String password, Boolean publish)
          throws IOException
  {
    return false;
  }

  /**
   * Method description
   *
   *
   * @param blogId
   * @param username
   * @param password
   * @param struct
   * @param publish
   *
   * @return
   *
   * @throws Exception
   */
  public Boolean editPost(String blogId, String username, String password,
                          Map struct, Boolean publish)
          throws Exception
  {
    Boolean result = Boolean.TRUE;
    LoginContext ctx = login(username, password);

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
   * @param struct
   * @param publish
   *
   * @return
   *
   * @throws Exception
   */
  public String newPost(String blogId, String username, String password,
                        Map struct, Boolean publish)
          throws Exception
  {
    String result = null;
    LoginContext ctx = login(username, password);
    Blog blog = findBlog(blogId);
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();

    if (ctx != null)
    {
      logout(ctx);
    }

    return result;    // database id
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param appKey
   * @param blogId
   * @param username
   * @param password
   * @param templateType
   *
   * @return
   *
   * @throws Exception
   */
  public String getTemplate(String appKey, String blogId, String username,
                            String password, String templateType)
          throws Exception
  {
    String result = null;

    return result;
  }

  /**
   * Method description
   *
   *
   * @param appkey
   * @param username
   * @param password
   *
   * @return
   *
   * @throws Exception
   */
  public Object[] getUserBlogs(String appkey, String username, String password)
          throws Exception
  {
    Object[] result = null;
    LoginContext ctx = login(username, password);

    if (ctx != null)
    {
      logout(ctx);
    }

    return result;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param appkey
   * @param blogId
   * @param username
   * @param password
   * @param template
   * @param templateType
   *
   * @return
   *
   * @throws Exception
   */
  public Boolean setTemplate(String appkey, String blogId, String username,
                             String password, String template,
                             String templateType)
          throws Exception
  {
    Boolean result = Boolean.TRUE;

    return result;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param id
   *
   * @return
   *
   * @throws XmlRpcException
   */
  protected Long convertId(String id) throws XmlRpcException
  {
    Long result = null;

    try
    {
      result = Long.parseLong(id);
    }
    catch (NumberFormatException ex)
    {
      throw new XmlRpcException("wrong id format");
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param blogId
   *
   * @return
   *
   * @throws XmlRpcException
   */
  protected Blog findBlog(String blogId) throws XmlRpcException
  {
    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();
    Blog blog = blogDAO.find(convertId(blogId));

    if (blog == null)
    {
      throw new XmlRpcException("no such blog");
    }

    if (!blog.isActive())
    {
      throw new XmlRpcException("blog is disabled");
    }

    return blog;
  }

  /**
   * Method description
   *
   *
   * @param username
   * @param password
   *
   * @return
   *
   * @throws XmlRpcNotAuthorizedException
   */
  protected LoginContext login(String username, String password)
          throws XmlRpcNotAuthorizedException
  {
    LoginContext ctx = null;

    try
    {
      BlogContext context = BlogContext.getInstance();

      ctx = context.buildLoginContext(username, password.toCharArray());
      ctx.login();
    }
    catch (LoginException ex)
    {
      logger.log(Level.WARNING, null, ex);

      throw new XmlRpcNotAuthorizedException("login failure");
    }

    return ctx;
  }

  /**
   * Method description
   *
   *
   * @param ctx
   *
   * @throws XmlRpcNotAuthorizedException
   */
  protected void logout(LoginContext ctx) throws XmlRpcNotAuthorizedException
  {
    try
    {
      ctx.logout();
    }
    catch (LoginException ex)
    {
      logger.log(Level.WARNING, null, ex);

      throw new XmlRpcNotAuthorizedException("logout failure");
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Logger logger;
}
