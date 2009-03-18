/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.authentication.SSOCallbackHandler;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.dao.cache.CacheManager;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.template.TemplateManager;
import sonia.blog.entity.Blog;

import sonia.injection.DefaultInjectionProvider;
import sonia.injection.InjectionProvider;

import sonia.jobqueue.JobQueue;

import sonia.macro.MacroParser;

import sonia.plugin.PluginContext;
import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

import sonia.security.authentication.LoginCallbackHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import javax.servlet.ServletContext;

/**
 *
 * @author sdorra
 */
public class BlogContext
{

  /** Field description */
  public static final String CONFIGFILE_PARAMETER = "sonia.config.ConfigFile";

  /** Field description */
  private static BlogContext instance;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public BlogContext()
  {
    this.injectionProvider = new DefaultInjectionProvider();
    this.pluginContext = new PluginContext();
    this.jobQueue = new JobQueue<BlogJob>();
    this.jobQueue.start();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static DAOFactory getDAOFactory()
  {
    return DAOFactory.getInstance();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public static BlogContext getInstance()
  {
    if (instance == null)
    {
      instance = new BlogContext();
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param username
   * @param password
   *
   * @return
   *
   * @throws LoginException
   */
  public LoginContext buildLoginContext(String username, char[] password)
          throws LoginException
  {
    if (loginConfiguration == null)
    {
      loginConfiguration =
        new DefaultLoginConfiguration(Constants.SERVICE_AUTHENTICATION);
    }

    LoginCallbackHandler callbackHandler = new LoginCallbackHandler(username,
                                             password);

    return new LoginContext(Constants.LOGINMODULE_NAME, new Subject(),
                            callbackHandler, loginConfiguration);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @return
   *
   * @throws LoginException
   */
  public LoginContext buildSSOLoginContext(BlogRequest request,
          BlogResponse response)
          throws LoginException
  {
    if (ssoLoginConfiguration == null)
    {
      ssoLoginConfiguration =
        new DefaultLoginConfiguration(Constants.SERVICE_SSOAUTHENTICATION);
    }

    SSOCallbackHandler callbackHandler = new SSOCallbackHandler(request,
                                           response);

    return new LoginContext(Constants.SSOLOGINMODULE_NAME, new Subject(),
                            callbackHandler, ssoLoginConfiguration);
  }

  /**
   * Method description
   * TODO: replace with DAOFactory.destroy
   */
  public void destroy()
  {
    getJobQueue().stop();
    getPluginContext().shutdown();
    getDAOFactory().close();

    if (installed &&!configuration.isEmpty())
    {
      try
      {
        configuration.store(new FileOutputStream(getConfigFile()));
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public CacheManager getCacheManager()
  {
    return getDAOFactory().getCacheManager();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public File getConfigFile()
  {
    String path = getServletContext().getRealPath(
                      getServletContext().getInitParameter(
                        CONFIGFILE_PARAMETER));

    return new File(path);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public BlogConfiguration getConfiguration()
  {
    if (configuration == null)
    {
      configuration = new BlogConfiguration();

      File config = getConfigFile();

      if (config.isFile())
      {
        try
        {
          configuration.load(new FileInputStream(config));
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }

    return configuration;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getDefaultBlog()
  {
    Blog blog = null;
    Long id = configuration.getLong(Constants.CONFIG_DEFAULTBLOG);

    if (id != null)
    {
      blog = getDAOFactory().getBlogDAO().get(id);
    }

    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public InjectionProvider getInjectionProvider()
  {
    return injectionProvider;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public JobQueue<BlogJob> getJobQueue()
  {
    return jobQueue;
  }

  /**
   * Method description
   * TODO: remove
   *
   *
   * @return
   *
   * public EntityManager getEntityManager(boolean recreate)
   * {
   * if (recreate || (entityManagerFactory == null))
   * {
   *   if (entityManagerFactory != null)
   *   {
   *     try
   *     {
   *       entityManagerFactory.close();
   *     }
   *     catch (Exception ex)
   *     {
   *       logger.log(Level.SEVERE, null, ex);
   *     }
   *   }
   *
   *   XmlConfiguration config = getConfiguration();
   *   Map<String, String> parameters = new HashMap<String, String>();
   *
   *   parameters.put("toplink.jdbc.driver",
   *                  config.getString(Constants.CONFIG_DB_DRIVER));
   *   parameters.put("toplink.jdbc.url",
   *                  config.getString(Constants.CONFIG_DB_URL));
   *   parameters.put("toplink.jdbc.user",
   *                  config.getString(Constants.CONFIG_DB_USERNAME));
   *   parameters.put("toplink.jdbc.password",
   *                  config.getString(Constants.CONFIG_DB_PASSWORD));
   *   entityManagerFactory =
   *     Persistence.createEntityManagerFactory("SoniaBlog-PU", parameters);
   * }
   *
   * return entityManagerFactory.createEntityManager();
   * }
   */

  /**
   * Method description
   * TODO: remove
   *
   * @return
   *
   * public EntityManager getEntityManager()
   * {
   * return getEntityManager(false);
   * }
   */

  /**
   * Method description
   *
   *
   * @return
   */
  public LinkBuilder getLinkBuilder()
  {
    if (linkBuilder == null)
    {
      linkBuilder = getServiceRegistry().get(LinkBuilder.class,
              Constants.SERVICE_LINKBUILDER);
    }

    return linkBuilder.get();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public MacroParser getMacroParser()
  {
    return MacroParser.getInstance();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingHandler getMappingHandler()
  {
    if (mappingHandler == null)
    {
      mappingHandler = getServiceRegistry().get(MappingHandler.class,
              Constants.SERVICE_MAPPINGHANDLER);
    }

    return mappingHandler.get();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public PluginContext getPluginContext()
  {
    return pluginContext;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ResourceManager getResourceManager()
  {
    if (resourceManager == null)
    {
      BlogConfiguration config = getConfiguration();
      String resourcePath =
        config.getString(Constants.CONFIG_RESOURCE_DIRECTORY);

      if (resourcePath == null)
      {
        resourcePath = getServletContext().getRealPath("WEB-INF/resources");
      }

      resourceManager = new ResourceManager(new File(resourcePath));
    }

    return resourceManager;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SearchContext getSearchContext()
  {
    if (searchContext == null)
    {
      searchContext = getServiceRegistry().get(SearchContext.class,
              Constants.SERVICE_SEARCHCONTEXT);
    }

    return searchContext.get();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ServiceRegistry getServiceRegistry()
  {
    return pluginContext.getServiceRegistry();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ServletContext getServletContext()
  {
    return servletContext;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public TemplateManager getTemplateManager()
  {
    if (templateManager == null)
    {
      templateManager = new TemplateManager();
    }

    return templateManager;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isInstalled()
  {
    if (!installed)
    {
      installed = getConfigFile().isFile();
    }

    return installed;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param servletContext
   */
  public void setServletContext(ServletContext servletContext)
  {
    this.servletContext = servletContext;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private BlogConfiguration configuration;

  /** Field description */
  private InjectionProvider injectionProvider;

  /** Field description */
  private boolean installed = false;

  /** Field description */
  private JobQueue<BlogJob> jobQueue;

  /** Field description */
  private ServiceReference<LinkBuilder> linkBuilder;

  /** Field description */
  private Logger logger = Logger.getLogger(BlogContext.class.getName());

  /** Field description */
  private Configuration loginConfiguration;

  /** Field description */
  private ServiceReference<MappingHandler> mappingHandler;

  /** Field description */
  private PluginContext pluginContext;

  /** Field description */
  private ResourceManager resourceManager;

  /** Field description */
  private ServiceReference<SearchContext> searchContext;

  /** Field description */
  private ServletContext servletContext;

  /** Field description */
  private Configuration ssoLoginConfiguration;

  /** Field description */
  private TemplateManager templateManager;
}
