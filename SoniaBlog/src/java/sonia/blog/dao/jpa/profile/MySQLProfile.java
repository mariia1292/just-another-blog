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
public class MySQLProfile implements DatabaseProfile
{

  /** Field description */
  public static final String DRIVER = "com.mysql.jdbc.Driver";

  /** Field description */
  public static final String NAME = "mysql";

  /** Field description */
  public static final String PATH =
    "/sonia/blog/dao/jpa/profile/create-mysql.ddl";

  /** Field description */
  public static final String URL = "jdbc:mysql://localhost:3306/soniablog";

  /** Field description */
  public static final String USER = "root";

  /** Field description */
  private static final String DISPLAYNAME = "MySQL";

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
    return DISPLAYNAME;
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
