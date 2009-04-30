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
public class OracleProfile implements DatabaseProfile
{

  /** Field description */
  public static final String PATH =
    "/sonia/blog/dao/jpa/profile/create-oracle.ddl";

  /** Field description */
  private static final String DISPLAYNAME = "Oracle";

  /** Field description */
  private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

  /** Field description */
  private static final String NAME = "oracle";

  /** Field description */
  private static final String URL = "jdbc:oracle:thin:@127.0.0.1:1521:jab";

  /** Field description */
  private static final String USER = "dba";

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
