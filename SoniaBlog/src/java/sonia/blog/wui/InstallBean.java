/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.config.StoreableConfiguration;
import sonia.config.XmlConfiguration;

import sonia.plugin.ServiceReference;

import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;

import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;

import javax.persistence.EntityManager;

/**
 *
 * @author sdorra
 */
public class InstallBean extends AbstractBean
{

  /**
   * Method description
   *
   */
  @Override
  public void init()
  {
    super.init();
    databaseEmbedded = true;
    databaseDriver = "org.apache.derby.jdbc.ClientDriver";
    databaseUrl = "jdbc:derby://localhost:1527/SoniaBlog";
    databaseUsername = "root";
    resourcePath = BlogContext.getInstance().getServletContext().getRealPath(
      "WEB-INF/resources");
    admin = new User();
    admin.setGlobalAdmin(true);
    admin.setActive(true);
    blog = new Blog();
    blog.setAllowComments(true);
    blog.setActive(true);
    blog.setServername(getRequest().getServerName());
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
      BlogContext context = BlogContext.getInstance();
      ServiceReference reference =
        context.getServiceRegistry().getServiceReference(
            Constants.SERVCIE_ENCRYPTION);

      if ((reference != null) && (reference.getImplementation() != null))
      {
        Encryption enc = (Encryption) reference.getImplementation();

        admin.setPassword(enc.encrypt(passwordRepeat));
      }

      XmlConfiguration configuration = context.getConfiguration();

      configuration.set("db.driver", databaseDriver);
      configuration.set("db.url", databaseUrl);
      configuration.set("db.username", databaseUsername);
      configuration.set("db.password", databsePassword);
      configuration.set("resource.directory", resourcePath);

      /*
       * try
       * {
       * configureLogger();
       * }
       * catch (IOException ex)
       * {
       * logger.log(Level.SEVERE, null, ex);
       * }
       */

      // create first Entry
      ResourceBundle message = getResourceBundle("message");
      ResourceBundle label = getResourceBundle("label");
      Category category = new Category();

      category.setName(label.getString("defaultCategory"));
      category.setBlog(blog);

      Entry entry = new Entry();

      entry.setCategory(category);
      entry.setAuthor(admin);
      entry.setTitle(message.getString("firstEntryTitle"));
      entry.setContent(message.getString("firstEntryContent"));

      EntityManager em = context.getEntityManager(true);

      em.getTransaction().begin();

      try
      {
        em.persist(admin);
        em.persist(blog);

        BlogMember member = new BlogMember(blog, admin, Role.ADMIN);

        em.persist(member);
        em.persist(category);
        em.persist(entry);
        em.getTransaction().commit();
        configuration.set("defaultBlog", blog.getId());

        if (configuration instanceof StoreableConfiguration)
        {
          try
          {
            configuration.store(
                new FileOutputStream(
                    BlogContext.getInstance().getConfigFile()));
          }
          catch (Exception ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }

        String uri = context.getLinkBuilder().buildLink(getRequest(), "/blog");

        sendRedirect(uri);
      }
      catch (Exception ex)
      {
        if (em.getTransaction().isActive())
        {
          em.getTransaction().rollback();
        }

        result = FAILURE;
        getMessageHandler().error("unknownError");
        logger.log(Level.SEVERE, null, ex);
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
   * @return
   */
  public String next()
  {
    String result = SUCCESS;
    File file = new File(resourcePath);

    if (file.isFile())
    {
      getMessageHandler().error("form:resourceDir", "resourcePathIsFile");
      result = FAILURE;
    }
    else if (!file.exists())
    {
      if (!file.mkdirs())
      {
        getMessageHandler().error("form:resourceDir",
                                  "resourcePathCreationFailure");
        result = FAILURE;
      }
    }

    File resourceDirectory = new File(resourcePath);

    new File(resourceDirectory, "index").mkdirs();
    new File(resourceDirectory, "attachment").mkdirs();
    new File(resourceDirectory, "logs").mkdirs();

    File dbDriectory = new File(resourceDirectory, "db");

    dbDriectory.mkdirs();

    File jabDBDirectory = new File(dbDriectory, "jab");

    if (databaseEmbedded)
    {
      databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
      databaseUrl = "jdbc:derby:" + jabDBDirectory.getPath() + ";create=true";
      databaseUsername = "jab";
      databsePassword = "pwd4jab";
    }

    try
    {
      Class.forName(databaseDriver);
    }
    catch (ClassNotFoundException ex)
    {
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
      logger.log(Level.FINE, null, ex);
    }

    return result;
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
  private String databaseUrl;

  /** Field description */
  private String databaseUsername;

  /** Field description */
  private String databsePassword;

  /** Field description */
  private String passwordRepeat;

  /** Field description */
  private String resourcePath;
}
