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
  public static final String LOGINMODULE_NAME = "BlogLoginModule";

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
  public static final String SERVICE_DEFAULTMAPPING = "/app/default-mapping";

  /** Field description */
  public static final String SERVICE_ENTRY_LISTENER = "/listener/entry";

  /** Field description */
  public static final String SERVICE_LINKBUILDER = "/app/linkBuilder";

  /** Field description */
  public static final String SERVICE_MAPPING = "/app/mapping";

  /** Field description */
  public static final String SERVICE_SEARCHCONTEXT = "/app/searchContext";

  /** Field description */
  public static final String SERVICE_SERVLET = "/servlet";
}
