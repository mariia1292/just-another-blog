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

import sonia.plugin.Plugin;
import sonia.plugin.PluginContext;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
   * @param exclude
   */
  public void setExclude(String exclude)
  {
    this.exclude = exclude;
  }

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
    List<Plugin> plugins = pluginContext.getPlugins();

    if (Util.hasContent(plugins))
    {
      if (Util.hasContent(exclude))
      {
        List<String> excludeList = Arrays.asList(exclude.split(","));
        List<Plugin> newList = new ArrayList<Plugin>();

        for (Plugin p : plugins)
        {
          if (!excludeList.contains(p.getName()))
          {
            newList.add(p);
          }
        }

        plugins = newList;
      }

      Collections.sort(plugins, new PluginComparator());
    }

    parameters.put("plugins", plugins);
    parameters.put("style", style);
    parameters.put("styleClass", styleClass);

    return parseTemplate(parameters, TEMPLATE);
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/10/12
   * @author         Enter your name here...
   */
  private class PluginComparator implements Comparator<Plugin>
  {

    /**
     * Method description
     *
     *
     * @param o1
     * @param o2
     *
     * @return
     */
    public int compare(Plugin o1, Plugin o2)
    {
      return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String exclude;

  /** Field description */
  @Context
  private PluginContext pluginContext;

  /** Field description */
  private String style;

  /** Field description */
  private String styleClass;
}
