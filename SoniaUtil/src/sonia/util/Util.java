/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.URL;
import java.net.URLConnection;

import java.text.NumberFormat;

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
   * @param file
   */
  public static void delete(File file)
  {
    if (file.isDirectory())
    {
      File[] children = file.listFiles();

      if (children != null)
      {
        for (File child : children)
        {
          delete(child);
        }
      }
    }

    if (!file.delete())
    {
      throw new RuntimeException("could not delete file " + file.getPath());
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

  /**
   * Method description
   *
   *
   * @param size
   *
   * @return
   */
  public static String formatSize(double size)
  {
    String suffix = "B";

    if (size > 1024)
    {
      size /= 1024;
      suffix = "KB";

      if (size > 1024)
      {
        size /= 1024;
        suffix = "MB";

        if (size > 1024)
        {
          size /= 1024;
          suffix = "GB";

          if (size > 1024)
          {
            size /= 1024;
            suffix = "TB";
          }
        }
      }
    }

    NumberFormat nf = NumberFormat.getInstance();

    nf.setMaximumFractionDigits(2);

    return nf.format(size) + " " + suffix;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param url
   *
   * @return
   *
   * @throws IOException
   */
  public static String getContent(URL url) throws IOException
  {
    StringBuffer result = null;
    URLConnection conn = url.openConnection();
    String type = conn.getContentType();

    if (type.toLowerCase().startsWith("text"))
    {
      String encoding = conn.getContentEncoding();
      InputStream in = conn.getInputStream();
      BufferedReader reader = null;

      try
      {
        if (isBlank(encoding))
        {
          reader = new BufferedReader(new InputStreamReader(in));
        }
        else
        {
          reader = new BufferedReader(new InputStreamReader(in, encoding));
        }

        result = new StringBuffer();

        String line = reader.readLine();

        while (line != null)
        {
          result.append(line + "\n");
          line = reader.readLine();
        }
      }
      finally
      {
        if (reader != null)
        {
          reader.close();
        }
      }
    }

    return (result != null)
           ? result.toString()
           : null;
  }

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   */
  public static long getLength(File file)
  {
    long size = 0;

    if (file.isDirectory())
    {
      File[] files = file.listFiles();

      for (File f : files)
      {
        size += getLength(f);
      }
    }
    else
    {
      size = file.length();
    }

    return size;
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  public static boolean isBlank(String value)
  {
    return (value == null) || (value.length() == 0);
  }
}
