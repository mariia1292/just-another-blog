/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.macro.def.DefaultMacroParser;

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
              new DefaultMacroParser());
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
  public void putMacro(String name, Macro macro)
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
   * @param name
   *
   * @return
   */
  public Macro getMacro(String name)
  {
    return macros.get(name);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Map<String, Macro> macros = new HashMap<String, Macro>();
}
