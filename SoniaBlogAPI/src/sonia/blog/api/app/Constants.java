/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- JDK imports ------------------------------------------------------------

import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author sdorra
 */
public interface Constants
{

  /** Field description */
  public static final String CONFIG_ADMIN_PAGESIZE = "admin.page-size";

  /** Field description */
  public static final String CONFIG_ALLOW_BLOGCREATION = "allow.blogCreation";

  /** Field description */
  public static final String CONFIG_ALLOW_REGISTRATION = "allow.registration";

  /** Field description */
  public static final String CONFIG_CLEANUPCODE = "editor.cleanup";

  /** Field description */
  public static final String CONFIG_COKKIETIME = "cookie.time";

  /** Field description */
  public static final String CONFIG_COMMAND_RESIZE_IMAGE =
    "app.cmd.resize-image";

  /** Field description */
  public static final String CONFIG_COOKIEKEY = "app.cookieKey";

  /** Field description */
  public static final String CONFIG_DB_DRIVER = "db.driver";

  /** Field description */
  public static final String CONFIG_DB_EMBEDDED = "db.embedded";

  /** Field description */
  public static final String CONFIG_DB_PASSWORD = "db.password";

  /** Field description */
  public static final String CONFIG_DB_URL = "db.url";

  /** Field description */
  public static final String CONFIG_DB_USERNAME = "db.username";

  /** Field description */
  public static final String CONFIG_DEFAULTBLOG = "defaultBlog";

  /** Field description */
  public static final String CONFIG_DEFAULTROLE = "default.role";

  /** Field description */
  public static final String CONFIG_DOMAIN = "domain";

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
  public static final String CONFIG_INSTALLED = "app.installed";

  /** Field description */
  public static final String CONFIG_JMX_ENABLE = "app.enable-jmx";

  /** Field description */
  public static final String CONFIG_PASSWORD_MINLENGTH = "password.minLength";

  /** Field description */
  public static final String CONFIG_REGISTERACKNOWLEDGEMENT =
    "register.acknowledgement.mail";

  /** Field description */
  public static final String CONFIG_RESIZE_IMAGE = "app.resize-image";

  /** Field description */
  public static final String CONFIG_RESOURCE_DIRECTORY = "resource.directory";

  /** Field description */
  public static final String CONFIG_SECUREKEY = "app.securekey";

  /** Field description */
  public static final String CONFIG_SMTPPASSWORD = "smtp.password";

  /** Field description */
  public static final String CONFIG_SMTPPORT = "smtp.port";

  /** Field description */
  public static final String CONFIG_SMTPSERVER = "smtp.server";

  /** Field description */
  public static final String CONFIG_SMTPSTARTTLS = "smtp.starttls";

  /** Field description */
  public static final String CONFIG_SMTPUSER = "smtp.user";

  /** Field description */
  public static final String CONFIG_SPAMMETHOD = "spam.method";

  /** Field description */
  public static final String CONFIG_SSO = "auth.sso";

  /** Field description */
  public static final String COOKIE_NAME = "jab.login.cookie";

  /** Field description */
  public static final String DEFAULT_COMMAND_RESIZE_IMAGE =
    "convert -resize {3,choice,0#|0<{3}}{4,choice,0#|0<x{4}} {0} {2}:{1}";

  /** Field description */
  public static final int DEFAULT_COOKIETIME = 60 * 60 * 24 * 31;

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
  public static final String DEFAULT_RESIZE_IMAGE = "internal";

  // Listeners

  /** Field description */
  public static final String LISTENER_ATTACHMENT = "/listener/attachment";

  /** Field description */
  public static final String LISTENER_BLOG = "/listener/blog";

  /** Field description */
  public static final String LISTENER_BLOGHITCOUNT = "/listener/blogHitCount";

  /** Field description */
  public static final String LISTENER_CATEGORY = "/listener/category";

  /** Field description */
  public static final String LISTENER_COMMENT = "/listener/comment";

  /** Field description */
  public static final String LISTENER_ENTRY = "/listener/entry";

  /** Field description */
  public static final String LISTENER_MEMBER = "/listener/member";

  /** Field description */
  public static final String LISTENER_PAGE = "/listener/page";

  /** Field description */
  public static final String LISTENER_TAG = "/listener/tag";

  /** Field description */
  public static final String LISTENER_USER = "/listener/user";

  /** Field description */
  public static final String LOGINMODULE_NAME = "BlogLoginModule";

  /** Field description */
  public static final String MAPPING_FILE = "/WEB-INF/config/mapping.xml";

  /*
   * Navigation
   */

  /** Field description */
  public static final String NAVIGATION_ADMIN = "/nav/admin";

  /** Field description */
  public static final String NAVIGATION_AUTHOR = "/nav/author";

  /** Field description */
  public static final String NAVIGATION_BLOGACTION =
    "/nav/globaladmin/blog/action";

  /** Field description */
  public static final String NAVIGATION_EXTRA = "/nav/extra";

  /** Field description */
  public static final String NAVIGATION_GLOBALADMIN = "/nav/globaladmin";

  /** Field description */
  public static final String NAVIGATION_READER = "/nav/reader";

  /*
   * Other
   */

  /** Field description */
  public static final String PAGE_PREFIX = "blog";

  /*
   * Resources
   */

  /** Field description */
  public static final String RESOURCE_ATTACHMENT = "attachments";

  /** Field description */
  public static final String RESOURCE_CONFIG = "config";

  /** Field description */
  public static final String RESOURCE_DATABASE = "db";

  /** Field description */
  public static final String RESOURCE_ENTRIES = "entries";

  /** Field description */
  public static final String RESOURCE_IMAGE = "images";

  /** Field description */
  public static final String RESOURCE_INDEX = "index";

  /** Field description */
  public static final String RESOURCE_LOG = "logs";

  /** Field description */
  public static final String RESOURCE_PAGES = "pages";

  /** Field description */
  public static final String RESOURCE_PLUGINSTORE = "plugins";

  /** Field description */
  public static String RESOURCE_TEMP = "temp";

  /*
   * Config Parameters
   */

  /** Field description */
  public static final char[] SECRET_KEY =
  {
    'L', 'W', '/', '!', 'C', 'b', 'X', ']', 'R', 'f', '4', 'B', 'H', 'd', 'I',
    '['
  };

  /*
   * Services
   */

  /** Field description */
  public static final String SERVCIE_CIPHER = "/app/cipher";

  /** Field description */
  public static final String SERVCIE_DAO = "/app/dao";

  /** Field description */
  public static final String SERVCIE_ENCRYPTION = "/app/encryption";

  /** Field description */
  public static final String SERVCIE_GLOBALCONFIGPROVIDER = "/config/global";

  /** Field description */
  public static final String SERVCIE_GLOBALSTATUSROVIDER = "/status/global";

  /** Field description */
  public static final String SERVICE_ATTACHMENTHANDLER =
    "/editor/attachmentHandler";

  /** Field description */
  public static final String SERVICE_AUTHENTICATION = "/app/authentication";

  /** Field description */
  public static final String SERVICE_CONTEXTLISTENER = "/contextListener";

  /** Field description */
  public static final String SERVICE_DASHBOARDWIDGET = "/dashboard/widget";

  /** Field description */
  public static final String SERVICE_INJECTIONPROVIDER =
    "/app/injectionProvider";

  /** Field description */
  public static final String SERVICE_INSTALLATIONLISTENER =
    "/listener/installation";

  /** Field description */
  public static final TimeZone DEFAULT_TIMEZONE =
    TimeZone.getTimeZone("Europe/Paris");

  /** Field description */
  public static final Locale DEFAULT_LOCALE = Locale.GERMAN;

  /** Field description */
  public static final String SERVICE_LINKBUILDER = "/app/linkBuilder";

  /** Field description */
  public static final String SERVICE_MAPPINGHANDLER = "/app/mappingHandler";

  /** Field description */
  public static final String SERVICE_REQUESTLISTENER = "/listener/request";

  /** Field description */
  public static final String SERVICE_SEARCHCONTEXT = "/app/searchContext";

  /** Field description */
  public static final String SERVICE_SPAMCHECK = "/app/spam/check";

  /** Field description */
  public static final String SERVICE_SPAMPROTECTIONMETHOD = "/app/spam/input";

  /** Field description */
  public static final String SERVICE_SSOAUTHENTICATION = "/app/sso";

  /** Field description */
  public static final String SSOLOGINMODULE_NAME = "BlogSSOLoginModule";

  /** Field description */
  public static final int SSO_DISABLED = 0;

  /** Field description */
  public static final int SSO_EVERYREQUEST = 2;

  /** Field description */
  public static final int SSO_ONEPERSESSION = 1;

  /** Field description */
  public static final String TEMPLATE_DETAIL = "detail.xhtml";

  /** Field description */
  public static final String TEMPLATE_ERROR = "error.xhtml";

  /** Field description */
  public static final String TEMPLATE_LIST = "list.xhtml";

  /** Field description */
  public static final String TEMPLATE_PAGE = "page.xhtml";
}
