/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.URL;
import java.net.URLConnection;

import java.text.NumberFormat;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class Util
{

  /** Field description */
  public static final int MILLIS_DAY = 1000 * 60 * 60 * 24;

  /** Field description */
  public static final int MILLIS_HOUR = 1000 * 60 * 60;

  /** Field description */
  public static final int MILLIS_MINUTE = 1000 * 60;

  /** Field description */
  public static final int MILLIS_SECOND = 1000;

  /** Field description */
  private static Logger logger = Logger.getLogger(Util.class.getName());

  //~--- methods --------------------------------------------------------------

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
   * @param collection
   * @param <T>
   *
   * @return
   */
  public static <T> Set<T> createSet(Collection<T> collection)
  {
    Set<T> set = new HashSet<T>();

    for (T o : collection)
    {
      if (!set.add(o) && logger.isLoggable(Level.FINEST))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("remove duplicate ").append(o);
        logger.finest(msg.toString());
      }
    }

    return set;
  }

  /**
   * Method description
   *
   *
   * @param text
   * @param delimiter
   *
   * @return
   */
  public static Set<String> createSet(String text, String delimiter)
  {
    Set<String> set = new HashSet<String>();
    String[] items = text.split(delimiter);

    for (String o : items)
    {
      o = o.trim();
      set.add(o);
    }

    return set;
  }

  /**
   * Method description
   *
   *
   * @param array
   * @param <T>
   *
   * @return
   */
  public static <T> Set<T> createSet(T[] array)
  {
    Set<T> set = new HashSet<T>();

    for (T o : array)
    {
      if (!set.add(o) && logger.isLoggable(Level.FINEST))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("remove duplicate ").append(o);
        logger.finest(msg.toString());
      }
    }

    return set;
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

  /**
   * Method description
   *
   *
   * @param time
   *
   * @return
   */
  public static String formatTime(long time)
  {
    int days = (int) (time / MILLIS_DAY);

    time = time - (days * MILLIS_DAY);

    int hours = (int) (time / MILLIS_HOUR);

    time = time - (hours * MILLIS_HOUR);

    int min = (int) (time / MILLIS_MINUTE);

    time = time - (min * MILLIS_MINUTE);

    int sec = (int) (time / MILLIS_SECOND);

    time = time - (sec * MILLIS_SECOND);
    time = (int) time;

    StringBuffer out = new StringBuffer();

    out.append(days).append("d ");

    if (hours < 10)
    {
      out.append('0');
    }

    out.append(hours).append(":");

    if (min < 10)
    {
      out.append('0');
    }

    out.append(min).append(':');

    if (sec < 10)
    {
      out.append('0');
    }

    out.append(sec).append('.');

    if (time < 10)
    {
      out.append('0').append('0');
    }
    else if (time < 100)
    {
      out.append('0');
    }

    out.append(time);

    return out.toString();
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
   * @param filename
   *
   * @return
   */
  public static String getExtension(String filename)
  {
    String ext = null;

    if (!isBlank(filename))
    {
      int index = filename.lastIndexOf(".");

      if (index > 0)
      {
        ext = filename.substring(index + 1);
      }
    }

    return ext;
  }

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   */
  public static String getExtension(File file)
  {
    return getExtension(file.getName());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public static File getHomeDirectory()
  {
    return new File(System.getProperty("user.home"));
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
   * @param file
   *
   * @return
   *
   * @throws IOException
   */
  public static String getTextFromFile(File file) throws IOException
  {
    StringBuffer buffer = new StringBuffer();
    BufferedReader reader = null;

    try
    {
      reader = new BufferedReader(new FileReader(file));

      String line = reader.readLine();

      while (line != null)
      {
        buffer.append(line).append("\n");
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

    return buffer.toString();
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  public static boolean hasContent(String value)
  {
    return (value != null) && (value.length() > 0);
  }

  /**
   * Method description
   *
   *
   * @param collection
   *
   * @return
   */
  public static boolean hasContent(Collection<?> collection)
  {
    return (collection != null) &&!collection.isEmpty();
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

  /**
   * Method description
   *
   *
   * @param collection
   *
   * @return
   */
  public static boolean isEmpty(Collection<?> collection)
  {
    return (collection == null) || collection.isEmpty();
  }
}
