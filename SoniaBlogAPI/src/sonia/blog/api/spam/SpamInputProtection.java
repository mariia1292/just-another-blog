/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.spam;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.Serializable;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public interface SpamInputProtection extends Serializable
{

  /**
   * Method description
   *
   *
   * @param writer
   *
   * @return
   */
  public String renderInput(ResponseWriter writer) throws IOException;
}
