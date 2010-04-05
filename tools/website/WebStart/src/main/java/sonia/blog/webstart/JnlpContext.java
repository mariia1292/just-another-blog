/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.webstart.repository.RepositoryManager;
import sonia.blog.webstart.statistic.StatisticManager;

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
   * @param repositoryManager
   * @param statisticManager
   */
  public void init(ServletContext servletContext,
                   RepositoryManager repositoryManager,
                   StatisticManager statisticManager)
  {
    this.servletContext = servletContext;
    this.repositoryManager = repositoryManager;
    this.statisticManager = statisticManager;
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
  public StatisticManager getStatisticManager()
  {
    return statisticManager;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isInstalled()
  {
    return (servletContext != null) && (repositoryManager != null)
           && (statisticManager != null);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private RepositoryManager repositoryManager;

  /** Field description */
  private ServletContext servletContext;

  /** Field description */
  private StatisticManager statisticManager;
}
