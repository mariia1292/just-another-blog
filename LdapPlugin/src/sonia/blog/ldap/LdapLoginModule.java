/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.ldap;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.User;

import sonia.config.ConfigurationListener;
import sonia.config.ModifyableConfiguration;

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

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import javax.security.auth.login.LoginException;

/**
 *
 * @author sdorra
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
   * @param em
   * @param lu
   *
   * @return
   */
  private User createUser(EntityManager em, LdapUser lu)
  {
    User u = null;

    em.getTransaction().begin();

    try
    {
      u = new User();
      u.setName(lu.getName());
      u.setActive(Boolean.TRUE);
      u.setEmail(lu.getMail());
      u.setPassword("ldap");
      u.setDisplayName(lu.getDisplayName());
      u.setLastLogin(new Date());
      em.persist(u);
      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.getTransaction().rollback();
      }

      logger.log(Level.SEVERE, null, ex);
      u = null;
    }

    return u;
  }

  /**
   * Method description
   *
   *
   * @param config
   */
  private void load(ModifyableConfiguration config)
  {
    String servername = config.getString(LdapConfigBean.CONFIG_LDAP_HOST);
    int port = config.getInteger(LdapConfigBean.CONFIG_LDAP_PORT, 389);

    url = "ldap://" + servername + ":" + port;
    searchDn = config.getString(LdapConfigBean.CONFIG_LDAP_BASEDN);
    ssl = config.getBoolean(LdapConfigBean.CONFIG_LDAP_SSL, Boolean.FALSE);
    bindUser = config.getString(LdapConfigBean.CONFIG_LDAP_USER);
    bindPassword = config.getString(LdapConfigBean.CONFIG_LDAP_PASSWORD);
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
   * @param em
   * @param user
   * @param lu
   */
  private void updateUser(EntityManager em, User user, LdapUser lu)
  {
    user.setDisplayName(lu.getDisplayName());
    user.setEmail(lu.getMail());
    user.setLastLogin(new Date());
    em.getTransaction().begin();

    try
    {
      user = em.merge(user);
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
   */
  private User getUser(LdapUser user)
  {
    User result = null;
    EntityManager em = BlogContext.getInstance().getEntityManager();

    try
    {
      Query q = em.createNamedQuery("User.findByName");

      q.setParameter("name", user.getName());
      result = (User) q.getSingleResult();
    }
    catch (NoResultException ex) {}

    if (result == null)
    {
      if (logger.isLoggable(Level.INFO))
      {
        logger.info("creating user " + user.getName());
      }

      result = createUser(em, user);
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

      updateUser(em, result, user);
    }

    em.close();

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
