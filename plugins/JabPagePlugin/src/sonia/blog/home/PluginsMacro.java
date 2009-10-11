/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.home;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Context;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

import sonia.plugin.PluginContext;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class PluginsMacro extends AbstractBlogMacro
{

  /** Field description */
  public static final String NAME = "plugins";

  /** Field description */
  public static final String TEMPLATE =
    "/sonia/blog/home/template/plugins.html";

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param style
   */
  public void setStyle(String style)
  {
    this.style = style;
  }

  /**
   * Method description
   *
   *
   * @param styleClass
   */
  public void setStyleClass(String styleClass)
  {
    this.styleClass = styleClass;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  @Override
  protected String doBody(BlogRequest request, String linkBase,
                          ContentObject object, String body)
  {
    Map<String, Object> parameters = new HashMap<String, Object>();

    parameters.put("plugins", pluginContext.getPlugins());
    parameters.put("style", style);
    parameters.put("styleClass", styleClass);

    return parseTemplate(parameters, TEMPLATE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private PluginContext pluginContext;

  /** Field description */
  private String style;

  /** Field description */
  private String styleClass;
}
