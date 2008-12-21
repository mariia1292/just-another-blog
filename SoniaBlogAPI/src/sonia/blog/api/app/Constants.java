/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

/**
 *
 * @author sdorra
 */
public interface Constants
{

  /** Field description */
  public static final String CONFIG_ALLOW_BLOGCREATION = "allow.blogCreation";

  /** Field description */
  public static final String CONFIG_ALLOW_REGISTRATION = "allow.registration";

  /** Field description */
  public static final String CONFIG_DB_DRIVER = "db.driver";

  /** Field description */
  public static final String CONFIG_DB_PASSWORD = "db.password";

  /** Field description */
  public static final String CONFIG_DB_URL = "db.url";

  /** Field description */
  public static final String CONFIG_DB_USERNAME = "db.username";

  /** Field description */
  public static String CONFIG_DEFAULTBLOG = "defaultBlog";

  /** Field description */
  public static final String CONFIG_ENCODING = "encoding";

  /** Field description */
  public static final String CONFIG_FEEDTYPE = "feed.type";

  /** Field description */
  public static final String CONFIG_IMAGEEXTENSION = "image.extension";

  /** Field description */
  public static final String CONFIG_IMAGEFORMAT = "image.format";

  /** Field description */
  public static final String CONFIG_IMAGEMIMETYPE = "image.mimetype";

  /** Field description */
  public static final String CONFIG_PASSWORD_MINLENGTH = "password.minLength";

  /** Field description */
  public static final String CONFIG_RESOURCE_DIRECTORY = "resource.directory";

  /** Field description */
  public static final String DEFAULT_ENCODING = "UTF-8";

  /** Field description */
  public static final String DEFAULT_IMAGE_EXTENSION = "jpg";

  /** Field description */
  public static final String DEFAULT_IMAGE_FORMAT = "jpg";

  /** Field description */
  public static final String DEFAULT_IMAGE_MIMETYPE = "image/jpeg";

  /** Field description */
  public static final int DEFAULT_PASSWORD_MINLENGTH = 6;

  /** Field description */
  public static final String LOGINMODULE_NAME = "BlogLoginModule";

  /** Field description */
  public static final String NAVIGATION_ADMIN = "/nav/admin";

  /** Field description */
  public static final String NAVIGATION_AUTHOR = "/nav/author";

  /** Field description */
  public static final String NAVIGATION_EXTRA = "/nav/extra";

  /** Field description */
  public static final String NAVIGATION_GLOBALADMIN = "/nav/globaladmin";

  /** Field description */
  public static final String NAVIGATION_READER = "/nav/reader";

  /** Field description */
  public static final String PAGE_PREFIX = "blog";

  /** Field description */
  public static final char[] SECRET_KEY =
  {
    'L', 'W', '/', '!', 'C', 'b', 'X', ']', 'R', 'f', '4', 'B', 'H', 'd', 'I',
    '['
  };

  /** Field description */
  public static final String SERVCIE_CIPHER = "/app/cipher";

  /** Field description */
  public static final String SERVCIE_ENCRYPTION = "/app/encryption";

  /** Field description */
  public static final String SERVCIE_GLOBALCONFIGPROVIDER = "/config/global";

  /** Field description */
  public static final String SERVCIE_GLOBALSTATUSROVIDER = "/status/global";

  /** Field description */
  public static final String SERVICE_ATTACHMENT_LISTENER =
    "/listener/attachment";

  /** Field description */
  public static final String SERVICE_AUTHENTICATION = "/app/authentication";

  /** Field description */
  public static final String SERVICE_BLOG_LISTENER = "/listener/blog";

  /** Field description */
  public static final String SERVICE_COMMENT_LISTENER = "/listener/comment";

  /** Field description */
  public static final String SERVICE_CONTEXTLISTENER = "/contextListener";

  /** Field description */
  public static final String SERVICE_ENTRY_LISTENER = "/listener/entry";

  /** Field description */
  public static final String SERVICE_LINKBUILDER = "/app/linkBuilder";

  /** Field description */
  public static final String SERVICE_MAPPINGHANDLER = "/app/mappingHandler";

  /** Field description */
  public static final String SERVICE_SEARCHCONTEXT = "/app/searchContext";

  /** Field description */
  public static final String SERVICE_SPAMPROTECTIONMETHOD = "/app/input/spam";
}
