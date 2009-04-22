/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

/**
 *
 * @author sdorra
 */
public interface InstallationListener
{

  /**
   * Method description
   *
   *
   * @param ctx
   */
  public void afterInstallation(BlogContext ctx);

  /**
   * Method description
   *
   *
   * @param ctx
   */
  public void beforeInstallation(BlogContext ctx);
}
