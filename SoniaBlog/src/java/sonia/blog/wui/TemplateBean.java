/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.template.Template;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import javax.persistence.EntityManager;

/**
 *
 * @author sdorra
 */
public class TemplateBean extends AbstractBean
{

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
      EntityManager em = BlogContext.getInstance().getEntityManager();

      em.getTransaction().begin();

      try
      {
        Blog blog = getRequest().getCurrentBlog();

        blog.setTemplate(template.getPath());
        em.merge(blog);
        em.getTransaction().commit();
        getMessageHandler().info("changeTemplateSuccess");
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("changeTemplateFailure");
      }
      finally
      {
        em.close();
      }

      result = SUCCESS;
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

    List<Template> templateList =
      BlogContext.getInstance().getTemplateManager().getTemplates();

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
