/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webint;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.webint.flickr.FlickrMacro;

import sonia.macro.MacroParser;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;

/**
 *
 * @author sdorra
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
    MacroParser parser = MacroParser.getInstance();

    parser.putMacro(GoogleGadgetMacro.NAME, GoogleGadgetMacro.class);
    parser.putMacro(FlickrMacro.NAME, FlickrMacro.class);
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void stop(PluginContext context)
  {
    MacroParser parser = MacroParser.getInstance();

    parser.removeMacro(GoogleGadgetMacro.NAME);
    parser.removeMacro(FlickrMacro.NAME);
  }
}
