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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.InstallationListener;
import sonia.blog.api.app.UpgradeListener;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.dao.jpa.profile.DatabaseProfile;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;
import sonia.blog.util.BlogUtil;

import sonia.plugin.DefaultPluginStore;
import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

import sonia.security.encryption.Encryption;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.text.MessageFormat;

import java.util.ArrayList;
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
 * @author Sebastian Sdorra
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
  private static final String DBEMBEDDED_DRIVER =
    "org.apache.derby.jdbc.EmbeddedDriver";

  /** Field description */
  private static final String DBEMBEDDED_PASSWORD = "pwd4jab";

  /** Field description */
  private static final String DBEMBEDDED_PROFILE = "derby-embedded";

  /** Field description */
  private static final String DBEMBEDDED_URL = "jdbc:derby:{0};create=true";

  /** Field description */
  private static final String DBEMBEDDED_USERNAME = "jab";

  /** Field description */
  private static Logger logger = Logger.getLogger(InstallBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public InstallBean()
  {
    init();
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
    loadDatabaseProfiles();
    resourcePath = new File(Util.getHomeDirectory(), ".jab").getAbsolutePath();
    databaseEmbedded = true;
    databaseDriver = DBEMBEDDED_DRIVER;
    databaseProfile = DBEMBEDDED_PROFILE;
    databaseUrl = MessageFormat.format(
      DBEMBEDDED_URL,
      new File(resourcePath, Constants.RESOURCE_DATABASE).getAbsolutePath());
    databaseUsername = DBEMBEDDED_USERNAME;
    databsePassword = DBEMBEDDED_PASSWORD;
    admin = new User();
    admin.setGlobalAdmin(true);
    admin.setActive(true);
    blog = new Blog();
    blog.setAllowComments(true);
    blog.setActive(true);
    blog.setIdentifier(getRequest().getServerName());
    blog.setTemplate("/template/jab");
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

      configuration.set(Constants.CONFIG_VERSION, context.getVersion());
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

      if (logger.isLoggable(Level.FINE))
      {
        logger.fine("creating DAOFactory");
      }

      DAOFactory daoFactory = BlogContext.getDAOFactory();

      daoFactory.init();
      daoFactory.install();

      boolean error = true;
      BlogSession session = BlogContext.getInstance().getSystemBlogSession();

      if (daoFactory.getUserDAO().add(session, admin))
      {
        if (daoFactory.getBlogDAO().add(session, blog))
        {
          daoFactory.getUserDAO().setRole(session, blog, admin, Role.ADMIN);

          Category category = new Category();

          category.setName(label.getString("defaultCategory"));
          category.setBlog(blog);

          if (daoFactory.getCategoryDAO().add(session, category))
          {
            Entry entry = new Entry();

            entry.setBlog(blog);
            entry.addCateogory(category);
            entry.setAuthor(admin);
            entry.publish();
            entry.setTitle(message.getString("firstEntryTitle"));
            entry.setContent(message.getString("firstEntryContent"));

            if (daoFactory.getEntryDAO().add(session, entry))
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
        getMessageHandler().error(getRequest(), "unknownError");
      }
      else
      {
        configuration.set("defaultBlog", blog.getId());
        configuration.set(Constants.CONFIG_INSTALLED, Boolean.TRUE);
        BlogUtil.configureLogger(context);

        FileOutputStream fos = null;

        try
        {
          fos = new FileOutputStream(BlogContext.getInstance().getConfigFile());
          configuration.store(fos);

          File pluginStore = context.getResourceManager().getDirectory(
                                 Constants.RESOURCE_PLUGINSTORE);

          if (!pluginStore.exists() &&!pluginStore.mkdirs())
          {
            throw new BlogException("could not create pluginstore directory");
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
        finally
        {
          if (fos != null)
          {
            try
            {
              fos.close();
            }
            catch (IOException ex)
            {
              logger.log(Level.SEVERE, null, ex);
            }
          }
        }

        redirect();
      }
    }
    else
    {
      result = FAILURE;
      getMessageHandler().error(getRequest(), "passwordsNotEqual");
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
      if (logger.isLoggable(Level.INFO))
      {
        logger.info("start upgrade");
      }

      upgrade();
    }
    else if (resourceDir.isFile())
    {
      getMessageHandler().error(getRequest(), "form:resourceDir",
                                "resourcePathIsFile");
      result = FAILURE;
    }
    else if (!resourceDir.exists())
    {
      if (!resourceDir.mkdirs())
      {
        getMessageHandler().error(getRequest(), "form:resourceDir",
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

      databaseDriver = DBEMBEDDED_DRIVER;
      databaseUrl = MessageFormat.format(DBEMBEDDED_URL,
                                         dbDriectory.getAbsolutePath());
      databaseUsername = DBEMBEDDED_USERNAME;
      databsePassword = DBEMBEDDED_PASSWORD;
      databaseProfile = DBEMBEDDED_PROFILE;
    }

    try
    {
      Class.forName(databaseDriver).newInstance();
    }
    catch (Exception ex)
    {
      logger.log(Level.WARNING, null, ex);
      getMessageHandler().error(getRequest(), "form:dbDriver",
                                "dbDriverNotFound");
      result = FAILURE;
    }

    if (!checkConnection())
    {
      getMessageHandler().error(getRequest(), "dbNoConnection");
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
    Connection connection = null;

    try
    {
      connection = DriverManager.getConnection(databaseUrl, databaseUsername,
              databsePassword);
      result = true;
    }
    catch (Exception ex)
    {
      logger.log(Level.WARNING, null, ex);
    }
    finally
    {
      try
      {
        if ((connection != null) &&!connection.isClosed())
        {
          connection.close();
        }
      }
      catch (SQLException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   */
  private void loadDatabaseProfiles()
  {
    databaseProfiles = new ArrayList<DatabaseProfile>();

    List<String> profilePathes =
      BlogContext.getInstance().getServiceRegistry().get(String.class,
        Constants.SERVICE_DBPROFILE).getAll();

    for (String profilePath : profilePathes)
    {
      try
      {
        databaseProfiles.add(DatabaseProfile.createProfile(profilePath));
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
   * @param profile
   */
  private void loadSampleData(DatabaseProfile profile)
  {
    databaseDriver = profile.getDriver();
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
  private void upgrade()
  {
    BlogContext context = BlogContext.getInstance();

    if (listeners != null)
    {
      for (InstallationListener listener : listeners)
      {
        listener.beforeInstallation(context);
      }
    }

    writeBaseProperties();

    try
    {
      BlogConfiguration config = context.getConfiguration();

      config.load();

      int oldVersion = config.getInteger(Constants.CONFIG_VERSION);

      config.set(Constants.CONFIG_VERSION, context.getVersion());

      DAOFactory daoFactory = BlogContext.getDAOFactory();

      daoFactory.upgrade(oldVersion);
      daoFactory.init();
      BlogUtil.configureLogger(context);

      ServiceReference<UpgradeListener> upgradeListenerReference =
        context.getServiceRegistry().get(UpgradeListener.class,
                                         Constants.SERVICE_UPGRADELISTENER);

      if (upgradeListenerReference != null)
      {
        List<UpgradeListener> upgradeListeners =
          upgradeListenerReference.getAll();

        if (upgradeListeners != null)
        {
          for (UpgradeListener listener : upgradeListeners)
          {
            listener.upgrade(oldVersion);
          }
        }
      }

      config.store();

      if (listeners != null)
      {
        for (InstallationListener listener : listeners)
        {
          listener.afterInstallation(context);
        }
      }

      redirect();
    }
    catch (IOException ex)
    {
      getMessageHandler().error(getRequest(), "unknownError");
      logger.log(Level.SEVERE, null, ex);
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
      throw new BlogException(ex);
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
