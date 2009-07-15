/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
 */


package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.macro.LinkResource;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.authentication.CookieLoginModule;
import sonia.blog.authentication.DefaultLoginModule;
import sonia.blog.mapping.DefaultMappingHandler;
import sonia.blog.util.BlogUtil;

import sonia.config.Config;
import sonia.config.ConfigInjector;
import sonia.config.ModifyableConfigurationMBean;

import sonia.injection.InjectionProvider;
import sonia.injection.ObjectInjector;

import sonia.jobqueue.JobQueueMBean;

import sonia.macro.MacroParser;

import sonia.net.FileNameMap;

import sonia.plugin.DefaultPluginStore;
import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceInjector;
import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import java.lang.management.ManagementFactory;

import java.net.URLConnection;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import javax.security.auth.login.AppConfigurationEntry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 * @author Sebastian Sdorra
 */
public class BlogContextListener implements ServletContextListener
{

  /** Field description */
  private static final String DBPROFILE_DERBY =
    "/sonia/blog/dao/jpa/profile/derby-profile.properties";

  /** Field description */
  private static final String DBPROFILE_DERBYEMBEDDED =
    "/sonia/blog/dao/jpa/profile/derby-embedded-profile.properties";

  /** Field description */
  private static final String DBPROFILE_MYSQL =
    "/sonia/blog/dao/jpa/profile/mysql-profile.properties";

  /** Field description */
  private static final String DBPROFILE_ORACLE =
    "/sonia/blog/dao/jpa/profile/oracle-profile.properties";

  /** Field description */
  private static final String DBPROFILE_POSTGRESQL =
    "/sonia/blog/dao/jpa/profile/postgresql-profile.properties";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(BlogContextListener.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public BlogContextListener() {}

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param event
   */
  public void contextDestroyed(ServletContextEvent event)
  {
    BlogContext context = BlogContext.getInstance();
    List<ServletContextListener> listeners = getPluginListeners(context);

    if (listeners != null)
    {
      for (ServletContextListener listener : listeners)
      {
        listener.contextDestroyed(event);
      }
    }

    context.destroy();
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void contextInitialized(ServletContextEvent event)
  {
    try
    {
      BlogContext context = BlogContext.getInstance();

      context.setServletContext(event.getServletContext());
      BlogUtil.configureLogger(context);
      initFileNameMap();
      initMBeans(context);
      initServices(context);
      registerResources(context);

      if (context.isInstalled())
      {
        BlogContext.getDAOFactory().init();

        File pluginStore = context.getResourceManager().getDirectory(
                               Constants.RESOURCE_PLUGINSTORE);

        if (!pluginStore.exists())
        {
          pluginStore.mkdirs();
        }

        context.getPluginContext().setStore(
            new DefaultPluginStore(pluginStore));
      }

      initInjectionProvider(context);
      initMacros(event.getServletContext());
      context.getPluginContext().searchClasspath(
          buildClasspath(event.getServletContext()));

      List<ServletContextListener> listeners = getPluginListeners(context);

      if (listeners != null)
      {
        for (ServletContextListener listener : listeners)
        {
          listener.contextInitialized(event);
        }
      }
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);

      throw new RuntimeException(ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  private String buildClasspath(ServletContext context)
  {
    String classpath = "";
    File libFile = new File(context.getRealPath("/WEB-INF/lib"));

    if (libFile.isDirectory())
    {
      File[] children = libFile.listFiles(new FilenameFilter()
      {
        public boolean accept(File file, String name)
        {
          return name.endsWith(".jar") || name.endsWith(".zip");
        }
      });

      if ((children != null) && (children.length > 0))
      {
        for (File child : children)
        {
          classpath += child.getPath() + ":";
        }
      }
    }

    File classFile = new File(context.getRealPath("/WEB-INF/classes"));

    if (classFile.isDirectory())
    {
      classpath += classFile.getPath();
    }

    if (classpath.endsWith(":"))
    {
      classpath = classpath.substring(0, classpath.length());
    }

    return classpath;
  }

  /**
   * Method description
   *
   */
  private void initFileNameMap()
  {
    FileNameMap nameMap = new FileNameMap();

    URLConnection.setFileNameMap(nameMap);
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  private void initInjectionProvider(BlogContext context)
  {
    InjectionProvider provider = context.getInjectionProvider();

    context.getPluginContext().setInjectionProvider(provider);
    provider.registerInjector(
        Service.class, new ServiceInjector(context.getServiceRegistry()));
    provider.registerInjector(Config.class,
                              new ConfigInjector(context.getConfiguration()));
    provider.registerInjector(Context.class, new ObjectInjector(context));
    provider.registerInjector(Dao.class,
                              new ObjectInjector(BlogContext.getDAOFactory()));
    context.getMacroParser().setInjectionProvider(provider);
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  private void initMBeans(BlogContext context)
  {
    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

    try
    {
      ObjectName sessionInfoName =
        new ObjectName("sonia.blog.jmx:type=SessionInformation");
      ObjectName configName = new ObjectName("sonia.blog.jmx:type=Config");
      ObjectName queueName = new ObjectName("sonia.blog.jmx:type=JobQueue");

      if (mbs.isRegistered(sessionInfoName))
      {
        mbs.unregisterMBean(sessionInfoName);
      }

      if (mbs.isRegistered(configName))
      {
        mbs.unregisterMBean(configName);
      }

      if (mbs.isRegistered(queueName))
      {
        mbs.unregisterMBean(queueName);
      }

      if (!context.isInstalled()
          || context.getConfiguration().getBoolean(Constants.CONFIG_JMX_ENABLE,
            Boolean.TRUE))
      {
        mbs.registerMBean(context.getSessionInformation(), sessionInfoName);
        mbs.registerMBean(
            new ModifyableConfigurationMBean(context.getConfiguration()),
            configName);
        mbs.registerMBean(new JobQueueMBean(context.getJobQueue()), queueName);
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param ctx
   */
  private void initMacros(ServletContext ctx)
  {
    MacroParser parser = MacroParser.getInstance();
    String path = ctx.getRealPath("/WEB-INF/config/macro.properties");
    FileInputStream fis = null;

    try
    {
      if (logger.isLoggable(Level.INFO))
      {
        StringBuffer log = new StringBuffer();

        log.append("loading macros from ").append(path);
        logger.info(log.toString());
      }

      fis = new FileInputStream(path);
      parser.load(fis);
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (fis != null)
      {
        try
        {
          fis.close();
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  private void initServices(BlogContext context)
  {
    logger.info("loading services");

    ServiceRegistry registry = context.getPluginContext().getServiceRegistry();

    // register database profiles
    registry.register(String.class, Constants.SERVICE_DBPROFILE).add(
        DBPROFILE_DERBYEMBEDDED).add(DBPROFILE_DERBY).add(DBPROFILE_MYSQL).add(
        DBPROFILE_ORACLE).add(DBPROFILE_POSTGRESQL);

    MappingHandler mappingHandler = new DefaultMappingHandler();

    loadMappingFile(context, mappingHandler);
    registry.register(MappingHandler.class,
                      Constants.SERVICE_MAPPINGHANDLER).add(mappingHandler);

    AppConfigurationEntry authEntry =
      new AppConfigurationEntry(
          DefaultLoginModule.class.getName(),
          AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
          new HashMap<String, Object>());

    registry.register(AppConfigurationEntry.class,
                      Constants.SERVICE_AUTHENTICATION).add(authEntry);

    AppConfigurationEntry ssoAuthEntry =
      new AppConfigurationEntry(
          CookieLoginModule.class.getName(),
          AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
          new HashMap<String, Object>());

    registry.register(AppConfigurationEntry.class,
                      Constants.SERVICE_SSOAUTHENTICATION).add(ssoAuthEntry);

    // register dashboardWidgets
    registry.register(String.class, Constants.SERVICE_DASHBOARDWIDGET).add(
        "/personal/widgets/rss.xhtml").add(
        "/personal/widgets/comments.xhtml").add(
        "/personal/widgets/drafts.xhtml").add("/personal/widgets/status.xhtml");

    String path =
      context.getServletContext().getRealPath("/WEB-INF/config/services.xml");
    FileInputStream fis = null;

    try
    {
      if (logger.isLoggable(Level.INFO))
      {
        StringBuffer log = new StringBuffer();

        log.append("load services from file ").append(path);
        logger.info(log.toString());
      }

      fis = new FileInputStream(path);
      context.getServiceRegistry().load(fis);
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (fis != null)
      {
        try
        {
          fis.close();
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param mappingHandler
   */
  private void loadMappingFile(BlogContext context,
                               MappingHandler mappingHandler)
  {
    File file =
      new File(context.getServletContext().getRealPath(Constants.MAPPING_FILE));

    if (file.exists())
    {
      FileInputStream fis = null;

      try
      {
        if (logger.isLoggable(Level.INFO))
        {
          StringBuffer log = new StringBuffer();

          log.append("load mappings from ").append(file.getAbsolutePath());
          logger.info(log.toString());
        }

        fis = new FileInputStream(file);
        mappingHandler.load(fis);
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
      finally
      {
        try
        {
          fis.close();
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  private void registerResources(BlogContext context)
  {
    ServiceRegistry registry = context.getServiceRegistry();
    String ctxPath = context.getServletContext().getContextPath();
    LinkResource entryRSS = new LinkResource(0);

    entryRSS.setRel(LinkResource.REL_RSSFEED);
    entryRSS.setType(LinkResource.TYPE_RSSFEED);
    entryRSS.setTitle("{2} - entries");
    entryRSS.setHref(ctxPath + "/feed/index.rss2");

    LinkResource commentRSS = new LinkResource(1);

    commentRSS.setRel(LinkResource.REL_RSSFEED);
    commentRSS.setType(LinkResource.TYPE_RSSFEED);
    commentRSS.setTitle("{2} - comments");
    commentRSS.setHref(ctxPath + "/feed/comments.rss2");

    LinkResource opensearch = new LinkResource(2);

    opensearch.setRel(LinkResource.REL_OPENSEARCH);
    opensearch.setType(LinkResource.TYPE_OPENSEARCH);
    opensearch.setTitle("JAB - {2}");
    opensearch.setHref(ctxPath + "/opensearch.xml");

    ScriptResource jquery = new ScriptResource(10,
                              ctxPath + "/resources/jquery/jquery.min.js");

    registry.register(WebResource.class, Constants.SERVICE_WEBRESOURCE).add(
        entryRSS).add(commentRSS).add(opensearch).add(jquery);
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
  private List<ServletContextListener> getPluginListeners(BlogContext context)
  {
    ServiceReference<ServletContextListener> reference =
      context.getServiceRegistry().get(ServletContextListener.class,
                                       Constants.SERVICE_CONTEXTLISTENER);

    return reference.getAll();
  }
}