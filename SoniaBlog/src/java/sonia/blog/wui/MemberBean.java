/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class MemberBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   *
   * @param event
   */
  public void roleChanged(ValueChangeEvent event)
  {
    BlogMember member = (BlogMember) members.getRowData();

    member.setRole((Role) event.getNewValue());

    if (member != null)
    {
      EntityManager em = BlogContext.getInstance().getEntityManager();

      em.getTransaction().begin();

      try
      {
        member = em.merge(member);
        em.getTransaction().commit();
        messageHandler.info("changeRoleSuccess");
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
        messageHandler.error("changeRoleFailure");
      }
      finally
      {
        em.close();
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getMembers()
  {
    members = new ListDataModel();

    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("BlogMember.findByBlog");

    q.setParameter("blog", getRequest().getCurrentBlog());

    try
    {
      List list = q.getResultList();

      members.setWrappedData(list);
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      em.close();
    }

    return members;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getRoleItems()
  {
    SelectItem[] items = new SelectItem[3];
    ResourceBundle bundle = getResourceBundle("label");

    items[0] = new SelectItem(Role.READER,
                              bundle.getString(Role.READER.name()));
    items[1] = new SelectItem(Role.AUTHOR,
                              bundle.getString(Role.AUTHOR.name()));
    items[2] = new SelectItem(Role.ADMIN, bundle.getString(Role.ADMIN.name()));

    return items;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel members;
}
