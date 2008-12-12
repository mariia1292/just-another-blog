/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.util.AbstractConfigBean;

import sonia.config.XmlConfiguration;

import sonia.plugin.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

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
    reference =
      BlogContext.getInstance().getServiceRegistry().getServiceReference(
        Constants.SERVCIE_GLOBALCONFIGPROVIDER);
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
  public boolean isAllowRegistration()
  {
    return allowRegistration;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param allowRegistration
   */
  public void setAllowResgistration(boolean allowRegistration)
  {
    this.allowRegistration = allowRegistration;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean allowBlogCreation;

  /** Field description */
  private boolean allowRegistration;

  /** Field description */
  private ServiceReference reference;
}
