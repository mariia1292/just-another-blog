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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class ServiceLocator
{

  /**
   * Method description
   *
   *
   * @param serviceClass
   *
   * @return
   */
  public static <T>T locateService(Class<T> serviceClass)
  {
    return locateService(serviceClass, null);
  }

  /**
   * Method description
   *
   *
   * @param serviceClass
   * @param def
   *
   * @return
   */
  public static <T>T locateService(Class<T> serviceClass, T def)
  {
    T service = null;

    try
    {
      BufferedReader reader = createReader(serviceClass);

      if (reader != null)
      {
        String line = reader.readLine();

        while ((line != null) && (service == null))
        {
          line = line.trim();

          if (!line.startsWith("#"))
          {
            Object obj = Class.forName(line).newInstance();
            service = serviceClass.cast(obj);
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    if (service == null)
    {
      service = def;
    }

    return service;
  }

  /**
   * Method description
   *
   *
   * @param serviceClass
   *
   * @return
   */
  public static <T>List<T> locateServices(Class<T> serviceClass)
  {
    return locateServices(serviceClass, null);
  }

  /**
   * Method description
   *
   *
   * @param serviceClass
   * @param def
   *
   * @return
   */
  public static <T>List<T> locateServices(Class<T> serviceClass, List<T> def)
  {
    List<T> resultList = null;

    try
    {
      BufferedReader reader = createReader(serviceClass);

      if (reader != null)
      {
        resultList = new ArrayList<T>();

        String line = reader.readLine();

        while (line != null)
        {
          line = line.trim();

          if (!line.startsWith("#"))
          {
            try
            {
              Object obj = Class.forName(line).newInstance();
              resultList.add( serviceClass.cast(obj) );
            }
            catch (Exception ex)
            {
              ex.printStackTrace();
            }
          }

          line = reader.readLine();
        }
      }
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }

    if ((resultList == null) || resultList.isEmpty())
    {
      resultList = def;
    }

    return resultList;
  }

  /**
   * Method description
   *
   *
   * @param serviceClass
   *
   * @return
   */
  private static BufferedReader createReader(Class serviceClass)
  {
    BufferedReader reader = null;
    InputStream in = Util.findResource("/META-INF/services/"
                                       + serviceClass.getName());

    if (in != null)
    {
      reader = new BufferedReader(new InputStreamReader(in));
    }

    return reader;
  }
}
