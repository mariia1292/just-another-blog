/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.ContentObject;

import sonia.macro.browse.MacroWidget;

/**
 *
 * @author sdorra
 */
public interface BlogMacroWidget extends MacroWidget
{

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   * @param name
   * @param param
   *
   * @return
   */
  public String getFormElement(BlogRequest request, ContentObject object,
                               String name, String param);

  /**
   * Method description
   *
   *
   * @param request
   * @param obejct
   * @param name
   * @param param
   *
   * @return
   */
  public String getResult(BlogRequest request, ContentObject obejct,
                          String name, String param);
}
