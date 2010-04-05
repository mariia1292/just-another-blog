/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.webstart.repository.RepositoryManager;

//~--- JDK imports ------------------------------------------------------------

import javax.servlet.ServletContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class JnlpContext
{

  /** Field description */
  private static JnlpContext instance;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  private JnlpContext() {}

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static JnlpContext getInstance()
  {
    if (instance == null)
    {
      instance = new JnlpContext();
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param servletContext
   */
  public void init(ServletContext servletContext, RepositoryManager repositoryManager)
  {
    this.servletContext = servletContext;
    this.repositoryManager = repositoryManager;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public RepositoryManager getRepositoryManager()
  {
    return repositoryManager;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ServletContext getServletContext()
  {
    return servletContext;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isInstalled()
  {
    return servletContext != null && repositoryManager != null;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private RepositoryManager repositoryManager;

  /** Field description */
  private ServletContext servletContext;
}
