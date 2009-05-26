
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
import sonia.blog.api.dao.TrackbackDAO;
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
 * @author sdorra
 */
public class JpaDAOFactory extends DAOFactory {

    /** Field description */
    private static Logger   logger = Logger.getLogger(JpaDAOFactory.class.getName());
    private AttachmentDAO   attachmentDAO;
    private BlogDAO         blogDAO;
    private BlogHitCountDAO blogHitCountDAO;
    private CategoryDAO     categoryDAO;
    private CommentDAO      commentDAO;

    /** Field description */
    private EntityManagerFactory entityManagerFactory;
    private EntryDAO             entryDAO;
    private PageDAO              pageDAO;
    private TagDAO               tagDAO;
    private TrackbackDAO         trackbackDAO;
    private UserDAO              userDAO;

    /**
     * Method description
     *
     */
    @Override
    public void close() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    /**
     * Method description
     *
     */
    @Override
    public void init() {
        BlogContext         ctx        = BlogContext.getInstance();
        String              serverInfo = ctx.getServletContext().getServerInfo();
        BlogConfiguration   config     = ctx.getConfiguration();
        String              pu         = "SoniaBlog-oracle-PU";
        Map<String, String> parameters = new HashMap<String, String>();
        File                tmpDir     = ctx.getResourceManager().getDirectory(Constants.RESOURCE_TEMP, true);

        if (serverInfo.equals("GlassFish/v3")) {
            logger.info("load EclispeLink PersistenceProvider");
            pu = "SoniaBlog-eclipse-PU";
            parameters.put("eclipselink.jdbc.driver", config.getString(Constants.CONFIG_DB_DRIVER));
            parameters.put("eclipselink.jdbc.url", config.getString(Constants.CONFIG_DB_URL));
            parameters.put("eclipselink.jdbc.user", config.getString(Constants.CONFIG_DB_USERNAME));
            parameters.put("eclipselink.jdbc.password", config.getSecureString(Constants.CONFIG_DB_PASSWORD));
            parameters.put("eclipselink.application-location", tmpDir.getAbsolutePath());
        } else {
            logger.info("load Toplink PersistenceProvider");
            parameters.put("toplink.jdbc.driver", config.getString(Constants.CONFIG_DB_DRIVER));
            parameters.put("toplink.jdbc.url", config.getString(Constants.CONFIG_DB_URL));
            parameters.put("toplink.jdbc.user", config.getString(Constants.CONFIG_DB_USERNAME));
            parameters.put("toplink.jdbc.password", config.getSecureString(Constants.CONFIG_DB_PASSWORD));
            parameters.put("toplink.application-location", tmpDir.getAbsolutePath());
        }

        entityManagerFactory = Persistence.createEntityManagerFactory(pu, parameters);
    }

    /**
     * Method description
     *
     */
    @Override
    public void install() {
        DatabaseProfile profile = getProfile();

        if (logger.isLoggable(Level.INFO)) {
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
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public AttachmentDAO getAttachmentDAO() {
        if (attachmentDAO == null) {
            attachmentDAO = new JpaAttachmentDAO(entityManagerFactory);
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
    public BlogDAO getBlogDAO() {
        if (blogDAO == null) {
            blogDAO = new JpaBlogDAO(entityManagerFactory);
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
    public BlogHitCountDAO getBlogHitCountDAO() {
        if (blogHitCountDAO == null) {
            blogHitCountDAO = new JpaBlogHitCountDAO(entityManagerFactory);
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
    public CacheManager getCacheManager() {
        return null;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public CategoryDAO getCategoryDAO() {
        if (categoryDAO == null) {
            categoryDAO = new JpaCategoryDAO(entityManagerFactory);
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
    public CommentDAO getCommentDAO() {
        if (commentDAO == null) {
            commentDAO = new JpaCommentDAO(entityManagerFactory);
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
    public Object getConnection() {
        return entityManagerFactory;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public EntryDAO getEntryDAO() {
        if (entryDAO == null) {
            entryDAO = new JpaEntryDAO(entityManagerFactory);
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
    public PageDAO getPageDAO() {
        if (pageDAO == null) {
            pageDAO = new JpaPageDAO(entityManagerFactory);
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
    public TagDAO getTagDAO() {
        if (tagDAO == null) {
            tagDAO = new JpaTagDAO(entityManagerFactory);
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
    public TrackbackDAO getTrackbackDAO() {
        if (trackbackDAO == null) {
            trackbackDAO = new JpaTrackbackDAO(entityManagerFactory);
        }

        return trackbackDAO;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new JpaUserDAO(entityManagerFactory);
        }

        return userDAO;
    }

    /**
     * Method description
     *
     *
     * @param profile
     */
    private void excecuteCommands(DatabaseProfile profile) {
        EntityManager em = entityManagerFactory.createEntityManager();
        InputStream   in = profile.getCreationCommands();

        try {
            em.getTransaction().begin();

            CommandExcecuter ce = new CommandExcecuter(em);

            ce.readLines(in);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.close();
            }

            logger.log(Level.SEVERE, null, ex);

            throw new DAOException(ex);
        } finally {
            em.close();

            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Method description
     *
     *
     * @return
     */
    private List<DatabaseProfile> getDatabaseProfiles() {
        List<DatabaseProfile>    profiles      = new ArrayList<DatabaseProfile>();
        BlogContext              ctx           = BlogContext.getInstance();
        ServiceReference<String> reference     = ctx.getServiceRegistry().get(String.class,
                                                     Constants.SERVICE_DBPROFILE);
        List<String>             profilePathes = reference.getAll();

        for (String profilePath : profilePathes) {
            try {
                profiles.add(DatabaseProfile.createProfile(profilePath));
            } catch (IOException ex) {
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
    private DatabaseProfile getProfile() {
        DatabaseProfile   profile     = null;
        BlogContext       ctx         = BlogContext.getInstance();
        BlogConfiguration config      = ctx.getConfiguration();
        String            profileName = config.getString(Constants.CONFIG_DB_PROFILE);

        if (Util.isBlank(profileName)) {
            throw new IllegalStateException("profile is empty");
        }

        List<DatabaseProfile> profiles = getDatabaseProfiles();

        for (DatabaseProfile dbProfile : profiles) {
            if (dbProfile.getName().equals(profileName)) {
                profile = dbProfile;

                break;
            }
        }

        return profile;
    }

    /**
     * Class description
     *
     *
     * @version        Enter version here..., 09/04/26
     * @author         Enter your name here...
     */
    private static class CommandExcecuter extends LineBasedReader {

        /** Field description */
        private EntityManager em;

        /**
         * Constructs ...
         *
         *
         * @param em
         */
        public CommandExcecuter(EntityManager em) {
            this.em = em;
        }

        /**
         * Method description
         *
         *
         * @param line
         */
        @Override
        public void invoke(String line) {
            if (logger.isLoggable(Level.FINEST)) {
                StringBuffer log = new StringBuffer();

                log.append("invoke ").append(line);
                logger.finest(log.toString());
            }

            Query q = em.createNativeQuery(line);

            q.executeUpdate();
        }
    }
}
