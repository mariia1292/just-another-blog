/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

/**
 *
 * @author sdorra
 */
public interface BlogRequestListener
{

  /**
   * Method description
   *
   *
   * @param request
   */
  public void afterMapping(BlogRequest request);

  /**
   * Method description
   *
   *
   * @param request
   */
  public void beforeMapping(BlogRequest request);
}
