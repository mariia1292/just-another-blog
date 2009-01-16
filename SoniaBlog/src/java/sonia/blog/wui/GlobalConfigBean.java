/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.spam.SpamInputProtection;
import sonia.blog.api.util.AbstractConfigBean;
import sonia.blog.entity.Blog;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.model.SelectItem;

/**
 *
 * @author sdorra
 */
public class GlobalConfigBean extends AbstractConfigBean
{

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
    smtpPassword = config.getEncString(Constants.CONFIG_SMTPPASSWORD);
    registerAcknowledgement =
      config.getBoolean(Constants.CONFIG_REGISTERACKNOWLEDGEMENT, false);
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
    config.setEncString(Constants.CONFIG_SMTPPASSWORD, smtpPassword);
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
  public SelectItem[] getBlogItems()
  {
    SelectItem[] items = null;
    BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();
    List<Blog> blogList = blogDAO.findAllActives();

    if ((blogList != null) &&!blogList.isEmpty())
    {
      int s = blogList.size();

      items = new SelectItem[s];

      for (int i = 0; i < s; i++)
      {
        Blog blog = blogList.get(i);
        String label = blog.getTitle() + " (" + blog.getServername() + ")";

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

  //~--- fields ---------------------------------------------------------------

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
}
