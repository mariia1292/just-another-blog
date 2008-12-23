/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.spam.SpamInputProtection;
import sonia.blog.api.util.AbstractConfigBean;

import sonia.config.XmlConfiguration;

import sonia.plugin.ServiceReference;

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

    reference = context.getServiceRegistry().getServiceReference(
      Constants.SERVCIE_GLOBALCONFIGPROVIDER);
    spamInputServcieReference =
      context.getServiceRegistry().getServiceReference(
        Constants.SERVICE_SPAMPROTECTIONMETHOD);

    if (spamInputMethod == null)
    {
      spamInputMethod =
        spamInputServcieReference.getImplementation().getClass().getName();
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
  public void load(XmlConfiguration config)
  {
    allowRegistration = config.getBoolean(Constants.CONFIG_ALLOW_REGISTRATION,
            Boolean.FALSE);
    allowBlogCreation = config.getBoolean(Constants.CONFIG_ALLOW_BLOGCREATION,
            Boolean.FALSE);
    passwordMinLength = config.getInteger(Constants.CONFIG_PASSWORD_MINLENGTH,
            Constants.DEFAULT_PASSWORD_MINLENGTH);
    spamInputMethod = config.getString(Constants.CONFIG_SPAMMETHOD);
  }

  /**
   * Method description
   *
   *
   * @param config
   */
  @Override
  public void store(XmlConfiguration config)
  {
    config.set(Constants.CONFIG_ALLOW_REGISTRATION, allowRegistration);
    config.set(Constants.CONFIG_ALLOW_BLOGCREATION, allowBlogCreation);
    config.set(Constants.CONFIG_PASSWORD_MINLENGTH, passwordMinLength);
    config.set(Constants.CONFIG_SPAMMETHOD, spamInputMethod);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<String> getConfigurationProviders()
  {
    return reference.getImplementations();
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
  @SuppressWarnings("unchecked")
  public SelectItem[] getSpamInputMethodItems()
  {
    SelectItem[] items = null;
    List<SpamInputProtection> list =
      spamInputServcieReference.getImplementations();

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
   * @param spamInputMethod
   */
  public void setSpamInputMethod(String spamInputMethod)
  {
    this.spamInputMethod = spamInputMethod;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean allowBlogCreation;

  /** Field description */
  private boolean allowRegistration;

  /** Field description */
  private int passwordMinLength;

  /** Field description */
  private ServiceReference reference;

  /** Field description */
  private String spamInputMethod;

  /** Field description */
  private ServiceReference spamInputServcieReference;
}
