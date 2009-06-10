/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.macro.Macro;

import sonia.util.ServiceLocator;

//~--- JDK imports ------------------------------------------------------------

import java.util.Locale;

/**
 *
 * @author sdorra
 */
public abstract class MetaInformationProvider
{

  /** Field description */
  private static MetaInformationProvider instance;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  protected MetaInformationProvider() {}

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static MetaInformationProvider getInstance()
  {
    if (instance == null)
    {
      instance = ServiceLocator.locateService(MetaInformationProvider.class,
              new DefaultMetaInformationProvider());
    }

    return instance;
  }

  /**
   * Method description
   *
   *
   * @param macroClass
   * @param locale
   *
   * @return
   */
  public abstract MetaInformation getInformation(
          Class<? extends Macro> macroClass, Locale locale);
}
