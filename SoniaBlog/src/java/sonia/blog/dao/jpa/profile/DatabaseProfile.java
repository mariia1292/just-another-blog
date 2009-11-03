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



package sonia.blog.dao.jpa.profile;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

/**
 *
 * @author Sebastian Sdorra
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
