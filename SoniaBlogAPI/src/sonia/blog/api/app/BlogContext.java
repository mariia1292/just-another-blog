/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.authentication.SSOCallbackHandler;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.api.search.SearchContext;
import sonia.blog.api.template.TemplateManager;
import sonia.blog.entity.Blog;
import sonia.blog.entity.User;
import sonia.blog.jmx.SessionInformation;

import sonia.config.WebVariableResolver;

import sonia.image.ImageHandler;

import sonia.injection.DefaultInjectionProvider;
import sonia.injection.InjectionProvider;

import sonia.jobqueue.JobQueue;

import sonia.macro.MacroParser;

import sonia.plugin.PluginContext;
import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

import sonia.security.KeyGenerator;
import sonia.security.authentication.LoginCallbackHandler;
import sonia.security.cipher.Cipher;
import sonia.security.cipher.DefaultCipher;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Date;
import java.util.Properties;
import java.util.Set;
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
public final class BlogContext
{

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

  /**
   * Method description
   *
   *
   * @param request
   * @param username
   * @param password
   *
   * @return
   *
   * @throws LoginException
   */
  public BlogSession login(BlogRequest request, String username,
                           char[] password)
          throws LoginException
  {
    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer log = new StringBuffer();

      log.append("login user ").append(username);
      logger.info(log.toString());
    }

    LoginContext loginContext = buildLoginContext(username, password);

    return login(loginContext, request);
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
  public BlogSession login(BlogRequest request, BlogResponse response)
          throws LoginException
  {
    LoginContext loginContext = buildSSOLoginContext(request, response);

    return login(loginContext, request);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param blogSession
   *
   * @throws LoginException
   */
  public void logout(BlogRequest request, BlogSession blogSession)
          throws LoginException
  {
    if (logger.isLoggable(Level.WARNING))
    {
      StringBuffer log = new StringBuffer();

      log.append("logout user ").append(blogSession.getUser().getName());
      logger.info(log.toString());
    }

    blogSession.getLoginContext().logout();
    blogSession = null;
    request.getSession().invalidate();
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
    if (configFile == null)
    {
      ResourceManager resManager = getResourceManager();

      if (resManager != null)
      {
        File dir = resManager.getDirectory(Constants.RESOURCE_CONFIG);

        configFile = new File(dir, "main-config.xml");
      }
    }

    return configFile;
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
      configuration.setVariableResolver(
          new WebVariableResolver(servletContext));

      File config = getConfigFile();
      String key = null;

      if ((config != null) && config.exists())
      {
        StringBuffer log = new StringBuffer();

        log.append("read config from ").append(config.getAbsolutePath());
        logger.info(log.toString());

        try
        {
          configuration.load(new FileInputStream(config));
          key = configuration.getString(Constants.CONFIG_SECUREKEY);
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }

      if (Util.isBlank(key))
      {
        key = KeyGenerator.generateKey(16);
        configuration.set(Constants.CONFIG_SECUREKEY, key);
      }

      Cipher cipher = null;
      ServiceReference<Cipher> cipherRefence =
        getServiceRegistry().get(Cipher.class, Constants.SERVCIE_CIPHER);

      if (cipherRefence != null)
      {
        cipher = cipherRefence.get();

        if (cipher == null)
        {
          cipher = new DefaultCipher();
          cipherRefence.add(cipher);
        }
      }
      else
      {
        cipher = new DefaultCipher();
      }

      cipher.setKey(key.toCharArray());
      configuration.setCipher(cipher);
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
  public ImageHandler getImageHandler()
  {
    if (imageHandler == null)
    {

      // TODO: get with service
      imageHandler = ImageHandler.getInstance();
    }

    return imageHandler;
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
    if (jobQueue == null)
    {
      Integer hc = null;

      if (isInstalled())
      {
        BlogConfiguration config = getConfiguration();

        hc = config.getInteger(Constants.CONFIG_QUEUEHANDLER);
      }

      if (hc != null)
      {
        this.jobQueue = new JobQueue<BlogJob>(hc);
      }
      else
      {
        this.jobQueue = new JobQueue<BlogJob>();
      }

      this.jobQueue.start();
    }

    return jobQueue;
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
  public MailService getMailService()
  {
    if (mailService == null)
    {
      mailService = getServiceRegistry().get(MailService.class,
              Constants.SERVICE_MAIL);
    }

    return mailService.get();
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
      String resourcePath = null;
      Properties props = new Properties();
      InputStream in = null;

      try
      {
        String path =
          getServletContext().getRealPath("/WEB-INF/base.properties");
        File baseProps = new File(path);

        if (baseProps.exists())
        {
          in = new FileInputStream(path);
          props.load(in);
          resourcePath = props.getProperty("resource.home");

          if (Util.hasContent(resourcePath))
          {
            resourceManager = new ResourceManager(new File(resourcePath));
          }
        }
      }
      catch (IOException ex)
      {
        throw new BlogException(ex);
      }
      finally
      {
        if (in != null)
        {
          try
          {
            in.close();
          }
          catch (IOException ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }
      }
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
  public SessionInformation getSessionInformation()
  {
    if (sessionInformation == null)
    {
      this.sessionInformation = new SessionInformation(new Date());
    }

    return sessionInformation;
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
  public String getVersion()
  {
    if (Util.isBlank(version))
    {
      String path =
        getServletContext().getRealPath("/WEB-INF/config/version.txt");
      File file = new File(path);

      if (file.exists())
      {
        try
        {
          version = Util.getTextFromFile(file);
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }

    return version;
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
      installed = (getResourceManager() != null)
                  && getConfiguration().getBoolean(Constants.CONFIG_INSTALLED,
                    Boolean.FALSE);
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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param loginContext
   * @param request
   *
   * @return
   *
   * @throws LoginException
   */
  private BlogSession login(LoginContext loginContext, BlogRequest request)
          throws LoginException
  {
    BlogSession blogSession = null;

    if (loginContext != null)
    {
      loginContext.login();

      User user = getUser(loginContext);
      Blog blog = request.getCurrentBlog();

      if ((user != null) && (blog != null))
      {
        blogSession = new BlogSession(loginContext, user, blog);
        request.getSession(true).setAttribute(BlogSession.SESSIONVAR,
                           blogSession);
      }
      else if (logger.isLoggable(Level.WARNING))
      {
        logger.warning("could not find blog or user");
      }
    }

    return blogSession;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param loginContext
   *
   * @return
   */
  private User getUser(LoginContext loginContext)
  {
    User user = null;
    Set<User> userSet = loginContext.getSubject().getPrincipals(User.class);

    if ((userSet != null) &&!userSet.isEmpty())
    {
      user = userSet.iterator().next();
    }

    return user;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File configFile;

  /** Field description */
  private BlogConfiguration configuration;

  /** Field description */
  private ImageHandler imageHandler;

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
  private ServiceReference<MailService> mailService;

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
  private SessionInformation sessionInformation;

  /** Field description */
  private Configuration ssoLoginConfiguration;

  /** Field description */
  private TemplateManager templateManager;

  /** Field description */
  private String version;
}
