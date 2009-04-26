/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.dao.jpa.profile;

//~--- JDK imports ------------------------------------------------------------

import java.io.InputStream;

/**
 *
 * @author sdorra
 */
public interface DatabaseProfile
{

  /**
   * Method description
   *
   *
   * @return
   */
  public InputStream getCreationCommands();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayName();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSampleDriver();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSampleUrl();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSampleUser();
}
