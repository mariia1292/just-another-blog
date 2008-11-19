/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sdorra
 */
public abstract class AbstractMappingHandler implements MappingHandler
{

  /**
   * Constructs ...
   *
   */
  public AbstractMappingHandler()
  {
    logger = Logger.getLogger(getClass().getName());
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  public BlogBean getBlogBean(FacesContext context)
  {
    BlogBean blogBean =
      (BlogBean) context.getExternalContext().getSessionMap().get("BlogBean");

    if (blogBean == null)
    {
      blogBean = new BlogBean();
    }

    return blogBean;
  }

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  public BlogRequest getRequest(FacesContext context)
  {
    BlogRequest request = null;
    Object req = context.getExternalContext().getRequest();

    if (req instanceof BlogRequest)
    {
      request = (BlogRequest) req;
    }
    else if (req instanceof HttpServletRequest)
    {
      request = new BlogRequest(request);
    }

    return request;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blogBean
   * @param list
   * @param position
   * @param max
   * @param prefix
   */
  public void setEntries(BlogBean blogBean, List list, int position, int max,
                         String prefix)
  {
    int size = list.size();

    if (position >= size)
    {
      position = 0;
    }

    if (position + max < size)
    {
      String uri = prefix + (position + max);

      blogBean.setPrevUri(uri);
    }

    if (position - max >= 0)
    {
      String uri = prefix + (position - max);

      blogBean.setNextUri(uri);
    }

    Collections.reverse(list);
    list = list.subList(position, (position + max < size)
                                  ? max + position
                                  : size);
    blogBean.setEntries(new ListDataModel(list));
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Logger logger;
}
