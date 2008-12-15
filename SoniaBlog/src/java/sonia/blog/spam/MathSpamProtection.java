/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.spam.SpamInputProtection;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Random;

import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public class MathSpamProtection implements SpamInputProtection
{

  /**
   * Method description
   *
   *
   * @param writer
   *
   * @return
   *
   * @throws IOException
   */
  public String renderInput(ResponseWriter writer) throws IOException
  {
    Random r = new Random();
    int n1 = r.nextInt(20);
    int n2 = r.nextInt(20);

    writer.write(n1 + " + " + n2);

    return "" + (n1 + n2);
  }
}
