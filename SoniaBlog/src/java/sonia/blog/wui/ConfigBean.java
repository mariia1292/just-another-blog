/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.util.BlogUtil;

import sonia.plugin.service.Service;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author sdorra
 */
public class ConfigBean extends AbstractBean
{

  /** Field description */
  private static Logger logger = Logger.getLogger(ConfigBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public ConfigBean()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String reIndex()
  {
    BlogContext.getInstance().getSearchContext().reIndex(getBlogSession(),
            getBlog());
    getMessageHandler().info("rebuildIndex");

    return SUCCESS;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;
    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();

    if (blogDAO.edit(blog))
    {
      getMessageHandler().info("unpdateConfigSuccess");
    }
    else
    {
      getMessageHandler().error("unpdateConfigFailure");
      result = FAILURE;
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
  public Blog getBlog()
  {
    blog = getRequest().getCurrentBlog();

    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getLocaleItems()
  {
    return BlogUtil.getLocaleItems(FacesContext.getCurrentInstance());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<String> getProviders()
  {
    return providers;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getTimeZoneItems()
  {
    return BlogUtil.getTimeZoneItems();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isSearchReIndexAble()
  {
    return BlogContext.getInstance().getSearchContext().isReIndexable();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  @Service(Constants.SERVICE_BLOGCONFIGPROVIDER)
  private List<String> providers;
}
