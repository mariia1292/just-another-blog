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
import sonia.blog.api.exception.BlogException;
import sonia.blog.dao.jpa.profile.DatabaseProfile;

import sonia.plugin.service.ServiceReference;

import sonia.util.LineBasedReader;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
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
 * @author Sebastian Sdorra
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
    if (strategy != null)
    {
      strategy.close();
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
    BlogConfiguration config = ctx.getConfiguration();
    String pu = getPU();
    Map<String, Object> parameters = new HashMap<String, Object>();
    File tmpDir =
      ctx.getResourceManager().getDirectory(Constants.RESOURCE_TEMP, true);

    // eclipselink
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

    // toplink
    parameters.put("toplink.jdbc.driver",
                   config.getString(Constants.CONFIG_DB_DRIVER));
    parameters.put("toplink.jdbc.url",
                   config.getString(Constants.CONFIG_DB_URL));
    parameters.put("toplink.jdbc.user",
                   config.getString(Constants.CONFIG_DB_USERNAME));
    parameters.put("toplink.jdbc.password",
                   config.getSecureString(Constants.CONFIG_DB_PASSWORD));
    parameters.put("toplink.application-location", tmpDir.getAbsolutePath());

    // hibernate
    parameters.put("hibernate.connection.driver_class",
                   config.getString(Constants.CONFIG_DB_DRIVER));
    parameters.put("hibernate.connection.url",
                   config.getString(Constants.CONFIG_DB_URL));
    parameters.put("hibernate.connection.username",
                   config.getString(Constants.CONFIG_DB_USERNAME));
    parameters.put("hibernate.connection.password",
                   config.getSecureString(Constants.CONFIG_DB_PASSWORD));

    // openjpa
    parameters.put("openjpa.ConnectionDriverName",
                   config.getString(Constants.CONFIG_DB_DRIVER));
    parameters.put("openjpa.ConnectionURL",
                   config.getString(Constants.CONFIG_DB_URL));
    parameters.put("openjpa.ConnectionUserName",
                   config.getString(Constants.CONFIG_DB_USERNAME));
    parameters.put("openjpa.ConnectionPassword",
                   config.getSecureString(Constants.CONFIG_DB_PASSWORD));

    for (String key : config.keySet())
    {
      if (key.startsWith("eclipselink.") || key.startsWith("toplink.")
          || key.startsWith("hibernate.") || key.startsWith("openjpa."))
      {
        parameters.put(key, config.getString(key));
      }
    }

    EntityManagerFactory emf = Persistence.createEntityManagerFactory(pu,
                                 parameters);

    strategy = new JpaStrategy(emf);
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
  public void release()
  {
    if (strategy != null)
    {
      strategy.release();
    }
  }

  /**
   * Method description
   *
   *
   * @param oldVersion
   */
  @Override
  public void upgrade(int oldVersion)
  {
    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("upgrade DAOFactory from version ").append(oldVersion);
      logger.info(msg.toString());
    }
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
    if (attachmentDAO == null)
    {
      attachmentDAO = new JpaAttachmentDAO(strategy);
    }

    return attachmentDAO;
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
    if (blogDAO == null)
    {
      blogDAO = new JpaBlogDAO(strategy);
    }

    return blogDAO;
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
    if (blogHitCountDAO == null)
    {
      blogHitCountDAO = new JpaBlogHitCountDAO(strategy);
    }

    return blogHitCountDAO;
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
    if (categoryDAO == null)
    {
      categoryDAO = new JpaCategoryDAO(strategy);
    }

    return categoryDAO;
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
    if (commentDAO == null)
    {
      commentDAO = new JpaCommentDAO(strategy);
    }

    return commentDAO;
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
    return strategy.getEntityManagerFactory();
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
    if (entryDAO == null)
    {
      entryDAO = new JpaEntryDAO(strategy);
    }

    return entryDAO;
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
    if (pageDAO == null)
    {
      pageDAO = new JpaPageDAO(strategy);
    }

    return pageDAO;
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
    if (tagDAO == null)
    {
      tagDAO = new JpaTagDAO(strategy);
    }

    return tagDAO;
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
    if (userDAO == null)
    {
      userDAO = new JpaUserDAO(strategy);
    }

    return userDAO;
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
    EntityManager em = strategy.getEntityManager(true);
    InputStream in = profile.getCreationCommands();

    try
    {
      CommandExcecuter ce = new CommandExcecuter(em);

      ce.readLines(in);
      strategy.flush();
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);

      throw new DAOException(ex);
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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private List<DatabaseProfile> getDatabaseProfiles()
  {
    List<DatabaseProfile> profiles = new ArrayList<DatabaseProfile>();
    BlogContext ctx = BlogContext.getInstance();
    ServiceReference<String> reference =
      ctx.getServiceRegistry().get(String.class, Constants.SERVICE_DBPROFILE);
    List<String> profilePathes = reference.getAll();

    for (String profilePath : profilePathes)
    {
      try
      {
        profiles.add(DatabaseProfile.createProfile(profilePath));
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return profiles;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private String getPU()
  {
    String pu = null;

    try
    {
      Class.forName("org.eclipse.persistence.jpa.PersistenceProvider");
      logger.info("load EclispeLink PersistenceProvider");
      pu = "SoniaBlog-eclipse-PU";
    }
    catch (ClassNotFoundException ex) {}

    if (pu == null)
    {
      try
      {
        Class.forName("oracle.toplink.essentials.PersistenceProvider");
        logger.info("load Toplink PersistenceProvider");
        pu = "SoniaBlog-toplink-PU";
      }
      catch (ClassNotFoundException ex) {}
    }

    if (pu == null)
    {
      try
      {
        Class.forName("org.apache.openjpa.persistence.PersistenceProviderImpl");
        logger.info("load OpenJPA PersistenceProvider");
        pu = "SoniaBlog-openjpa-PU";
      }
      catch (ClassNotFoundException ex) {}
    }

    if (pu == null)
    {
      try
      {
        Class.forName("org.hibernate.ejb.HibernatePersistence");
        logger.info("load Hibernate PersistenceProvider");
        pu = "SoniaBlog-hibernate-PU";
      }
      catch (ClassNotFoundException ex) {}
    }

    if (pu == null)
    {
      throw new BlogException(
          "no PersistenceProvider found, check your classpath");
    }

    return pu;
  }

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

    List<DatabaseProfile> profiles = getDatabaseProfiles();

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
  private AttachmentDAO attachmentDAO;

  /** Field description */
  private BlogDAO blogDAO;

  /** Field description */
  private BlogHitCountDAO blogHitCountDAO;

  /** Field description */
  private CategoryDAO categoryDAO;

  /** Field description */
  private CommentDAO commentDAO;

  /** Field description */
  private EntryDAO entryDAO;

  /** Field description */
  private PageDAO pageDAO;

  /** Field description */
  private JpaStrategy strategy;

  /** Field description */
  private TagDAO tagDAO;

  /** Field description */
  private UserDAO userDAO;
}
