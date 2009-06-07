/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;

import sonia.macro.Macro;
import sonia.macro.MacroParser;
import sonia.macro.MacroResult;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public abstract class FilterMapping implements Mapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(FilterMapping.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  protected abstract String handleFilterMapping(BlogRequest request,
          BlogResponse response, String[] param)
          throws IOException, ServletException;

  /**
   * Method description
   *
   *
   * @param request
   * @param resonse
   * @param param
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public boolean handleMapping(BlogRequest request, BlogResponse resonse,
                               String[] param)
          throws IOException, ServletException
  {
    boolean result = false;
    String viewId = handleFilterMapping(request, resonse, param);

    if (Util.isBlank(viewId))
    {
      logger.severe("viewid is null, sending '404 not found'");
      resonse.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
    else
    {
      request.setViewId(viewId);
      result = true;
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<WebResource> getResources()
  {
    return resources;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param viewIdSuffix
   *
   * @return
   */
  protected String buildTemplateViewId(BlogRequest request, String viewIdSuffix)
  {
    return buildTemplateViewId(request.getCurrentBlog(), viewIdSuffix);
  }

  /**
   * Method description
   *
   *
   * @param blog
   * @param viewIdSuffix
   *
   * @return
   */
  protected String buildTemplateViewId(Blog blog, String viewIdSuffix)
  {
    StringBuffer templateBuffer = new StringBuffer();

    templateBuffer.append(blog.getTemplate()).append("/").append(viewIdSuffix);

    return templateBuffer.toString();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   * @param teaser
   */
  protected void setDisplayContent(BlogRequest request, ContentObject object,
                                   boolean teaser)
  {
    Map<String, ?> env = getEnvironment(request, object);

    setDisplayContent(env, object, teaser);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param objects
   * @param teaser
   */
  protected void setDisplayContent(BlogRequest request,
                                   List<? extends ContentObject> objects,
                                   boolean teaser)
  {
    for (ContentObject object : objects)
    {
      Map<String, ?> env = getEnvironment(request, object);

      setDisplayContent(env, object, teaser);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   *
   * @return
   */
  private Map<String, ?> getEnvironment(BlogRequest request,
          ContentObject object)
  {
    Map<String, Object> env = new HashMap<String, Object>();

    env.put("request", request);
    env.put("object", object);
    env.put("blog", request.getCurrentBlog());

    LinkBuilder builder = BlogContext.getInstance().getLinkBuilder();

    env.put("linkBase", builder.buildLink(request, "/"));

    return env;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param env
   * @param object
   * @param teaser
   */
  private void setDisplayContent(Map<String, ?> env, ContentObject object,
                                 boolean teaser)
  {
    MacroParser parser = BlogContext.getInstance().getMacroParser();
    String text = null;

    if (teaser && Util.hasContent(object.getTeaser()))
    {
      text = object.getTeaser();
    }
    else
    {
      text = object.getContent();
    }

    MacroResult result = parser.parseText(env, text);
    List<Macro> macros = result.getMacros();

    if (Util.hasContent(macros))
    {
      for (Macro macro : macros)
      {
        if (macro instanceof WebMacro)
        {
          WebMacro webMacro = (WebMacro) macro;
          List<WebResource> wr = webMacro.getResources();

          if (Util.hasContent(wr))
          {
            resources.addAll(wr);
          }
        }
      }
    }

    object.setDisplayContent(result.getText());
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected List<WebResource> resources = new ArrayList<WebResource>();
}
