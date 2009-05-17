/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa.profile;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

/**
 *
 * @author sdorra
 */
public class DatabaseProfile
{

  /** Field description */
  private static final String PROPERTY_CREATIONCOMMANDS = "creationCommands";

  /** Field description */
  private static final String PROPERTY_DISPLAYNAME = "displayName";

  /** Field description */
  private static final String PROPERTY_DRIVER = "driver";

  /** Field description */
  private static final String PROPERTY_NAME = "name";

  /** Field description */
  private static final String PROPERTY_SAMPLEURL = "sample.url";

  /** Field description */
  private static final String PROPERTY_SAMPLEUSER = "sample.user";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param properties
   */
  public DatabaseProfile(Properties properties)
  {
    name = properties.getProperty(PROPERTY_NAME);
    displayName = properties.getProperty(PROPERTY_DISPLAYNAME);
    driver = properties.getProperty(PROPERTY_DRIVER);
    creationCommands = properties.getProperty(PROPERTY_CREATIONCOMMANDS);
    sampleUrl = properties.getProperty(PROPERTY_SAMPLEURL);
    sampleUser = properties.getProperty(PROPERTY_SAMPLEUSER);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   *
   * @throws IOException
   */
  public static DatabaseProfile createProfile(String path) throws IOException
  {
    DatabaseProfile profile = null;
    InputStream in = null;

    try
    {
      in = Util.findResource(path);

      if (in != null)
      {
        Properties properties = new Properties();

        properties.load(in);
        profile = new DatabaseProfile(properties);
      }
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }
    }

    return profile;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public InputStream getCreationCommands()
  {
    return Util.findResource(creationCommands);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayName()
  {
    return displayName;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDriver()
  {
    return driver;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSampleUrl()
  {
    return sampleUrl;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSampleUser()
  {
    return sampleUser;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String creationCommands;

  /** Field description */
  private String displayName;

  /** Field description */
  private String driver;

  /** Field description */
  private String name;

  /** Field description */
  private String sampleUrl;

  /** Field description */
  private String sampleUser;
}
