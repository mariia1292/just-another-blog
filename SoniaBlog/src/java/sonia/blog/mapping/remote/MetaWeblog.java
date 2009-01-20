/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping.remote;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

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
  public Map getCategories(String blogId, String username, String password)
          throws Exception
  {
    return null;
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
    Map result = null;
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
   *
   * @return
   *
   * @throws Exception
   */
  public Map getRecentPosts(String blogId, String username, String password)
          throws Exception
  {
    return null;
  }
}
