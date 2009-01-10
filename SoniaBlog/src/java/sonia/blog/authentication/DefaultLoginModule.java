/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.authentication;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.authentication.RolePrincipal;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.service.ServiceReference;

import sonia.security.authentication.LoginModule;
import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.security.Principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
      BlogContext.getInstance().getServiceRegistry().get(Encryption.class,
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

    if ((reference != null) && (reference.get() != null))
    {
      Encryption enc = reference.get();

      passwordString = enc.encrypt(passwordString);
    }

    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();
    User u = userDAO.findByNameAndPassword(username, passwordString);

    if (u != null)
    {
      u.setLastLogin(new Date());
      userDAO.edit(u);
    }

    return u;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference<Encryption> reference;
}
