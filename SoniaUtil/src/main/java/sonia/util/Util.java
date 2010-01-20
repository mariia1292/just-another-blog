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



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import java.math.BigInteger;

import java.net.URL;
import java.net.URLConnection;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author Sebastian Sdorra
 */
public class Util
{

  /** Field description */
  public static final long MILLIS_DAY = 1000 * 60 * 60 * 24;

  /** Field description */
  public static final long MILLIS_HOUR = 1000 * 60 * 60;

  /** Field description */
  public static final long MILLIS_MINUTE = 1000 * 60;

  /** Field description */
  public static final long MILLIS_SECOND = 1000;

  /** Field description */
  private static Logger logger = Logger.getLogger(Util.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param type
   * @param value
   *
   * @return
   */
  public static Object convertString(Class<?> type, String value)
  {
    Object result = null;

    try
    {
      if (type.isAssignableFrom(String.class))
      {
        result = value;
      }
      else if (type.isAssignableFrom(Short.class))
      {
        result = Short.parseShort(value);
      }
      else if (type.isAssignableFrom(Integer.class))
      {
        result = Integer.parseInt(value);
      }
      else if (type.isAssignableFrom(Long.class))
      {
        result = Long.parseLong(value);
      }
      else if (type.isAssignableFrom(BigInteger.class))
      {
        result = new BigInteger(value);
      }
      else if (type.isAssignableFrom(Float.class))
      {
        result = Float.parseFloat(value);
      }
      else if (type.isAssignableFrom(Double.class))
      {
        result = Double.parseDouble(value);
      }
      else if (type.isAssignableFrom(Boolean.class))
      {
        result = Boolean.parseBoolean(value);
      }
    }
    catch (NumberFormatException ex)
    {
      logger.log(Level.FINER, null, ex);
    }

    return result;
  }

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
   * @param content
   *
   * @return
   */
  public static String extractHTMLText(String content)
  {
    try
    {
      EditorKit kit = new HTMLEditorKit();
      javax.swing.text.Document doc = kit.createDefaultDocument();

      doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

      Reader reader = new StringReader(content);

      kit.read(reader, doc, 0);
      content = doc.getText(0, doc.getLength());
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return content;
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

  /**
   * Method description
   *
   *
   * @param collection
   * @param <T>
   *
   * @return
   */
  public static <T> List<T> unique(Collection<T> collection)
  {
    List<T> resultList = new ArrayList<T>();

    for (T item : collection)
    {
      if (!resultList.contains(item))
      {
        resultList.add(item);
      }
    }

    return resultList;
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
    return getContent(url.openConnection());
  }

  /**
   * Method description
   *
   *
   *
   * @param conn
   *
   * @return
   *
   * @throws IOException
   */
  public static String getContent(URLConnection conn) throws IOException
  {
    StringBuffer result = null;
    String type = conn.getContentType();

    if ((type != null) && type.toLowerCase().startsWith("text"))
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
   * @param throwable
   *
   * @return
   */
  public static String getStacktraceAsString(Throwable throwable)
  {
    StringWriter writer = new StringWriter();
    PrintWriter printWriter = new PrintWriter(writer);

    appendStacktrace(printWriter, throwable);

    return writer.toString();
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
   * @param map
   *
   * @return
   */
  public static boolean hasContent(Map<?, ?> map)
  {
    return (map != null) &&!map.isEmpty();
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
   * @param value
   *
   * @return
   */
  public static boolean isEmpty(String value)
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

  /**
   * Method description
   *
   *
   * @param map
   *
   * @return
   */
  public static boolean isEmpty(Map<?, ?> map)
  {
    return (map == null) || map.isEmpty();
  }

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  public static boolean isNotEmpty(String value)
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
  public static boolean isNotEmpty(Collection<?> collection)
  {
    return (collection != null) &&!collection.isEmpty();
  }

  /**
   * Method description
   *
   *
   * @param map
   *
   * @return
   */
  public static boolean isNotEmpty(Map<?, ?> map)
  {
    return (map != null) &&!map.isEmpty();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param writer
   * @param throwable
   */
  private static void appendStacktrace(PrintWriter writer, Throwable throwable)
  {
    if (throwable != null)
    {
      throwable.printStackTrace(writer);

      Throwable cause = throwable.getCause();

      if (cause != null)
      {
        appendStacktrace(writer, cause);
      }
    }
  }
}
