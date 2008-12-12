/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.User;

import sonia.config.Configuration;

import sonia.plugin.ServiceReference;

import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class RegistrationBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public RegistrationBean()
  {
    super();
    this.user = new User();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param em
   *
   * @return
   */
  public String checkNameAndEmail(EntityManager em)
  {
    String result = SUCCESS;
    User u = null;

    try
    {
      Query q = em.createNamedQuery("User.findByName");

      q.setParameter("name", user.getName());
      u = (User) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    if (u != null)
    {
      getMessageHandler().error("regform:username", "nameAllreadyExists", null,
                                user.getName());
      result = FAILURE;
    }
    else
    {
      try
      {
        Query q = em.createNamedQuery("User.findByEmail");

        q.setParameter("email", user.getEmail());
        u = (User) q.getSingleResult();
      }
      catch (NoResultException ex) {}

      if (u != null)
      {
        getMessageHandler().error("regform:email", "emailAllreadyExists", null,
                                  user.getEmail());
        result = FAILURE;
      }
    }

    return result;
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

    if (isPermitted())
    {
      if (user.getPassword().equals(passwordRepeat))
      {
        user.setPassword(encryptPassword(passwordRepeat));

        EntityManager em = BlogContext.getInstance().getEntityManager();

        result = checkNameAndEmail(em);

        if (result.equals(SUCCESS))
        {
          result = createUser(em);
        }

        redirect();

        if (em != null)
        {
          em.close();
        }
      }
      else
      {
        getMessageHandler().error("passwordsNotEqual");
        result = FAILURE;
      }
    }
    else
    {
      getMessageHandler().error("registrationDisabled");
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
  public String getPasswordRepeat()
  {
    return passwordRepeat;
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

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param passwordRepeat
   */
  public void setPasswordRepeat(String passwordRepeat)
  {
    this.passwordRepeat = passwordRepeat;
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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param em
   *
   * @return
   */
  private String createUser(EntityManager em)
  {
    String result = SUCCESS;

    em.getTransaction().begin();

    try
    {
      em.persist(user);
      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      getMessageHandler().error("unknownError");
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param password
   *
   * @return
   */
  private String encryptPassword(String password)
  {
    ServiceReference reference =
      BlogContext.getInstance().getServiceRegistry().getServiceReference(
          Constants.SERVCIE_ENCRYPTION);

    if ((reference != null) && (reference.getImplementation() != null))
    {
      Encryption enc = (Encryption) reference.getImplementation();

      password = enc.encrypt(password);
    }

    return password;
  }

  /**
   * Method description
   *
   */
  private void redirect()
  {
    BlogRequest request = getRequest();
    String uri = BlogContext.getInstance().getLinkBuilder().buildLink(request,
                   request.getCurrentBlog());

    sendRedirect(uri);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private boolean isPermitted()
  {
    Configuration config = BlogContext.getInstance().getConfiguration();

    return config.getBoolean(Constants.CONFIG_ALLOW_REGISTRATION,
                             Boolean.FALSE);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String passwordRepeat;

  /** Field description */
  private User user;
}
