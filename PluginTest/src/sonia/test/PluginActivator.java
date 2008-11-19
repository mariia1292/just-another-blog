/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.test;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;
import sonia.plugin.ServiceReference;

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

    ServiceReference reference = getReference(context);

    if (reference != null)
    {
      servlet = new TestServlet();
      System.out.println("add Implementation");
      reference.addImplementation(servlet);
    }
    else
    {
      System.out.println("failed to add implementation");
    }

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

    ServiceReference reference = getReference(context);

    if ((reference != null) && (servlet != null))
    {
      reference.getImplementations().remove(servlet);
    }

    BlogContext.getInstance().getMacroParser().removeMacro("hello");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  private ServiceReference getReference(
          PluginContext context)
  {
    return context.getServiceRegistry().getServiceReference("/servlet");
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private TestServlet servlet;
}
