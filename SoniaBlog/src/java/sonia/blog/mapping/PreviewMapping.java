/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.mapping.FilterMapping;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.entity.ContentObject;
import sonia.blog.util.BlogUtil;
import sonia.blog.wui.BlogBean;
import sonia.blog.wui.EntryBean;
import sonia.blog.wui.PageAuthorBean;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.servlet.ServletException;

/**
 *
 * @author sdorra
 */
public class PreviewMapping extends FilterMapping
{

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingNavigation getMappingNavigation()
  {
    return null;
  }

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
  @Override
  protected String handleFilterMapping(BlogRequest request,
          BlogResponse response, String[] param)
          throws IOException, ServletException
  {
    String result = null;

    if ((param != null) && (param.length == 1))
    {
      ContentObject object = getObject(request, param[0]);
      BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                            BlogBean.NAME);

      if ((blogBean != null) && (object != null))
      {
        setDisplayContent(request, object, false);
        blogBean.setEntry(object);
        result = buildTemplateViewId(request, Constants.TEMPLATE_DETAIL);
      }
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param type
   *
   * @return
   */
  private ContentObject getObject(BlogRequest request, String type)
  {
    ContentObject object = null;

    if (type.equals("entry"))
    {
      EntryBean entryBean = BlogUtil.getSessionBean(request, EntryBean.class,
                              EntryBean.NAME);

      if (entryBean != null)
      {
        object = entryBean.getEntry();
      }
    }
    else if (type.equals("page"))
    {
      PageAuthorBean pageBean = BlogUtil.getSessionBean(request,
                                  PageAuthorBean.class, PageAuthorBean.NAME);

      if (pageBean != null)
      {
        object = pageBean.getPage();
      }
    }

    return object;
  }
}
