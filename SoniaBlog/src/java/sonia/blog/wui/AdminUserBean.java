/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.ServiceReference;

import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

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
public class AdminUserBean extends AbstractBean
{

  /** Field description */
  public static final String BACK = "back";

  /** Field description */
  public static final String DETAIL = "detail";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String detail()
  {
    user = (User) users.getRowData();

    return DETAIL;
  }

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
        getMessageHandler().info("changeRoleSuccess");
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error("changeRoleFailure");
      }
      finally
      {
        em.close();
      }
    }
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
    EntityManager em = BlogContext.getInstance().getEntityManager();

    em.getTransaction().begin();

    try
    {
      user = em.merge(user);
      em.getTransaction().commit();
      getMessageHandler().info("userSettingsUpdateSuccess");
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      result = FAILURE;
      getMessageHandler().error("unknownError");
    }
    finally
    {
      em.close();
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String savePassword()
  {
    String result = SUCCESS;

    if (passwordRetry.equals(user.getPassword()))
    {
      ServiceReference reference =
        BlogContext.getInstance().getServiceRegistry().getServiceReference(
            Constants.SERVCIE_ENCRYPTION);

      if (reference != null)
      {
        Encryption enc = (Encryption) reference.getImplementation();

        if (enc != null)
        {
          user.setPassword(enc.encrypt(passwordRetry));
          result = save();
        }
      }
    }
    else
    {
      getMessageHandler().warn("passwordsNotEqual");
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
  public DataModel getMembers()
  {
    if (members == null)
    {
      EntityManager em = BlogContext.getInstance().getEntityManager();

      try
      {
        Query q = em.createNamedQuery("BlogMember.findByUser");

        q.setParameter("user", user);
        members = new ListDataModel(q.getResultList());
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
      finally
      {
        em.close();
      }
    }

    return members;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPasswordRetry()
  {
    return passwordRetry;
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

  /**
   * Method description
   *
   *
   * @return
   */
  public User getUser()
  {
    return user;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getUsers()
  {
    EntityManager em = BlogContext.getInstance().getEntityManager();

    try
    {
      String query = onlyActive
                     ? "User.findAllActives"
                     : "User.findAll";

      System.out.println(query);

      Query q = em.createNamedQuery(query);

      users = new ListDataModel(q.getResultList());
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      em.close();
    }

    return users;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isOnlyActive()
  {
    return onlyActive;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param members
   */
  public void setMembers(DataModel members)
  {
    this.members = members;
  }

  /**
   * Method description
   *
   *
   * @param onlyActive
   */
  public void setOnlyActive(boolean onlyActive)
  {
    this.onlyActive = onlyActive;
  }

  /**
   * Method description
   *
   *
   * @param passwordRetry
   */
  public void setPasswordRetry(String passwordRetry)
  {
    this.passwordRetry = passwordRetry;
  }

  /**
   * Method description
   *
   *
   * @param user
   */
  public void setUser(User user)
  {
    this.user = user;
  }

  /**
   * Method description
   *
   *
   * @param users
   */
  public void setUsers(DataModel users)
  {
    this.users = users;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel members;

  /** Field description */
  private boolean onlyActive;

  /** Field description */
  private String passwordRetry;

  /** Field description */
  private User user;

  /** Field description */
  private DataModel users;
}
