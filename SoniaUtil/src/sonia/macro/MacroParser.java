/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.injection.InjectionProvider;

import sonia.util.ServiceLocator;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sdorra
 */
public abstract class MacroParser
{

  /** Field description */
  private static MacroParser instance;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static MacroParser getInstance()
  {
    if (instance == null)
    {
      instance = ServiceLocator.locateService(MacroParser.class,
              new RegexMacroParser());
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param environment
   * @param text
   *
   * @return
   */
  public abstract String parseText(Map<String, ?> environment, String text);

  /**
   * Method description
   *
   *
   * @param name
   * @param macro
   */
  public void putMacro(String name, Class<? extends Macro> macro)
  {
    macros.put(name, macro);
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void removeMacro(String name)
  {
    macros.remove(name);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public InjectionProvider getInjectionProvider()
  {
    return injectionProvider;
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public Class<? extends Macro> getMacro(String name)
  {
    return macros.get(name);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param injectionProvider
   */
  public void setInjectionProvider(InjectionProvider injectionProvider)
  {
    this.injectionProvider = injectionProvider;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected InjectionProvider injectionProvider;

  /** Field description */
  protected Map<String, Class<? extends Macro>> macros =
    new HashMap<String, Class<? extends Macro>>();
}
