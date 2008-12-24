/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.converter;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.Category;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import javax.persistence.EntityManager;

/**
 *
 * @author sdorra
 */
public class CategoryConverter implements Converter
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(CategoryConverter.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   * @param value
   *
   * @return
   */
  public Object getAsObject(FacesContext context, UIComponent component,
                            String value)
  {
    Object object = null;

    EntityManager em = BlogContext.getInstance().getEntityManager();
    try
    {
      

      object = em.find(Category.class, Long.parseLong(value));
    }
    catch (NumberFormatException ex)
    {
      logger.log(Level.WARNING, null, ex);
    }
    finally
    {
      em.close();
    }

    return object;
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   * @param value
   *
   * @return
   */
  public String getAsString(FacesContext context, UIComponent component,
                            Object value)
  {
    String result = null;

    if (value instanceof Category)
    {
      result = ((Category) value).getId().toString();
    }

    return result;
  }
}
