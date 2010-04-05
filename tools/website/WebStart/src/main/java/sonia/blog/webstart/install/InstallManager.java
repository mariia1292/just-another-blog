/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.install;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.webstart.JnlpContext;
import sonia.blog.webstart.repository.DefaultRepositoryManager;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;

import java.util.Properties;

import javax.servlet.ServletContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class InstallManager
{

  /** Field description */
  private static final String BASEFILE = "/WEB-INF/base.properties";

  /** Field description */
  private static final String PROPERTY = "repository.home";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param servletContext
   */
  public static void install(ServletContext servletContext)
  {
    File baseFile = getBaseFile(servletContext);

    if (baseFile.exists())
    {
      Properties properties = new Properties();

      try
      {
        properties.load(new FileInputStream(baseFile));

        String path = properties.getProperty(PROPERTY);

        install(servletContext, new File(path), false);
      }
      catch (IOException ex)
      {
        throw new RuntimeException(ex);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param servletContext
   * @param repostitory
   */
  public static void install(ServletContext servletContext, File repostitory)
  {
    install(servletContext, repostitory, true);
  }

  /**
   * Method description
   *
   *
   * @param servletContext
   * @param repostitory
   * @param save
   */
  private static void install(ServletContext servletContext, File repostitory,
                              boolean save)
  {
    JnlpContext.getInstance().init(servletContext,
                                   new DefaultRepositoryManager(repostitory));

    if (save)
    {
      Properties properties = new Properties();

      properties.setProperty(PROPERTY, repostitory.getAbsolutePath());

      try
      {
        properties.store(new FileOutputStream(getBaseFile(servletContext)),
                         "repository path");
      }
      catch (IOException ex)
      {
        throw new RuntimeException(ex);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param servletContext
   *
   * @return
   */
  private static File getBaseFile(ServletContext servletContext)
  {
    return new File(servletContext.getRealPath(BASEFILE));
  }
}
