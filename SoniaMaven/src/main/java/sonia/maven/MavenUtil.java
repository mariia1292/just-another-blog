/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.maven;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class MavenUtil
{

  /**
   * Method description
   *
   *
   * @param parts
   *
   * @return
   */
  public static String buildClasspath(String[] parts)
  {
    StringBuffer buffer = new StringBuffer();

    for (int i = 0; i < parts.length; i++)
    {
      if (i > 0)
      {
        buffer.append(":");
      }

      buffer.append(parts[i]);
    }

    return buffer.toString();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param baseDir
   * @param includes
   * @param excludes
   *
   * @return
   */
  public static List<File> getFiles(File baseDir, String[] includes,
                                    String[] excludes)
  {
    preparePathParameter(includes);

    if ((excludes != null) && (excludes.length > 0))
    {
      preparePathParameter(excludes);
    }

    String basepath = baseDir.getPath();
    List<File> files = new ArrayList<File>();

    walkTree(files, basepath, baseDir, includes, excludes);

    return files;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param params
   */
  private static void preparePathParameter(String[] params)
  {
    String s = File.separator;

    if (s.equals("\\"))
    {
      s = "\\\\";
    }

    for (int i = 0; i < params.length; i++)
    {
      params[i] = params[i].replaceAll("\\*", "[^/]*").replaceAll("/", s);
    }
  }

  /**
   * Method description
   *
   *
   * @param files
   * @param basepath
   * @param directory
   * @param includes
   * @param excludes
   */
  private static void walkTree(List<File> files, String basepath,
                               File directory, String[] includes,
                               String[] excludes)
  {
    File[] children = directory.listFiles();

    for (File child : children)
    {
      if (child.isDirectory())
      {
        walkTree(files, basepath, child, includes, excludes);
      }
      else
      {
        String path = child.getPath().substring(basepath.length());
        boolean include = false;

        for (String inc : includes)
        {
          if (path.matches(inc))
          {
            include = true;

            break;
          }
        }

        if (include)
        {
          if (excludes != null)
          {
            for (String exc : excludes)
            {
              if (path.matches(exc))
              {
                include = false;

                break;
              }
            }
          }

          if (include)
          {
            files.add(child);
          }
        }
      }
    }
  }
}
