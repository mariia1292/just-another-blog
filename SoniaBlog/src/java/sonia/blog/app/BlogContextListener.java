/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.authentication.CookieLoginModule;
import sonia.blog.authentication.DefaultLoginModule;
import sonia.blog.link.DefaultLinkBuilder;
import sonia.blog.macro.AttachmentMacro;
import sonia.blog.macro.BlogsMacro;
import sonia.blog.macro.FLVMacro;
import sonia.blog.macro.GalleryMacro;
import sonia.blog.macro.HelloWorldMacro;
import sonia.blog.macro.SpoilerMacro;
import sonia.blog.mapping.AttachmentMappingEntry;
import sonia.blog.mapping.CategoryMappingEntry;
import sonia.blog.mapping.DateMappingEntry;
import sonia.blog.mapping.DefaultMappingHandler;
import sonia.blog.mapping.FeedMappingEntry;
import sonia.blog.mapping.ListMappingEntry;
import sonia.blog.mapping.OpenSearchMappingEntry;
import sonia.blog.mapping.RandomMappingEntry;
import sonia.blog.mapping.ResourceMappingEntry;
import sonia.blog.mapping.SearchMappingEntry;
import sonia.blog.mapping.TagMappingEntry;
import sonia.blog.search.DefaultSearchContext;
import sonia.blog.search.IndexListener;
import sonia.blog.spam.CaptchaSpamProtection;
import sonia.blog.spam.MathSpamProtection;

import sonia.macro.MacroParser;

import sonia.net.FileNameMap;

import sonia.plugin.DefaultPluginStore;
import sonia.plugin.ServiceReference;
import sonia.plugin.ServiceRegistry;

import sonia.security.cipher.DefaultCipher;
import sonia.security.encryption.MD5Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import java.net.URLConnection;

import java.util.HashMap;
import java.util.List;
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
      initServices(context);
      initMacros();
      configureLogger();

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
    Logger logger = Logger.getLogger("sonia.blog");

    // logger.setUseParentHandlers(false);
    // logger.addHandler(new LoggingHandler());
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
   */
  private void initMacros()
  {
    MacroParser parser = MacroParser.getInstance();

    parser.putMacro("hello", new HelloWorldMacro());
    parser.putMacro("gallery", new GalleryMacro());
    parser.putMacro("spoiler", new SpoilerMacro());
    parser.putMacro("blogs", new BlogsMacro());
    parser.putMacro("flvviewer", new FLVMacro());

    AttachmentMacro am = new AttachmentMacro();

    parser.putMacro("attachments", am);
    parser.putMacro("attachment", am);
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

    mappingHandler.addMappging("/list", new ListMappingEntry());
    mappingHandler.addMappging("/tags", new TagMappingEntry());
    mappingHandler.addMappging("/categories", new CategoryMappingEntry());
    mappingHandler.addMappging("/random.jab", new RandomMappingEntry());
    mappingHandler.addMappging("/attachment", new AttachmentMappingEntry());
    mappingHandler.addMappging("/resource/", new ResourceMappingEntry());
    mappingHandler.addMappging("/feed", new FeedMappingEntry());
    mappingHandler.addMappging("/search.jab", new SearchMappingEntry());
    mappingHandler.addMappging("/opensearch.xml", new OpenSearchMappingEntry());
    mappingHandler.addMappging("/date", new DateMappingEntry());
    registry.registerService(
        Constants.SERVICE_MAPPINGHANDLER).addImplementation(mappingHandler);
    registry.registerService(Constants.SERVICE_CONTEXTLISTENER);
    registry.registerService(Constants.SERVCIE_ENCRYPTION).addImplementation(
        new MD5Encryption());
    registry.registerService(Constants.SERVCIE_CIPHER).addImplementation(
        new DefaultCipher());

    AppConfigurationEntry authEntry =
      new AppConfigurationEntry(
          DefaultLoginModule.class.getName(),
          AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
          new HashMap<String, Object>());

    registry.registerService(
        Constants.SERVICE_AUTHENTICATION).addImplementation(authEntry);

    AppConfigurationEntry ssoAuthEntry =
      new AppConfigurationEntry(
          CookieLoginModule.class.getName(),
          AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
          new HashMap<String, Object>());

    registry.registerService(
        Constants.SERVICE_SSOAUTHENTICATION).addImplementation(ssoAuthEntry);
    registry.registerService(Constants.SERVICE_BLOG_LISTENER);
    registry.registerService(
        Constants.SERVICE_ENTRY_LISTENER).addImplementation(
        new IndexListener());
    registry.registerService(Constants.SERVICE_COMMENT_LISTENER);
    registry.registerService(Constants.SERVICE_ATTACHMENT_LISTENER);
    registry.registerService(Constants.SERVICE_SEARCHCONTEXT).addImplementation(
        new DefaultSearchContext());
    registry.registerService(Constants.SERVICE_LINKBUILDER).addImplementation(
        new DefaultLinkBuilder());
    registry.registerService(Constants.SERVCIE_GLOBALCONFIGPROVIDER);
    registry.registerService(Constants.SERVCIE_GLOBALSTATUSROVIDER);
    registry.registerService(
        Constants.SERVICE_SPAMPROTECTIONMETHOD).addImplementation(
        new MathSpamProtection()).addImplementation(
        new CaptchaSpamProtection());

    // register NavigationProvider
    registry.registerService(Constants.NAVIGATION_EXTRA);
    registry.registerService(Constants.NAVIGATION_READER);
    registry.registerService(Constants.NAVIGATION_AUTHOR);
    registry.registerService(Constants.NAVIGATION_ADMIN);
    registry.registerService(Constants.NAVIGATION_GLOBALADMIN);

    // register dashboardWidgets
    registry.registerService(
        Constants.SERVICE_DASHBOARDWIDGET).addImplementation(
        "/personal/widgets/rss.xhtml").addImplementation(
        "/personal/widgets/comments.xhtml").addImplementation(
        "/personal/widgets/drafts.xhtml").addImplementation(
        "/personal/widgets/status.xhtml");
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
  @SuppressWarnings("unchecked")
  private List<ServletContextListener> getPluginListeners(BlogContext context)
  {
    ServiceReference reference =
      context.getServiceRegistry().getServiceReference(
          Constants.SERVICE_CONTEXTLISTENER);

    return reference.getImplementations();
  }
}
