/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
 */


package sonia.blog.ldap;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.entity.User;

import sonia.net.ssl.SSLSocketFactory;

import sonia.security.authentication.LoginModule;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.security.Principal;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import javax.security.auth.login.LoginException;

/**
 *
 * @author Sebastian Sdorra
 */
public class LdapLoginModule extends LoginModule
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(LdapLoginModule.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public LdapLoginModule()
  {
    load(BlogContext.getInstance().getConfiguration());
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
    logger.fine(username + " try ldap login");

    User u = null;

    try
    {
      LdapUser lu = getLdapUser(username);

      if (lu != null)
      {
        if (login(lu.getDn(), password))
        {
          u = getUser(lu);
        }
      }
    }
    catch (NamingException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    if (u != null)
    {
      if (logger.isLoggable(Level.INFO))
      {
        logger.info(username + " successfully logged in");
      }
    }
    else if (logger.isLoggable(Level.WARNING))
    {
      logger.warning(username + " failure during login");
    }

    return u;
  }

  /**
   * Method description
   *
   *
   * @param dn
   * @param password
   *
   * @return
   */
  private Hashtable<String, String> buildEnvironment(String dn, String password)
  {
    Hashtable<String, String> environment = new Hashtable<String, String>();

    environment.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.ldap.LdapCtxFactory");
    environment.put(Context.SECURITY_AUTHENTICATION, "simple");
    environment.put(Context.PROVIDER_URL, url);

    if (ssl)
    {
      environment.put(Context.SECURITY_PROTOCOL, "ssl");

      // TODO: check function
      environment.put("java.naing.ldap.factory.socket",
                      SSLSocketFactory.class.getName());
    }

    environment.put(Context.SECURITY_PRINCIPAL, dn);
    environment.put(Context.SECURITY_CREDENTIALS, password);

    return environment;
  }

  /**
   * Method description
   *
   *
   *
   * @param userDAO
   * @param lu
   *
   * @return
   *
   * @throws LoginException
   */
  private User createUser(UserDAO userDAO, LdapUser lu) throws LoginException
  {
    User u = null;

    u = new User();
    u.setName(lu.getName());
    u.setActive(Boolean.TRUE);
    u.setSelfManaged(false);
    u.setEmail(lu.getMail());
    u.setPassword("ldap");
    u.setDisplayName(lu.getDisplayName());
    u.setLastLogin(new Date());

    if (!userDAO.add(u))
    {
      logger.severe("error during user creation");

      throw new LoginException("error during user creation");
    }

    return u;
  }

  /**
   * Method description
   *
   *
   * @param config
   */
  private void load(BlogConfiguration config)
  {
    String servername = config.getString(LdapConfigBean.CONFIG_LDAP_HOST);
    int port = config.getInteger(LdapConfigBean.CONFIG_LDAP_PORT, 389);

    url = "ldap://" + servername + ":" + port;
    searchDn = config.getString(LdapConfigBean.CONFIG_LDAP_BASEDN);
    ssl = config.getBoolean(LdapConfigBean.CONFIG_LDAP_SSL, Boolean.FALSE);
    bindUser = config.getString(LdapConfigBean.CONFIG_LDAP_USER);
    bindPassword = config.getSecureString(LdapConfigBean.CONFIG_LDAP_PASSWORD);
    scope = config.getString(LdapConfigBean.CONFIG_LDAP_SCOPE);
    filter = config.getString(LdapConfigBean.CONFIG_LDAP_FILTER);
    nameAttribute = config.getString(LdapConfigBean.CONFIG_LDAP_NAMEATTRINUTE);
    mailAttribute = config.getString(LdapConfigBean.CONFIG_LDAP_MAILATTRINUTE);
    displayNameAttribute =
      config.getString(LdapConfigBean.CONFIG_LDAP_DISPLAYNAMEATTRIBUTE);
  }

  /**
   * Method description
   *
   *
   * @param dn
   * @param password
   *
   * @return
   *
   * @throws NamingException
   */
  private boolean login(String dn, char[] password) throws NamingException
  {
    boolean result = false;
    Hashtable<String, String> env = buildEnvironment(dn, new String(password));
    DirContext ctx = null;

    try
    {
      ctx = new InitialDirContext(env);
      result = true;
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (ctx != null)
      {
        ctx.close();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   *
   * @param userDAO
   * @param user
   * @param lu
   *
   * @throws LoginException
   */
  private void updateUser(UserDAO userDAO, User user, LdapUser lu)
          throws LoginException
  {
    user.setDisplayName(lu.getDisplayName());
    user.setEmail(lu.getMail());
    user.setLastLogin(new Date());

    if (!userDAO.edit(user))
    {
      logger.severe("error during user update");

      throw new LoginException("error during user update");
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param username
   *
   * @return
   *
   * @throws NamingException
   */
  private LdapUser getLdapUser(String username) throws NamingException
  {
    LdapUser result = null;
    Hashtable<String, String> environment = buildEnvironment(bindUser,
                                              bindPassword);
    DirContext ctx = null;

    try
    {
      ctx = new InitialDirContext(environment);

      SearchControls sc = new SearchControls();

      if (scope.equalsIgnoreCase("object"))
      {
        sc.setSearchScope(SearchControls.OBJECT_SCOPE);
      }
      else if (scope.equalsIgnoreCase("one"))
      {
        sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
      }
      else
      {
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
      }

      sc.setReturningAttributes(new String[] { nameAttribute, mailAttribute,
              displayNameAttribute });

      NamingEnumeration<SearchResult> results = ctx.search(searchDn, filter,
                                                  new Object[] { username },
                                                  sc);

      if ((results != null) && results.hasMore())
      {
        SearchResult sr = results.next();
        String dn = sr.getNameInNamespace();
        Attribute na = sr.getAttributes().get(nameAttribute);

        if (na != null)
        {
          String name = (String) na.get(0);

          if (!Util.isBlank(name))
          {
            Attribute ma = sr.getAttributes().get(mailAttribute);

            if (ma != null)
            {
              String mail = (String) ma.get(0);

              if (!Util.isBlank(mail))
              {
                Attribute da = sr.getAttributes().get(displayNameAttribute);

                if (da != null)
                {
                  String displayName = (String) da.get(0);

                  if (!Util.isBlank(displayName))
                  {
                    result = new LdapUser(dn, name, displayName, mail);
                  }
                  else
                  {
                    logger.warning("displayName attribute '"
                                   + displayNameAttribute + "' is null");
                  }
                }
                else
                {
                  logger.warning("displayName attribute '"
                                 + displayNameAttribute + "' is null");
                }
              }
              else
              {
                logger.warning("mail attribute '" + mailAttribute
                               + "' is null");
              }
            }
            else
            {
              logger.warning("mail attribute '" + mailAttribute + "' is null");
            }
          }
          else
          {
            logger.warning("name attribute '" + nameAttribute + "' is null");
          }
        }
        else
        {
          logger.warning("name attribute '" + nameAttribute + "' is null");
        }
      }
      else
      {
        logger.warning("ldap search has no result");
      }
    }
    catch (NamingException ex)
    {
      logger.log(Level.SEVERE, null, ex);

      if (ctx != null)
      {
        ctx.close();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   *
   * @param user
   *
   * @return
   *
   * @throws LoginException
   */
  private User getUser(LdapUser user) throws LoginException
  {
    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();
    User result = userDAO.get(user.getName());

    if (result == null)
    {
      if (logger.isLoggable(Level.INFO))
      {
        logger.info("creating user " + user.getName());
      }

      result = createUser(userDAO, user);
    }
    else if (!result.isActive())
    {
      result = null;
      logger.warning(user.getName() + " is disabled");
    }
    else
    {
      if (logger.isLoggable(Level.INFO))
      {
        logger.info("update user " + user.getName());
      }

      updateUser(userDAO, result, user);
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String bindPassword;

  /** Field description */
  private String bindUser;

  /** Field description */
  private String displayNameAttribute;

  /** Field description */
  private String filter;

  /** Field description */
  private String mailAttribute;

  /** Field description */
  private String nameAttribute;

  /** Field description */
  private String scope;

  /** Field description */
  private String searchDn;

  /** Field description */
  private boolean ssl;

  /** Field description */
  private String url;
}