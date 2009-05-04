/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa.profile;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.InputStream;

/**
 *
 * @author sdorra
 */
public class PostgreSQLProfile implements DatabaseProfile
{

  /** Field description */
  public static final String DISPLAY_NAME = "PostgreSQL";

  /** Field description */
  public static final String DRIVER = "org.postgresql.Driver";

  /** Field description */
  public static final String NAME = "postgresql";

  /** Field description */
  public static final String PATH =
    "/sonia/blog/dao/jpa/profile/create-postgresql.ddl";

  /** Field description */
  public static final String URL = "jdbc:postgresql://127.0.0.1:5432/jab";

  /** Field description */
  public static final String USER = "root";

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public InputStream getCreationCommands()
  {
    return Util.findResource(PATH);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayName()
  {
    return DISPLAY_NAME;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return NAME;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSampleDriver()
  {
    return DRIVER;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSampleUrl()
  {
    return URL;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSampleUser()
  {
    return USER;
  }
}
