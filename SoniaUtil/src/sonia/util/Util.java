/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author sdorra
 */
public class Util
{

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   *
   * @throws IOException
   */
  public static void copy(InputStream in, OutputStream out) throws IOException
  {
    byte[] buffer = new byte[0xFFFF];

    for (int len; (len = in.read(buffer)) != -1; )
    {
      out.write(buffer, 0, len);
    }
  }

  /**
   * Method description
   *
   *
   * @param resource
   *
   * @return
   */
  public static InputStream findResource(String resource)
  {
    InputStream in = Util.class.getResourceAsStream(resource);

    if (in == null)
    {
      in = Util.class.getClassLoader().getResourceAsStream(resource);
    }

    return in;
  }
}
