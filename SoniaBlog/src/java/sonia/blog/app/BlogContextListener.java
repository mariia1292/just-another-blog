/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequestListener;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.dao.DAOListener;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.editor.AttachmentHandler;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.api.navigation.NavigationProvider;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.spam.SpamCheck;
import sonia.blog.api.spam.SpamInputProtection;
import sonia.blog.authentication.CookieLoginModule;
import sonia.blog.authentication.DefaultLoginModule;
import sonia.blog.dao.jpa.JpaDAOFactory;
import sonia.blog.editor.FlowPlayerHandler;
import sonia.blog.editor.LinkHandler;
import sonia.blog.link.DefaultLinkBuilder;
import sonia.blog.macro.AttachmentMacro;
import sonia.blog.macro.BlogsMacro;
import sonia.blog.macro.FLVMacro;
import sonia.blog.macro.GalleryMacro;
import sonia.blog.macro.HelloWorldMacro;
import sonia.blog.macro.SpoilerMacro;
import sonia.blog.mapping.DefaultMappingHandler;
import sonia.blog.search.DefaultSearchContext;
import sonia.blog.search.IndexListener;
import sonia.blog.spam.BlacklistSpamCheck;
import sonia.blog.spam.CaptchaSpamProtection;
import sonia.blog.spam.MathSpamProtection;

import sonia.config.Config;
import sonia.config.ConfigInjector;

import sonia.injection.InjectionProvider;
import sonia.injection.ObjectInjector;

import sonia.logging.SimpleFormatter;

import sonia.macro.MacroParser;

import sonia.net.FileNameMap;

import sonia.plugin.DefaultPluginStore;
import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceInjector;
import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

import sonia.security.cipher.Cipher;
import sonia.security.cipher.DefaultCipher;
import sonia.security.encryption.Encryption;
import sonia.security.encryption.MD5Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import java.net.URLConnection;

import java.util.HashMap;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.AppConfigurationEntry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 * @author sdorra
 */
public class BlogContextListener implements ServletContextListener
{

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
      initFileNameMap();
      configureLogger();
      initServices(context);

      if (context.isInstalled())
      {
        BlogContext.getDAOFactory().init();
      }

      initInjectionProvider(context);
      initMacros();

      File pluginStore = context.getResourceManager().getDirectory(
                             Constants.RESOURCE_PLUGINSTORE);

      if (!pluginStore.exists())
      {
        pluginStore.mkdirs();
      }

      context.getPluginContext().setStore(new DefaultPluginStore(pluginStore));
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

      // context.getJobQueue().start();
    }
    catch (IOException ex)
    {
      ex.printStackTrace(System.err);

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
   *
   * @throws IOException
   */
  private void configureLogger() throws IOException
  {
    SimpleFormatter formatter = new SimpleFormatter();
    Logger logger = Logger.getLogger("sonia");

    logger.setUseParentHandlers(false);

    ConsoleHandler handler = new ConsoleHandler();

    handler.setFormatter(formatter);
    handler.setLevel(Level.FINEST);
    logger.addHandler(handler);
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
   */
  private void initMacros()
  {
    MacroParser parser = MacroParser.getInstance();

    parser.putMacro("hello", HelloWorldMacro.class);
    parser.putMacro("gallery", GalleryMacro.class);
    parser.putMacro("spoiler", SpoilerMacro.class);
    parser.putMacro("blogs", BlogsMacro.class);
    parser.putMacro("flvviewer", FLVMacro.class);
    parser.putMacro("attachments", AttachmentMacro.class);
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  private void initServices(BlogContext context)
  {
    ServiceRegistry registry = context.getPluginContext().getServiceRegistry();
    MappingHandler mappingHandler = new DefaultMappingHandler();

    loadMappingFile(context, mappingHandler);
    registry.register(DAOFactory.class,
                      Constants.SERVCIE_DAO).add(new JpaDAOFactory());
    registry.register(MappingHandler.class,
                      Constants.SERVICE_MAPPINGHANDLER).add(mappingHandler);
    registry.register(ServletContextListener.class,
                      Constants.SERVICE_CONTEXTLISTENER);
    registry.register(Encryption.class,
                      Constants.SERVCIE_ENCRYPTION).add(new MD5Encryption());
    registry.register(Cipher.class,
                      Constants.SERVCIE_CIPHER).add(new DefaultCipher());

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
    registry.register(SearchContext.class, Constants.SERVICE_SEARCHCONTEXT).add(
        new DefaultSearchContext());
    registry.register(LinkBuilder.class, Constants.SERVICE_LINKBUILDER).add(
        new DefaultLinkBuilder());
    registry.register(String.class, Constants.SERVCIE_GLOBALCONFIGPROVIDER);
    registry.register(String.class, Constants.SERVCIE_GLOBALSTATUSROVIDER);

    // spam protection
    registry.register(
        SpamInputProtection.class, Constants.SERVICE_SPAMPROTECTIONMETHOD).add(
        new MathSpamProtection()).add(new CaptchaSpamProtection());
    registry.register(SpamCheck.class, Constants.SERVICE_SPAMCHECK).add(
        new BlacklistSpamCheck());

    // register Listeners
    registry.register(DAOListener.class, Constants.LISTENER_ATTACHMENT);
    registry.register(DAOListener.class, Constants.LISTENER_BLOG);
    registry.register(DAOListener.class, Constants.LISTENER_CATEGORY);
    registry.register(DAOListener.class, Constants.LISTENER_COMMENT);
    registry.register(DAOListener.class,
                      Constants.LISTENER_ENTRY).add(new IndexListener());
    registry.register(DAOListener.class, Constants.LISTENER_MEMBER);
    registry.register(DAOListener.class, Constants.LISTENER_TAG);
    registry.register(DAOListener.class, Constants.LISTENER_USER);
    registry.register(BlogRequestListener.class,
                      Constants.SERVICE_REQUESTLISTENER);

    // register NavigationProvider
    registry.register(NavigationProvider.class, Constants.NAVIGATION_EXTRA);
    registry.register(NavigationProvider.class, Constants.NAVIGATION_READER);
    registry.register(NavigationProvider.class, Constants.NAVIGATION_AUTHOR);
    registry.register(NavigationProvider.class, Constants.NAVIGATION_ADMIN);
    registry.register(NavigationProvider.class,
                      Constants.NAVIGATION_GLOBALADMIN);
    registry.register(NavigationProvider.class,
                      Constants.NAVIGATION_BLOGACTION);

    // register dashboardWidgets
    registry.register(String.class, Constants.SERVICE_DASHBOARDWIDGET).add(
        "/personal/widgets/rss.xhtml").add(
        "/personal/widgets/comments.xhtml").add(
        "/personal/widgets/drafts.xhtml").add("/personal/widgets/status.xhtml");
    registry.register(
        AttachmentHandler.class, Constants.SERVICE_ATTACHMENTHANDLER).add(
        new LinkHandler()).add(new FlowPlayerHandler());
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
        fis = new FileInputStream(file);
        mappingHandler.load(fis);
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
      finally
      {
        try
        {
          fis.close();
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }
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
