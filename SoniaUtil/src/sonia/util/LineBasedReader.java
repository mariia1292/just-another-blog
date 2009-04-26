/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author sdorra
 */
public abstract class LineBasedReader
{

  /**
   * Method description
   *
   *
   * @param line
   */
  public abstract void invoke(String line);

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  public void readLines(InputStream in) throws IOException
  {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line = reader.readLine();

    while (line != null)
    {
      line = line.trim();

      if (Util.hasContent(line))
      {
        if (!line.startsWith("#"))
        {
          int i = line.indexOf("#");

          if (i > 0)
          {
            line = line.substring(0, i).trim();
          }

          invoke(line);
        }
      }

      line = reader.readLine();
    }
  }
}
