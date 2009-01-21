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
    if (googleGadgetMacro == null)
    {
      googleGadgetMacro = new GoogleGadgetMacro();
    }

    if (flickrMacro == null)
    {
      flickrMacro = new FlickrMacro();
    }

    MacroParser parser = MacroParser.getInstance();

    parser.putMacro(GoogleGadgetMacro.NAME, googleGadgetMacro);
    parser.putMacro(FlickrMacro.NAME, flickrMacro);
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private FlickrMacro flickrMacro;

  /** Field description */
  private GoogleGadgetMacro googleGadgetMacro;
}
