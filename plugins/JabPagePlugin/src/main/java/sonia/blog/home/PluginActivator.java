/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.home;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Context;

import sonia.macro.MacroParser;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class PluginActivator implements Activator
{

  /**
   * Method description
   *
   *
   * @param context
   */
  public void start(PluginContext context)
  {
    macroParser.putMacro(PluginsMacro.NAME, PluginsMacro.class);
    macroParser.putMacro(MacrosMacro.NAME, MacrosMacro.class);
    macroParser.putMacro(MacroInfoMacro.NAME, MacroInfoMacro.class);
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void stop(PluginContext context)
  {
    macroParser.removeMacroFactory(PluginsMacro.NAME);
    macroParser.removeMacroFactory(MacrosMacro.NAME);
    macroParser.removeMacroFactory(MacroInfoMacro.NAME);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private MacroParser macroParser;
}
