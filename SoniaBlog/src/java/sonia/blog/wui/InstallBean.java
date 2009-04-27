/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRuntimeException;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.InstallationListener;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.dao.jpa.profile.DatabaseProfile;
import sonia.blog.dao.jpa.profile.DerbyProfile;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;
import sonia.blog.util.BlogUtil;

import sonia.config.StoreableConfiguration;

import sonia.plugin.DefaultPluginStore;
import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.sql.Connection;
import java.sql.DriverManager;

import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author sdorra
 */
public class InstallBean extends AbstractBean
{

  /** Field description */
  public static final String STEP1 = "step1";

  /** Field description */
  public static final String STEP2 = "step2";

  /** Field description */
  public static final String STEP3 = "step3";

  /** Field description */
  private static Logger logger = Logger.getLogger(InstallBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public InstallBean()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Override
  public void init()
  {
    super.init();
    databaseEmbedded = true;
    databaseDriver = DerbyProfile.DRIVER;
    databaseUrl = DerbyProfile.URL;
    databaseUsername = DerbyProfile.USER;
    databaseProfile = DerbyProfile.NAME;
    resourcePath = BlogContext.getInstance().getServletContext().getRealPath(
      "WEB-INF/resources");
    admin = new User();
    admin.setGlobalAdmin(true);
    admin.setActive(true);
    blog = new Blog();
    blog.setAllowComments(true);
    blog.setActive(true);
    blog.setIdentifier(getRequest().getServerName());
    blog.setTemplate("jab");
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String install()
  {
    String result = SUCCESS;

    if (admin.getPassword().equals(passwordRepeat))
    {
      writeBaseProperties();

      BlogContext context = BlogContext.getInstance();
      ServiceReference<Encryption> reference =
        context.getServiceRegistry().get(Encryption.class,
                                         Constants.SERVCIE_ENCRYPTION);

      if ((reference != null) && (reference.get() != null))
      {
        Encryption enc = reference.get();

        admin.setPassword(enc.encrypt(passwordRepeat));
      }

      BlogConfiguration configuration = context.getConfiguration();

      configuration.set(Constants.CONFIG_DB_PROFILE, databaseProfile);
      configuration.set(Constants.CONFIG_DB_DRIVER, databaseDriver);
      configuration.set(Constants.CONFIG_DB_URL, databaseUrl);
      configuration.set(Constants.CONFIG_DB_USERNAME, databaseUsername);
      configuration.setSecureString(Constants.CONFIG_DB_PASSWORD,
                                    databsePassword);

      if (isDatabaseEmbedded())
      {
        configuration.set(Constants.CONFIG_DB_EMBEDDED, Boolean.TRUE);
      }

      if (listeners != null)
      {
        for (InstallationListener listener : listeners)
        {
          listener.beforeInstallation(context);
        }
      }

      ResourceBundle message = getResourceBundle("message");
      ResourceBundle label = getResourceBundle("label");
      Category category = new Category();

      category.setName(label.getString("defaultCategory"));
      category.setBlog(blog);

      Entry entry = new Entry();

      entry.setBlog(blog);
      entry.addCateogory(category);
      entry.setAuthor(admin);
      entry.publish();
      entry.setTitle(message.getString("firstEntryTitle"));
      entry.setContent(message.getString("firstEntryContent"));

      if (logger.isLoggable(Level.FINE))
      {
        logger.fine("creating DAOFactory");
      }

      DAOFactory daoFactory = BlogContext.getDAOFactory();

      daoFactory.init();
      daoFactory.install();

      boolean error = true;

      if (daoFactory.getUserDAO().add(admin))
      {
        if (daoFactory.getBlogDAO().add(blog))
        {
          daoFactory.getUserDAO().setRole(blog, admin, Role.ADMIN);

          if (daoFactory.getCategoryDAO().add(category))
          {
            if (daoFactory.getEntryDAO().add(entry))
            {
              error = false;
            }
            else
            {
              logger.severe("error during entry creation");

              // cant create entry
            }
          }
          else
          {
            logger.severe("error during category creation");

            // cat create category
          }
        }
        else
        {
          logger.severe("error during blog creation");

          // cant create blog
        }
      }
      else
      {
        logger.severe("error during user creation");

        // cant create user
      }

      if (error)
      {
        getMessageHandler().error("unknownError");
      }
      else
      {
        configuration.set("defaultBlog", blog.getId());
        configuration.set(Constants.CONFIG_INSTALLED, Boolean.TRUE);

        if (configuration instanceof StoreableConfiguration)
        {
          try
          {
            configuration.store(
                new FileOutputStream(
                    BlogContext.getInstance().getConfigFile()));

            File pluginStore = context.getResourceManager().getDirectory(
                                   Constants.RESOURCE_PLUGINSTORE);

            if (!pluginStore.exists())
            {
              pluginStore.mkdirs();
            }

            context.getPluginContext().setStore(
                new DefaultPluginStore(pluginStore));

            if (listeners != null)
            {
              for (InstallationListener listener : listeners)
              {
                listener.beforeInstallation(context);
              }
            }
          }
          catch (Exception ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }

        redirect();
      }
    }
    else
    {
      result = FAILURE;
      getMessageHandler().error("passwordsNotEqual");
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void profileChange(ValueChangeEvent event)
  {
    String profileName = (String) event.getNewValue();

    for (DatabaseProfile profile : databaseProfiles)
    {
      if (profile.getName().equals(profileName))
      {
        loadSampleData(profile);

        break;
      }
    }
  }

  /**
   * Method description
   *
   */
  public void redirect()
  {
    BlogContext context = BlogContext.getInstance();
    String uri = context.getLinkBuilder().buildLink(getRequest(), "/");

    sendRedirect(uri);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String step2()
  {
    String result = STEP2;
    File resourceDir = new File(resourcePath);

    if (isAllreadyInstalled(resourceDir))
    {
      writeBaseProperties();

      try
      {
        BlogContext.getInstance().getConfiguration().load();
        BlogContext.getDAOFactory().init();
        redirect();
      }
      catch (IOException ex)
      {
        getMessageHandler().error("unknownError");
        logger.log(Level.SEVERE, null, ex);
      }
    }
    else if (resourceDir.isFile())
    {
      getMessageHandler().error("form:resourceDir", "resourcePathIsFile");
      result = FAILURE;
    }
    else if (!resourceDir.exists())
    {
      if (!resourceDir.mkdirs())
      {
        getMessageHandler().error("form:resourceDir",
                                  "resourcePathCreationFailure");
        result = FAILURE;
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String step3()
  {
    String result = STEP3;

    if (databaseEmbedded)
    {
      File resourceDirectory = new File(resourcePath);
      File dbDriectory = new File(resourceDirectory,
                                  Constants.RESOURCE_DATABASE);

      dbDriectory.mkdirs();

      File jabDBDirectory = new File(dbDriectory, "jab");

      databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
      databaseUrl = "jdbc:derby:" + jabDBDirectory.getPath() + ";create=true";
      databaseUsername = "jab";
      databsePassword = "pwd4jab";
      databaseProfile = DerbyProfile.NAME;
    }

    try
    {
      Class.forName(databaseDriver).newInstance();
    }
    catch (Exception ex)
    {
      logger.log(Level.WARNING, null, ex);
      getMessageHandler().error("form:dbDriver", "dbDriverNotFound");
      result = FAILURE;
    }

    if (!checkConnection())
    {
      getMessageHandler().error("dbNoConnection");
      result = FAILURE;
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public User getAdmin()
  {
    return admin;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDatabaseDriver()
  {
    return databaseDriver;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDatabaseProfile()
  {
    return databaseProfile;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getDatabaseProfiles()
  {
    int size = databaseProfiles.size();
    SelectItem[] items = new SelectItem[size];

    for (int i = 0; i < size; i++)
    {
      DatabaseProfile dbP = databaseProfiles.get(i);

      items[i] = new SelectItem(dbP.getName(), dbP.getDisplayName());
    }

    return items;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDatabaseUrl()
  {
    return databaseUrl;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDatabaseUsername()
  {
    return databaseUsername;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDatabsePassword()
  {
    return databsePassword;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getLocaleItems()
  {
    return BlogUtil.getLocaleItems(FacesContext.getCurrentInstance());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPasswordRepeat()
  {
    return passwordRepeat;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getResourcePath()
  {
    return resourcePath;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getTimeZoneItems()
  {
    return BlogUtil.getTimeZoneItems();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isDatabaseEmbedded()
  {
    return databaseEmbedded;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param admin
   */
  public void setAdmin(User admin)
  {
    this.admin = admin;
  }

  /**
   * Method description
   *
   *
   * @param blog
   */
  public void setBlog(Blog blog)
  {
    this.blog = blog;
  }

  /**
   * Method description
   *
   *
   * @param databaseDriver
   */
  public void setDatabaseDriver(String databaseDriver)
  {
    this.databaseDriver = databaseDriver;
  }

  /**
   * Method description
   *
   *
   * @param databaseEmbedded
   */
  public void setDatabaseEmbedded(boolean databaseEmbedded)
  {
    this.databaseEmbedded = databaseEmbedded;
  }

  /**
   * Method description
   *
   *
   * @param databaseProfile
   */
  public void setDatabaseProfile(String databaseProfile)
  {
    this.databaseProfile = databaseProfile;
  }

  /**
   * Method description
   *
   *
   * @param databaseUrl
   */
  public void setDatabaseUrl(String databaseUrl)
  {
    this.databaseUrl = databaseUrl;
  }

  /**
   * Method description
   *
   *
   * @param databaseUsername
   */
  public void setDatabaseUsername(String databaseUsername)
  {
    this.databaseUsername = databaseUsername;
  }

  /**
   * Method description
   *
   *
   * @param databsePassword
   */
  public void setDatabsePassword(String databsePassword)
  {
    this.databsePassword = databsePassword;
  }

  /**
   * Method description
   *
   *
   * @param passwordRepeat
   */
  public void setPasswordRepeat(String passwordRepeat)
  {
    this.passwordRepeat = passwordRepeat;
  }

  /**
   * Method description
   *
   *
   * @param resourcePath
   */
  public void setResourcePath(String resourcePath)
  {
    this.resourcePath = resourcePath;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private boolean checkConnection()
  {
    boolean result = false;

    try
    {
      Connection connection = DriverManager.getConnection(databaseUrl,
                                databaseUsername, databsePassword);

      connection.close();
      result = true;
    }
    catch (Exception ex)
    {
      logger.log(Level.WARNING, null, ex);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param profile
   */
  private void loadSampleData(DatabaseProfile profile)
  {
    databaseDriver = profile.getSampleDriver();
    databaseUrl = profile.getSampleUrl();
    databaseUsername = profile.getSampleUser();

    FacesContext ctx = FacesContext.getCurrentInstance();
    UIViewRoot vr = ctx.getViewRoot();
    UIComponent cmp = vr.findComponent("form:dbDriver");

    if ((cmp != null) && (cmp instanceof UIInput))
    {
      ((UIInput) cmp).setValue(databaseDriver);
    }

    cmp = vr.findComponent("form:dbUrl");

    if ((cmp != null) && (cmp instanceof UIInput))
    {
      ((UIInput) cmp).setValue(databaseUrl);
    }

    cmp = vr.findComponent("form:dbUsername");

    if ((cmp != null) && (cmp instanceof UIInput))
    {
      ((UIInput) cmp).setValue(databaseUsername);
    }
  }

  /**
   * Method description
   *
   */
  private void writeBaseProperties()
  {
    Properties props = new Properties();

    props.setProperty("resource.home", resourcePath);

    OutputStream out = null;

    try
    {
      String path = BlogContext.getInstance().getServletContext().getRealPath(
                        "/WEB-INF/base.properties");

      out = new FileOutputStream(path);
      props.store(out, "BaseConfiguration");
    }
    catch (IOException ex)
    {
      throw new BlogRuntimeException(ex);
    }
    finally
    {
      if (out != null)
      {
        try
        {
          out.close();
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param resourceDir
   *
   * @return
   */
  private boolean isAllreadyInstalled(File resourceDir)
  {
    boolean result = false;

    if (resourceDir.exists())
    {
      StringBuffer configPath = new StringBuffer();

      configPath.append(Constants.RESOURCE_CONFIG).append(File.separator);
      configPath.append("main-config.xml");
      result = new File(resourceDir, configPath.toString()).exists();
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private User admin;

  /** Field description */
  private Blog blog;

  /** Field description */
  private String databaseDriver;

  /** Field description */
  private boolean databaseEmbedded;

  /** Field description */
  private String databaseProfile;

  /** Field description */
  @Service(Constants.SERVICE_DBPROFILE)
  private List<DatabaseProfile> databaseProfiles;

  /** Field description */
  private String databaseUrl;

  /** Field description */
  private String databaseUsername;

  /** Field description */
  private String databsePassword;

  /** Field description */
  @Service(Constants.SERVICE_INSTALLATIONLISTENER)
  private List<InstallationListener> listeners;

  /** Field description */
  private String passwordRepeat;

  /** Field description */
  private String resourcePath;
}
