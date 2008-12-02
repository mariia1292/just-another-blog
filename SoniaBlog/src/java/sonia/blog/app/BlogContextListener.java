/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.authentication.DefaultLoginModule;
import sonia.blog.link.DefaultLinkBuilder;
import sonia.blog.macro.AttachmentMacro;
import sonia.blog.macro.BlogsMacro;
import sonia.blog.macro.GalleryMacro;
import sonia.blog.macro.HelloWorldMacro;
import sonia.blog.macro.SpoilerMacro;
import sonia.blog.mapping.AttachmentMappingEntry;
import sonia.blog.mapping.CategoryMappingEntry;
import sonia.blog.mapping.DefaultMappingHandler;
import sonia.blog.mapping.FeedMappingEntry;
import sonia.blog.mapping.ListMappingEntry;
import sonia.blog.mapping.RandomMappingEntry;
import sonia.blog.mapping.ResourceMappingEntry;
import sonia.blog.mapping.SearchMappingEntry;
import sonia.blog.mapping.TagMappingEntry;
import sonia.blog.search.DefaultSearchContext;
import sonia.blog.search.IndexListener;

import sonia.macro.MacroParser;

import sonia.plugin.Plugin;
import sonia.plugin.PluginStateListener;
import sonia.plugin.ServiceReference;
import sonia.plugin.ServiceRegistry;

import sonia.security.cipher.DefaultCipher;
import sonia.security.encryption.MD5Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
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

  /** Field description */
  private static Logger logger =
    Logger.getLogger(BlogContextListener.class.getName());

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

      if (context.isInstalled())
      {

        // configureLogger();
      }

      initServices(context);
      initMacros();
      context.getPluginContext().addStateChangeListener(new PluginListener());
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
   *
   * @throws IOException
   */
  private void configureLogger() throws IOException
  {
    String path = BlogContext.getInstance().getServletContext().getRealPath(
                      "/WEB-INF/config/logger.properties");

    LogManager.getLogManager().readConfiguration(new FileInputStream(path));
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

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 08/09/23
   * @author     Enter your name here...
   */
  private class PluginListener implements PluginStateListener
  {

    /**
     * Method description
     *
     *
     * @param oldState
     * @param newState
     * @param plugin
     */
    public void stateChanged(int oldState, int newState, Plugin plugin)
    {
      System.out.println(plugin.getName() + "(" + oldState + "," + newState
                         + ")");
    }
  }
}
