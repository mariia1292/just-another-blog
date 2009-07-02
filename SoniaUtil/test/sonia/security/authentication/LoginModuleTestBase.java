/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.security.authentication;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.security.Principal;

import java.util.HashMap;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author sdorra
 */
public abstract class LoginModuleTestBase
{

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract Class<? extends LoginModule> getLoginModule();

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract char[] getPassword();

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract String getRolename();

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract String getUsername();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public void testFail()
  {
    String username = "time" + System.currentTimeMillis();
    char[] password = { 'a', 'b', 'c', 'd' };
    LoginContext ctx = buildLoginContext(username, password);

    try
    {
      ctx.login();
      fail("fail login success");
    }
    catch (LoginException ex)
    {
      assertNotNull(ex);
    }

    assertFalse(checkPrincipal(ctx, username));
  }

  /**
   * Method description
   *
   */
  @Test
  public void testLogin()
  {
    String username = getUsername();
    char[] password = getPassword();
    String rolename = getRolename();
    LoginContext ctx = buildLoginContext(username, password);

    login(ctx, username, rolename);
  }

  /**
   * Method description
   *
   */
  @Test
  public void testLogout()
  {
    String username = getUsername();
    char[] password = getPassword();
    String rolename = getRolename();
    LoginContext ctx = buildLoginContext(username, password);

    login(ctx, username, rolename);

    try
    {
      ctx.logout();
      assertFalse(checkPrincipal(ctx, username));
      assertFalse(checkPrincipal(ctx, rolename));
    }
    catch (LoginException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  /**
   * Method description
   *
   *
   * @param username
   * @param password
   *
   * @return
   */
  private LoginContext buildLoginContext(String username, char[] password)
  {
    LoginContext ctx = null;
    Class<? extends LoginModule> module = getLoginModule();

    try
    {
      ctx = new LoginContext(module.getName(), new Subject(),
                             new LoginCallbackHandler(username, password),
                             new TestConfiguration(module));
      assertNotNull(ctx);
    }
    catch (LoginException ex)
    {
      fail(ex.getLocalizedMessage());
    }

    return ctx;
  }

  /**
   * Method description
   *
   *
   * @param ctx
   * @param username
   *
   * @return
   */
  private boolean checkPrincipal(LoginContext ctx, String username)
  {
    boolean result = false;
    Subject subject = ctx.getSubject();

    if (subject != null)
    {
      Set<Principal> principals = subject.getPrincipals();

      if ((principals != null) &&!principals.isEmpty())
      {
        assertNotNull(principals);
        assertFalse(principals.isEmpty());

        for (Principal p : principals)
        {
          if (p.getName().equals(username))
          {
            result = true;

            break;
          }
        }
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param ctx
   * @param username
   * @param rolename
   */
  private void login(LoginContext ctx, String username, String rolename)
  {
    try
    {
      ctx.login();
      assertTrue(checkPrincipal(ctx, username));
      assertTrue(checkPrincipal(ctx, rolename));
    }
    catch (LoginException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/07/02
   * @author         Enter your name here...
   */
  private class TestConfiguration extends Configuration
  {

    /**
     * Constructs ...
     *
     *
     * @param module
     */
    public TestConfiguration(Class<? extends LoginModule> module)
    {
      this.module = module;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param name
     *
     * @return
     */
    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name)
    {
      return new AppConfigurationEntry[] {
        new AppConfigurationEntry(
            module.getName(),
            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
            new HashMap<String, Object>()) };
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private Class<? extends LoginModule> module;
  }
}
