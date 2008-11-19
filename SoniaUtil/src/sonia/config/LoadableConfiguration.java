/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author sdorra
 */
public interface LoadableConfiguration extends Configuration
{

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  public void load(InputStream in) throws IOException;
}
