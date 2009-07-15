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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class ServiceLocator
{

  /**
   * Method description
   *
   *
   * @param serviceClass
   * @param <T>
   *
   * @return
   */
  public static <T> T locateService(Class<T> serviceClass)
  {
    return locateService(serviceClass, null);
  }

  /**
   * Method description
   *
   *
   * @param serviceClass
   * @param def
   * @param <T>
   *
   * @return
   */
  public static <T> T locateService(Class<T> serviceClass, T def)
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
   * @param <T>
   *
   * @return
   */
  public static <T> List<T> locateServices(Class<T> serviceClass)
  {
    return locateServices(serviceClass, null);
  }

  /**
   * Method description
   *
   *
   * @param serviceClass
   * @param def
   * @param <T>
   *
   * @return
   */
  public static <T> List<T> locateServices(Class<T> serviceClass, List<T> def)
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

              resultList.add(serviceClass.cast(obj));
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