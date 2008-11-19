/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author sdorra
 */
public interface StoreableConfiguration extends ModifyableConfiguration
{

  /**
   * Method description
   *
   *
   * @param out
   *
   * @throws IOException
   */
  public void store(OutputStream out) throws IOException;
}
