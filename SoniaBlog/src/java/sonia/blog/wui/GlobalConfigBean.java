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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.MailService;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.spam.SpamInputProtection;
import sonia.blog.api.util.AbstractConfigBean;
import sonia.blog.entity.Blog;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.model.SelectItem;

/**
 *
 * @author Sebastian Sdorra
 */
public class GlobalConfigBean extends AbstractConfigBean
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(GlobalConfigBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public GlobalConfigBean()
  {
    super();

    BlogContext context = BlogContext.getInstance();

    reference = context.getServiceRegistry().get(String.class,
            Constants.SERVCIE_GLOBALCONFIGPROVIDER);
    spamInputServcieReference =
      context.getServiceRegistry().get(SpamInputProtection.class,
                                       Constants.SERVICE_SPAMPROTECTIONMETHOD);

    if (spamInputMethod == null)
    {
      spamInputMethod = spamInputServcieReference.get().getClass().getName();
    }
  }

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
    adminPageSize = config.getInteger(Constants.CONFIG_ADMIN_PAGESIZE, 20);
    allowRegistration = config.getBoolean(Constants.CONFIG_ALLOW_REGISTRATION,
            Boolean.FALSE);
    allowBlogCreation = config.getBoolean(Constants.CONFIG_ALLOW_BLOGCREATION,
            Boolean.FALSE);
    passwordMinLength = config.getInteger(Constants.CONFIG_PASSWORD_MINLENGTH,
            Constants.DEFAULT_PASSWORD_MINLENGTH);
    spamInputMethod = config.getString(Constants.CONFIG_SPAMMETHOD);
    defaultBlog = config.getLong(Constants.CONFIG_DEFAULTBLOG);
    cleanupCode = config.getBoolean(Constants.CONFIG_CLEANUPCODE,
                                    Boolean.FALSE);
    sso = config.getInteger(Constants.CONFIG_SSO, Constants.SSO_ONEPERSESSION);
    domain = config.getString(Constants.CONFIG_DOMAIN, "");
    smtpServer = config.getString(Constants.CONFIG_SMTPSERVER);
    smtpPort = config.getInteger(Constants.CONFIG_SMTPPORT, 25);
    smtpUsername = config.getString(Constants.CONFIG_SMTPUSER);
    smtpPassword = config.getSecureString(Constants.CONFIG_SMTPPASSWORD);
    startTls = config.getBoolean(Constants.CONFIG_SMTPSTARTTLS, Boolean.FALSE);
    registerAcknowledgement =
      config.getBoolean(Constants.CONFIG_REGISTERACKNOWLEDGEMENT, false);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String sendTestMail()
  {
    String result = SUCCESS;

    try
    {
      String from = getRequest().getCurrentBlog().getEmail();
      MailService msgService = BlogContext.getInstance().getMailService();

      msgService.sendMail(testMail, from, "JAB TestMail",
                          "This is a test message");
    }
    catch (Exception ex)
    {
      result = FAILURE;
      logger.log(Level.SEVERE, null, ex);
    }

    return result;
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
    config.set(Constants.CONFIG_ADMIN_PAGESIZE, adminPageSize);
    config.set(Constants.CONFIG_ALLOW_REGISTRATION, allowRegistration);
    config.set(Constants.CONFIG_ALLOW_BLOGCREATION, allowBlogCreation);
    config.set(Constants.CONFIG_PASSWORD_MINLENGTH, passwordMinLength);
    config.set(Constants.CONFIG_SPAMMETHOD, spamInputMethod);
    config.set(Constants.CONFIG_DEFAULTBLOG, defaultBlog);
    config.set(Constants.CONFIG_CLEANUPCODE, cleanupCode);
    config.set(Constants.CONFIG_SSO, sso);
    config.set(Constants.CONFIG_DOMAIN, domain);
    config.set(Constants.CONFIG_SMTPSERVER, smtpServer);
    config.set(Constants.CONFIG_SMTPPORT, smtpPort);
    config.set(Constants.CONFIG_SMTPUSER, smtpUsername);
    config.set(Constants.CONFIG_SMTPSTARTTLS, startTls);

    if (Util.hasContent(smtpPassword))
    {
      config.setSecureString(Constants.CONFIG_SMTPPASSWORD, smtpPassword);
    }
    else
    {
      config.set(Constants.CONFIG_SMTPPASSWORD, null);
    }

    config.set(Constants.CONFIG_REGISTERACKNOWLEDGEMENT,
               registerAcknowledgement);
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
    boolean result = true;

    if (registerAcknowledgement)
    {
      if (Util.isBlank(smtpServer))
      {
        result = false;
        getMessageHandler().warn("mailForm:servername", "mailNotConfigured");
      }
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
  public int getAdminPageSize()
  {
    return adminPageSize;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getBlogItems()
  {
    SelectItem[] items = null;
    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();

    // TODO: scrolling
    List<Blog> blogList = blogDAO.getAll(true, 0, 100);

    if ((blogList != null) &&!blogList.isEmpty())
    {
      int s = blogList.size();

      items = new SelectItem[s];

      for (int i = 0; i < s; i++)
      {
        Blog blog = blogList.get(i);
        String label = blog.getTitle() + " (" + blog.getIdentifier() + ")";

        items[i] = new SelectItem(blog.getId(), label);
      }
    }
    else
    {
      items = new SelectItem[0];
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<String> getConfigurationProviders()
  {
    return reference.getAll();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Long getDefaultBlog()
  {
    return defaultBlog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDomain()
  {
    return domain;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getPasswordMinLength()
  {
    return passwordMinLength;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ServiceReference<String> getReference()
  {
    return reference;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSmtpPassword()
  {
    return smtpPassword;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getSmtpPort()
  {
    return smtpPort;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSmtpServer()
  {
    return smtpServer;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSmtpUsername()
  {
    return smtpUsername;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSpamInputMethod()
  {
    return spamInputMethod;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getSpamInputMethodItems()
  {
    SelectItem[] items = null;
    List<SpamInputProtection> list = spamInputServcieReference.getAll();

    if ((list != null) &&!list.isEmpty())
    {
      int s = list.size();

      items = new SelectItem[s];

      for (int i = 0; i < s; i++)
      {
        SpamInputProtection sp = list.get(i);

        items[i] = new SelectItem(sp.getClass().getName(), sp.getLabel());
      }
    }
    else
    {
      items = new SelectItem[0];
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ServiceReference<SpamInputProtection> getSpamInputServcieReference()
  {
    return spamInputServcieReference;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getSso()
  {
    return sso;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Boolean getStartTls()
  {
    return startTls;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTestMail()
  {
    return testMail;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAllowBlogCreation()
  {
    return allowBlogCreation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAllowRegistration()
  {
    return allowRegistration;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isCleanupCode()
  {
    return cleanupCode;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isRegisterAcknowledgement()
  {
    return registerAcknowledgement;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param adminPageSize
   */
  public void setAdminPageSize(int adminPageSize)
  {
    this.adminPageSize = adminPageSize;
  }

  /**
   * Method description
   *
   *
   * @param allowBlogCreation
   */
  public void setAllowBlogCreation(boolean allowBlogCreation)
  {
    this.allowBlogCreation = allowBlogCreation;
  }

  /**
   * Method description
   *
   *
   * @param allowRegistration
   */
  public void setAllowRegistration(boolean allowRegistration)
  {
    this.allowRegistration = allowRegistration;
  }

  /**
   * Method description
   *
   *
   * @param cleanupCode
   */
  public void setCleanupCode(boolean cleanupCode)
  {
    this.cleanupCode = cleanupCode;
  }

  /**
   * Method description
   *
   *
   * @param defaultBlog
   */
  public void setDefaultBlog(Long defaultBlog)
  {
    this.defaultBlog = defaultBlog;
  }

  /**
   * Method description
   *
   *
   * @param domain
   */
  public void setDomain(String domain)
  {
    this.domain = domain;
  }

  /**
   * Method description
   *
   *
   * @param passwordMinLength
   */
  public void setPasswordMinLength(int passwordMinLength)
  {
    this.passwordMinLength = passwordMinLength;
  }

  /**
   * Method description
   *
   *
   * @param reference
   */
  public void setReference(ServiceReference<String> reference)
  {
    this.reference = reference;
  }

  /**
   * Method description
   *
   *
   * @param registerAcknowledgement
   */
  public void setRegisterAcknowledgement(boolean registerAcknowledgement)
  {
    this.registerAcknowledgement = registerAcknowledgement;
  }

  /**
   * Method description
   *
   *
   * @param smtpPassword
   */
  public void setSmtpPassword(String smtpPassword)
  {
    this.smtpPassword = smtpPassword;
  }

  /**
   * Method description
   *
   *
   * @param smtpPort
   */
  public void setSmtpPort(int smtpPort)
  {
    this.smtpPort = smtpPort;
  }

  /**
   * Method description
   *
   *
   * @param smtpServer
   */
  public void setSmtpServer(String smtpServer)
  {
    this.smtpServer = smtpServer;
  }

  /**
   * Method description
   *
   *
   * @param smtpUsername
   */
  public void setSmtpUsername(String smtpUsername)
  {
    this.smtpUsername = smtpUsername;
  }

  /**
   * Method description
   *
   *
   * @param spamInputMethod
   */
  public void setSpamInputMethod(String spamInputMethod)
  {
    this.spamInputMethod = spamInputMethod;
  }

  /**
   * Method description
   *
   *
   * @param spamInputServcieReference
   */
  public void setSpamInputServcieReference(
          ServiceReference<SpamInputProtection> spamInputServcieReference)
  {
    this.spamInputServcieReference = spamInputServcieReference;
  }

  /**
   * Method description
   *
   *
   * @param sso
   */
  public void setSso(int sso)
  {
    this.sso = sso;
  }

  /**
   * Method description
   *
   *
   * @param startTls
   */
  public void setStartTls(Boolean startTls)
  {
    this.startTls = startTls;
  }

  /**
   * Method description
   *
   *
   * @param testMail
   */
  public void setTestMail(String testMail)
  {
    this.testMail = testMail;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int adminPageSize;

  /** Field description */
  private boolean allowBlogCreation;

  /** Field description */
  private boolean allowRegistration;

  /** Field description */
  private boolean cleanupCode;

  /** Field description */
  private Long defaultBlog;

  /** Field description */
  private String domain;

  /** Field description */
  private int passwordMinLength;

  /** Field description */
  private ServiceReference<String> reference;

  /** Field description */
  private boolean registerAcknowledgement;

  /** Field description */
  private String smtpPassword;

  /** Field description */
  private int smtpPort;

  /** Field description */
  private String smtpServer;

  /** Field description */
  private String smtpUsername;

  /** Field description */
  private String spamInputMethod;

  /** Field description */
  private ServiceReference<SpamInputProtection> spamInputServcieReference;

  /** Field description */
  private int sso;

  /** Field description */
  private Boolean startTls;

  /** Field description */
  private String testMail;
}
