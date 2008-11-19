/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.plugin;

//~--- JDK imports ------------------------------------------------------------

import javax.servlet.http.HttpServlet;

/**
 *
 * @author sdorra
 */
public abstract class AbstractPluginServlet extends HttpServlet
{

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract String getMapping();
}
