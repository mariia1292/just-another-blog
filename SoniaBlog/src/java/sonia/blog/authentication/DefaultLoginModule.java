/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.authentication.RolePrincipal;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.ServiceReference;

import sonia.security.authentication.LoginModule;
import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.security.Principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import javax.security.auth.login.LoginException;

/**
 *
 * @author sdorra
 */
public class DefaultLoginModule extends LoginModule
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultLoginModule.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DefaultLoginModule()
  {
    reference =
      BlogContext.getInstance().getServiceRegistry().getServiceReference(
        Constants.SERVCIE_ENCRYPTION);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param user
   *
   * @return
   */
  @Override
  public Collection<? extends Principal> getRoles(Principal user)
  {
    List<RolePrincipal> roles = new ArrayList<RolePrincipal>();
    User u = (User) user;

    if (u.isGlobalAdmin())
    {
      roles.add(new RolePrincipal(Role.ADMIN));
    }

    return null;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param username
   * @param password
   *
   * @return
   *
   * @throws LoginException
   */
  @Override
  protected Principal handleLogin(String username, char[] password)
          throws LoginException
  {
    logger.info(username + " try to login");

    String passwordString = new String(password);

    if ((reference != null) && (reference.getImplementation() != null))
    {
      Encryption enc = (Encryption) reference.getImplementation();

      passwordString = enc.encrypt(passwordString);
    }

    EntityManager em = BlogContext.getInstance().getEntityManager();
    Query q = em.createNamedQuery("User.login");

    q.setParameter("name", username);
    q.setParameter("password", passwordString);

    User u = null;

    try
    {
      u = (User) q.getSingleResult();
      em.getTransaction().begin();

      try
      {
        u.setLastLogin(new Date());
        u = em.merge(u);
        em.getTransaction().commit();
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        logger.log(Level.SEVERE, null, ex);
      }
    }
    catch (NoResultException ex)
    {

      // do nothing
    }

    return u;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference reference;
}
