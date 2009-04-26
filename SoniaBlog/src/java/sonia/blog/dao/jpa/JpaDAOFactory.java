/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.BlogHitCountDAO;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.DAOException;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.dao.cache.CacheManager;
import sonia.blog.dao.jpa.profile.DatabaseProfile;

import sonia.plugin.service.ServiceReference;

import sonia.util.LineBasedReader;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class JpaDAOFactory extends DAOFactory
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(JpaDAOFactory.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Override
  public void close()
  {
    if (entityManagerFactory != null)
    {
      entityManagerFactory.close();
    }
  }

  /**
   * Method description
   *
   */
  @Override
  public void init()
  {
    BlogContext ctx = BlogContext.getInstance();
    String serverInfo = ctx.getServletContext().getServerInfo();
    BlogConfiguration config = ctx.getConfiguration();
    String pu = "SoniaBlog-oracle-PU";
    Map<String, String> parameters = new HashMap<String, String>();
    File tmpDir =
      ctx.getResourceManager().getDirectory(Constants.RESOURCE_TEMP, true);

    if (serverInfo.equals("GlassFish/v3"))
    {
      logger.info("load EclispeLink PersistenceProvider");
      pu = "SoniaBlog-eclipse-PU";
      parameters.put("eclipselink.jdbc.driver",
                     config.getString(Constants.CONFIG_DB_DRIVER));
      parameters.put("eclipselink.jdbc.url",
                     config.getString(Constants.CONFIG_DB_URL));
      parameters.put("eclipselink.jdbc.user",
                     config.getString(Constants.CONFIG_DB_USERNAME));
      parameters.put("eclipselink.jdbc.password",
                     config.getSecureString(Constants.CONFIG_DB_PASSWORD));
      parameters.put("eclipselink.application-location",
                     tmpDir.getAbsolutePath());
    }
    else
    {
      logger.info("load Toplink PersistenceProvider");
      parameters.put("toplink.jdbc.driver",
                     config.getString(Constants.CONFIG_DB_DRIVER));
      parameters.put("toplink.jdbc.url",
                     config.getString(Constants.CONFIG_DB_URL));
      parameters.put("toplink.jdbc.user",
                     config.getString(Constants.CONFIG_DB_USERNAME));
      parameters.put("toplink.jdbc.password",
                     config.getSecureString(Constants.CONFIG_DB_PASSWORD));
      parameters.put("toplink.application-location", tmpDir.getAbsolutePath());
    }

    entityManagerFactory = Persistence.createEntityManagerFactory(pu,
            parameters);
  }

  /**
   * Method description
   *
   */
  @Override
  public void install()
  {
    DatabaseProfile profile = getProfile();

    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer log = new StringBuffer();

      log.append("install database with profile ").append(profile.getName());
      logger.info(log.toString());
    }

    excecuteCommands(profile);
  }

  /**
   * Method description
   *
   */
  @Override
  public void update()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public AttachmentDAO getAttachmentDAO()
  {
    return new JpaAttachmentDAO(entityManagerFactory);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public BlogDAO getBlogDAO()
  {
    return new JpaBlogDAO(entityManagerFactory);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public BlogHitCountDAO getBlogHitCountDAO()
  {
    return new JpaBlogHitCountDAO(entityManagerFactory);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public CacheManager getCacheManager()
  {
    return null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public CategoryDAO getCategoryDAO()
  {
    return new JpaCategoryDAO(entityManagerFactory);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public CommentDAO getCommentDAO()
  {
    return new JpaCommentDAO(entityManagerFactory);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Object getConnection()
  {
    return entityManagerFactory;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public EntityManagerFactory getEntityManagerFactory()
  {
    return entityManagerFactory;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public EntryDAO getEntryDAO()
  {
    return new JpaEntryDAO(entityManagerFactory);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public PageDAO getPageDAO()
  {
    return new JpaPageDAO(entityManagerFactory);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public TagDAO getTagDAO()
  {
    return new JpaTagDAO(entityManagerFactory);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public UserDAO getUserDAO()
  {
    return new JpaUserDAO(entityManagerFactory);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param profile
   */
  private void excecuteCommands(DatabaseProfile profile)
  {
    EntityManager em = entityManagerFactory.createEntityManager();
    InputStream in = profile.getCreationCommands();

    try
    {
      em.getTransaction().begin();

      CommandExcecuter ce = new CommandExcecuter(em);

      ce.readLines(in);
      em.getTransaction().commit();
    }
    catch (Exception ex)
    {
      if (em.getTransaction().isActive())
      {
        em.close();
      }

      logger.log(Level.SEVERE, null, ex);

      throw new DAOException(ex);
    }
    finally
    {
      em.close();

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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private DatabaseProfile getProfile()
  {
    DatabaseProfile profile = null;
    BlogContext ctx = BlogContext.getInstance();
    BlogConfiguration config = ctx.getConfiguration();
    String profileName = config.getString(Constants.CONFIG_DB_PROFILE);

    if (Util.isBlank(profileName))
    {
      throw new IllegalStateException("profile is empty");
    }

    ServiceReference<DatabaseProfile> reference =
      ctx.getServiceRegistry().get(DatabaseProfile.class,
                                   Constants.SERVICE_DBPROFILE);
    List<DatabaseProfile> profiles = reference.getAll();

    for (DatabaseProfile dbProfile : profiles)
    {
      if (dbProfile.getName().equals(profileName))
      {
        profile = dbProfile;

        break;
      }
    }

    return profile;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/04/26
   * @author         Enter your name here...
   */
  private static class CommandExcecuter extends LineBasedReader
  {

    /**
     * Constructs ...
     *
     *
     * @param em
     */
    public CommandExcecuter(EntityManager em)
    {
      this.em = em;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param line
     */
    @Override
    public void invoke(String line)
    {
      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer log = new StringBuffer();

        log.append("invoke ").append(line);
        logger.finest(log.toString());
      }

      Query q = em.createNativeQuery(line);

      q.executeUpdate();
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private EntityManager em;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private EntityManagerFactory entityManagerFactory;
}
