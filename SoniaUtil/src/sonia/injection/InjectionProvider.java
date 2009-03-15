/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.injection;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Annotation;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public abstract class InjectionProvider
{

  /**
   * Constructs ...
   *
   */
  public InjectionProvider()
  {
    logger = Logger.getLogger(getClass().getName());
    injectorMap = new HashMap<Class<? extends Annotation>, Injector>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   */
  public abstract void inject(Object object);

  /**
   * Method description
   *
   *
   * @param annotation
   * @param injector
   */
  public void registerInjector(Class<? extends Annotation> annotation,
                               Injector injector)
  {
    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("register injector ").append(injector.getClass().getName());
      msg.append(" for ").append(annotation.getClass().getName());
      logger.finest(msg.toString());
    }

    injectorMap.put(annotation, injector);
  }

  /**
   * Method description
   *
   *
   * @param annotation
   */
  public void unregisterInjector(Class<? extends Annotation> annotation)
  {
    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("unregister Injector for ");
      msg.append(annotation.getClass().getName());
      logger.finest(msg.toString());
    }

    injectorMap.remove(annotation);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Map<Class<? extends Annotation>, Injector> injectorMap;

  /** Field description */
  protected Logger logger;
}
