/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.install;

//~--- JDK imports ------------------------------------------------------------

import java.net.URLConnection;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import sonia.net.FileNameMap;

/**
 * Web application lifecycle listener.
 * @author Sebastian Sdorra
 */
public class InstallContextListener implements ServletContextListener
{

  /**
   * Method description
   *
   *
   * @param sce
   */
  public void contextDestroyed(ServletContextEvent sce) {}

  /**
   * Method description
   *
   *
   * @param sce
   */
  public void contextInitialized(ServletContextEvent sce)
  {
    URLConnection.setFileNameMap( new FileNameMap() );
    InstallManager.install(sce.getServletContext());
  }
}
