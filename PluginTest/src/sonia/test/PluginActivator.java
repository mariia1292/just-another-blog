/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.test;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

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
    System.out.println("start Test-Plugin");
    BlogContext.getInstance().getMacroParser().putMacro("hello",
            new TestMacro());
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void stop(PluginContext context)
  {
    System.out.println("stop Test-Plugin");
    BlogContext.getInstance().getMacroParser().removeMacro("hello");
  }
}
