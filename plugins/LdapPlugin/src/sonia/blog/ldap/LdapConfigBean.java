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
import sonia.blog.api.util.AbstractConfigBean;

/**
 *
 * @author Sebastian Sdorra
 */
public class LdapConfigBean extends AbstractConfigBean
{

  /** Field description */
  public static final String CONFIG_LDAP_BASEDN = "ldap.baseDn";

  /** Field description */
  public static final String CONFIG_LDAP_DISPLAYNAMEATTRIBUTE =
    "ldap.attribute.displayName";

  /** Field description */
  public static final String CONFIG_LDAP_ENABLED = "ldap.enabled";

  /** Field description */
  public static final String CONFIG_LDAP_FILTER = "ldap.filter";

  /** Field description */
  public static final String CONFIG_LDAP_HOST = "ldap.host";

  /** Field description */
  public static final String CONFIG_LDAP_MAILATTRINUTE = "ldap.attribute.mail";

  /** Field description */
  public static final String CONFIG_LDAP_NAMEATTRINUTE = "ldap.attribute.name";

  /** Field description */
  public static final String CONFIG_LDAP_PASSWORD = "ldap.bind.password";

  /** Field description */
  public static final String CONFIG_LDAP_PORT = "ldap.port";

  /** Field description */
  public static final String CONFIG_LDAP_SCOPE = "ldap.scope";

  /** Field description */
  public static final String CONFIG_LDAP_SSL = "ldap.ssl";

  /** Field description */
  public static final String CONFIG_LDAP_USER = "ldap.bind.user";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param config
   */
  @Override
  public void load(BlogConfiguration config)
  {
    host = config.getString(CONFIG_LDAP_HOST);
    port = config.getInteger(CONFIG_LDAP_PORT, 389);
    ssl = config.getBoolean(CONFIG_LDAP_SSL, Boolean.FALSE);
    user = config.getString(CONFIG_LDAP_USER);
    password = config.getSecureString(CONFIG_LDAP_PASSWORD);
    filter = config.getString(CONFIG_LDAP_FILTER);
    scope = config.getString(CONFIG_LDAP_SCOPE, "sub");
    enabled = config.getBoolean(CONFIG_LDAP_ENABLED, Boolean.FALSE);
    baseDn = config.getString(CONFIG_LDAP_BASEDN);
    nameAttribute = config.getString(CONFIG_LDAP_NAMEATTRINUTE, "uid");
    mailAttribute = config.getString(CONFIG_LDAP_MAILATTRINUTE, "mail");
    displayNameAttribute = config.getString(CONFIG_LDAP_DISPLAYNAMEATTRIBUTE,
            "cn");
  }

  /**
   * Method description
   *
   *
   * @param config
   */
  @Override
  public void store(BlogConfiguration config)
  {
    config.set(CONFIG_LDAP_HOST, host);
    config.set(CONFIG_LDAP_PORT, port);
    config.set(CONFIG_LDAP_SSL, ssl);
    config.set(CONFIG_LDAP_USER, user);
    config.setSecureString(CONFIG_LDAP_PASSWORD, password);
    config.set(CONFIG_LDAP_FILTER, filter);
    config.set(CONFIG_LDAP_SCOPE, scope);
    config.set(CONFIG_LDAP_ENABLED, enabled);
    config.set(CONFIG_LDAP_BASEDN, baseDn);
    config.set(CONFIG_LDAP_NAMEATTRINUTE, nameAttribute);
    config.set(CONFIG_LDAP_MAILATTRINUTE, mailAttribute);
    config.set(CONFIG_LDAP_DISPLAYNAMEATTRIBUTE, displayNameAttribute);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public boolean verify()
  {
    return true;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getBaseDn()
  {
    return baseDn;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayNameAttribute()
  {
    return displayNameAttribute;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getFilter()
  {
    return filter;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getHost()
  {
    return host;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMailAttribute()
  {
    return mailAttribute;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getNameAttribute()
  {
    return nameAttribute;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getPort()
  {
    return port;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getScope()
  {
    return scope;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getUser()
  {
    return user;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEnabled()
  {
    return enabled;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isSsl()
  {
    return ssl;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param baseDn
   */
  public void setBaseDn(String baseDn)
  {
    this.baseDn = baseDn;
  }

  /**
   * Method description
   *
   *
   * @param displayNameAttribute
   */
  public void setDisplayNameAttribute(String displayNameAttribute)
  {
    this.displayNameAttribute = displayNameAttribute;
  }

  /**
   * Method description
   *
   *
   * @param enabled
   */
  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }

  /**
   * Method description
   *
   *
   * @param filter
   */
  public void setFilter(String filter)
  {
    this.filter = filter;
  }

  /**
   * Method description
   *
   *
   * @param host
   */
  public void setHost(String host)
  {
    this.host = host;
  }

  /**
   * Method description
   *
   *
   * @param mailAttribute
   */
  public void setMailAttribute(String mailAttribute)
  {
    this.mailAttribute = mailAttribute;
  }

  /**
   * Method description
   *
   *
   * @param nameAttribute
   */
  public void setNameAttribute(String nameAttribute)
  {
    this.nameAttribute = nameAttribute;
  }

  /**
   * Method description
   *
   *
   * @param password
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * Method description
   *
   *
   * @param port
   */
  public void setPort(int port)
  {
    this.port = port;
  }

  /**
   * Method description
   *
   *
   * @param scope
   */
  public void setScope(String scope)
  {
    this.scope = scope;
  }

  /**
   * Method description
   *
   *
   * @param ssl
   */
  public void setSsl(boolean ssl)
  {
    this.ssl = ssl;
  }

  /**
   * Method description
   *
   *
   * @param user
   */
  public void setUser(String user)
  {
    this.user = user;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String baseDn;

  /** Field description */
  private String displayNameAttribute;

  /** Field description */
  private boolean enabled;

  /** Field description */
  private String filter;

  /** Field description */
  private String host;

  /** Field description */
  private String mailAttribute;

  /** Field description */
  private String nameAttribute;

  /** Field description */
  private String password;

  /** Field description */
  private int port;

  /** Field description */
  private String scope;

  /** Field description */
  private boolean ssl;

  /** Field description */
  private String user;
}