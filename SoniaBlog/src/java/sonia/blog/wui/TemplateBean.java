/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.template.Template;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Logger;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author sdorra
 */
public class TemplateBean extends AbstractBean
{

  /** Field description */
  private static Logger logger = Logger.getLogger(TemplateBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public TemplateBean()
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
  public String applyTemplate()
  {
    String result = FAILURE;
    Template template = (Template) templates.getRowData();

    if (template != null)
    {
      Blog blog = getRequest().getCurrentBlog();

      blog.setTemplate(template.getPath());

      BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();

      if (blogDAO.edit(blog))
      {
        result = SUCCESS;
        getMessageHandler().info("changeTemplateSuccess");
      }
      else
      {
        getMessageHandler().error("changeTemplateFailure");
      }
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
  public DataModel getTemplates()
  {
    templates = new ListDataModel();

    Blog blog = getRequest().getCurrentBlog();

    List<Template> templateList =
      BlogContext.getInstance().getTemplateManager().getTemplates( blog );

    if (templateList != null)
    {
      templates.setWrappedData(templateList);
    }

    return templates;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel templates;
}
