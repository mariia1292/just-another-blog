/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public abstract class AbstractConfigBean extends AbstractBean
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AbstractConfigBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public AbstractConfigBean()
  {
    super();
    this.config = BlogContext.getInstance().getConfiguration();
    load(config);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param config
   */
  public abstract void load(BlogConfiguration config);

  /**
   * Method description
   *
   *
   * @param config
   */
  public abstract void store(BlogConfiguration config);

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract boolean verify();

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;

    if (verify())
    {
      store(config);

      try
      {
        config.store();
        getMessageHandler().info("successStoreConfig");
      }
      catch (Exception ex)
      {
        StringBuffer log = new StringBuffer();

        log.append("exception in ").append(getClass().getName());
        logger.log(Level.SEVERE, log.toString(), ex);
        getMessageHandler().error("failureStoreConfig");
        result = FAILURE;
      }
    }
    else
    {
      result = FAILURE;
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private BlogConfiguration config;
}
