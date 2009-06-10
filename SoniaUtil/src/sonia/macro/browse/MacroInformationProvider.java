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
public abstract class MacroInformationProvider
{

  /** Field description */
  private static MacroInformationProvider instance;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  protected MacroInformationProvider() {}

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static MacroInformationProvider getInstance()
  {
    if (instance == null)
    {
      instance = ServiceLocator.locateService(MacroInformationProvider.class,
              new DefaultMacroInformationProvider());
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
  public abstract MacroInformation getInformation(
          Class<? extends Macro> macroClass, Locale locale);
}
