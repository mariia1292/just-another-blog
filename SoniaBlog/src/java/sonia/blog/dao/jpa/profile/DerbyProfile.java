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
public class DerbyProfile implements DatabaseProfile
{

  /** Field description */
  public static final String DISPLAY_NAME = "Java DB/Derby";

  /** Field description */
  public static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";

  /** Field description */
  public static final String NAME = "derby";

  /** Field description */
  public static final String PATH =
    "/sonia/blog/dao/jpa/profile/create-derby.ddl";

  /** Field description */
  public static final String URL = "jdbc:derby://localhost:1527/SoniaBlog";

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
