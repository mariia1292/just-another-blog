/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.script;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.app.InstallationListener;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.api.navigation.NavigationProvider;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;
import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class PluginActivator implements Activator, InstallationListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(PluginActivator.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param ctx
   */
  public void afterInstallation(BlogContext ctx)
  {
    init();
  }

  /**
   * Method description
   *
   *
   * @param ctx
   */
  public void beforeInstallation(BlogContext ctx)
  {

    // do nothing
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void start(PluginContext context)
  {
    if (BlogContext.getInstance().isInstalled())
    {
      init();
    }
    else
    {
      ServiceReference<InstallationListener> reference =
        BlogContext.getInstance().getServiceRegistry().get(
            InstallationListener.class, Constants.SERVICE_INSTALLATIONLISTENER);

      if (reference != null)
      {
        reference.add(this);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void stop(PluginContext context)
  {
    mappingHandler.remove(ScriptMapping.class);

    if (navigationProvider != null)
    {
      navigationProviderReference.remove(navigationProvider);
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   */
  private void copySample(InputStream in)
  {
    File file = ScriptManager.getInstance().createScriptFile();
    FileOutputStream fos = null;

    try
    {
      fos = new FileOutputStream(file);
      Util.copy(in, fos);
    }
    catch (IOException ex)
    {
      logger.log(Level.WARNING, null, ex);
    }
    finally
    {
      try
      {
        if (fos != null)
        {
          fos.close();
        }

        if (in != null)
        {
          in.close();
        }
      }
      catch (IOException ex)
      {
        logger.log(Level.WARNING, null, ex);
      }
    }
  }

  /**
   * Method description
   *
   */
  private void init()
  {
    File main = BlogContext.getInstance().getResourceManager().getDirectory(
                    ScriptConstants.DIRECTORY, false);

    if (!main.exists())
    {
      install(main);
    }

    // register mapping
    mappingHandler.add(ScriptMapping.class);

    // register navigation entry
    navigationProvider = new ScriptNavigationProvider();
    navigationProviderReference.add(navigationProvider);
  }

  /**
   * Method description
   *
   *
   * @param main
   */
  private void install(File main)
  {
    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer log = new StringBuffer();

      log.append("install ScriptingPlugin at ").append(main.getPath());
      logger.info(log.toString());
    }

    if (main.mkdir())
    {
      installSamples();
    }
    else if (logger.isLoggable(Level.WARNING))
    {
      StringBuffer log = new StringBuffer();

      log.append("could not create directory ").append(main.getPath());
      logger.warning(log.toString());
    }
  }

  /**
   * Method description
   *
   */
  private void installSamples()
  {
    for (String sample : ScriptConstants.SAMPLES)
    {
      InputStream in = Util.findResource(sample);

      if (in != null)
      {
        copySample(in);
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private MappingHandler mappingHandler;

  /** Field description */
  private ScriptNavigationProvider navigationProvider;

  /** Field description */
  @Service(Constants.NAVIGATION_GLOBALADMIN)
  private ServiceReference<NavigationProvider> navigationProviderReference;
}
