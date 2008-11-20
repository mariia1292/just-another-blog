/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.template.TemplateManager;
import sonia.blog.entity.Blog;

import sonia.config.XmlConfiguration;

import sonia.macro.MacroParser;

import sonia.plugin.PluginContext;
import sonia.plugin.ServiceReference;
import sonia.plugin.ServiceRegistry;

import sonia.security.authentication.LoginCallbackHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
    this.pluginContext = new PluginContext();
  }

  //~--- get methods ----------------------------------------------------------

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
      loginConfiguration = new DefaultLoginConfiguration();
    }

    LoginCallbackHandler callbackHandler = new LoginCallbackHandler(username,
                                             password);

    return new LoginContext(Constants.LOGINMODULE_NAME, new Subject(),
                            callbackHandler, loginConfiguration);
  }

  /**
   * Method description
   *
   */
  public void destroy()
  {
    getPluginContext().shutdown();

    if (entityManagerFactory != null)
    {
      entityManagerFactory.close();
    }

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
  public XmlConfiguration getConfiguration()
  {
    if (configuration == null)
    {
      configuration = new XmlConfiguration();

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
    Long id = configuration.getLong("defaultBlog");

    if (id != null)
    {
      blog = getEntityManager().find(Blog.class, id);
    }

    return blog;
  }

  /**
   * Method description
   *
   *
   *
   * @param recreate
   * @return
   */
  public EntityManager getEntityManager(boolean recreate)
  {
    if (recreate || (entityManagerFactory == null))
    {
      if (entityManagerFactory != null)
      {
        try
        {
          entityManagerFactory.close();
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }

      XmlConfiguration config = getConfiguration();
      Map<String, String> parameters = new HashMap<String, String>();

      parameters.put("toplink.jdbc.driver", config.getString("db.driver"));
      parameters.put("toplink.jdbc.url", config.getString("db.url"));
      parameters.put("toplink.jdbc.user", config.getString("db.username"));
      parameters.put("toplink.jdbc.password", config.getString("db.password"));
      entityManagerFactory =
        Persistence.createEntityManagerFactory("SoniaBlog-PU", parameters);
    }

    return entityManagerFactory.createEntityManager();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public EntityManager getEntityManager()
  {
    return getEntityManager(false);
  }

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
      linkBuilder =
        getServiceRegistry().getServiceReference(Constants.SERVICE_LINKBUILDER);
    }

    return (LinkBuilder) linkBuilder.getImplementation();
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
  public File getResourceDirectory()
  {
    if (resourceDirectory == null)
    {
      XmlConfiguration config = getConfiguration();
      String resourcePath = config.getString("resource.directory");

      if (resourcePath == null)
      {
        resourcePath = getServletContext().getRealPath("WEB-INF/resources");
      }

      resourceDirectory = new File(resourcePath);
    }

    return resourceDirectory;
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
      searchContext = getServiceRegistry().getServiceReference(
        Constants.SERVICE_SEARCHCONTEXT);
    }

    return (SearchContext) searchContext.getImplementation();
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
  private XmlConfiguration configuration;

  /** Field description */
  private EntityManagerFactory entityManagerFactory;

  /** Field description */
  private boolean installed = false;

  /** Field description */
  private ServiceReference linkBuilder;

  /** Field description */
  private Logger logger = Logger.getLogger(BlogContext.class.getName());

  /** Field description */
  private Configuration loginConfiguration;

  /** Field description */
  private PluginContext pluginContext;

  /** Field description */
  private File resourceDirectory;

  /** Field description */
  private ServiceReference searchContext;

  /** Field description */
  private ServletContext servletContext;

  /** Field description */
  private TemplateManager templateManager;
}
