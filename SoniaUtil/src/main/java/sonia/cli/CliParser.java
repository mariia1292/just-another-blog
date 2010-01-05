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



package sonia.cli;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Field;

/**
 *
 * @author Sebastian Sdorra
 */
public class CliParser
{

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public String createHelp(Object object)
  {
    String s = System.getProperty("line.separator");
    StringBuffer result = new StringBuffer();
    Field[] fields = object.getClass().getDeclaredFields();

    for (Field field : fields)
    {
      Argument argument = field.getAnnotation(Argument.class);

      if (argument != null)
      {
        String name = argument.value();
        String longName = argument.longName();
        String description = argument.description();

        result.append("-").append(name);

        if (longName.length() > 0)
        {
          result.append(",").append(longName);
        }

        if (description.length() > 0)
        {
          result.append("\t\t").append(description);
        }

        result.append(s);
      }
    }

    return result.toString();
  }

  /**
   * Method description
   *
   *
   * @param object
   * @param arguments
   *
   * @throws CliException
   */
  public void parse(Object object, String[] arguments) throws CliException
  {
    Field[] fields = object.getClass().getDeclaredFields();
    int length = arguments.length;

    for (Field field : fields)
    {
      Argument argument = field.getAnnotation(Argument.class);

      if (argument != null)
      {
        String name = "-" + argument.value();
        String longName = "--" + argument.longName();
        boolean found = false;

        for (int i = 0; i < length; i++)
        {
          if (arguments[i].equals(name)
              || (!longName.equals("--") && arguments[i].equals(longName)))
          {
            found = true;

            if (field.getType().isAssignableFrom(Boolean.class))
            {
              setArgument(object, field, Boolean.TRUE);
            }
            else if (i + 1 < length)
            {
              setArgument(object, field,
                          Util.convertString(field.getType(),
                                             arguments[i + 1]));
            }
            else
            {
              throw new CliException("missing parameter for " + name);
            }
          }
        }

        if (!found && argument.required())
        {
          throw new CliRequiredException(name + " is required");
        }
      }
    }
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param object
   * @param field
   * @param value
   *
   * @throws CliException
   */
  private void setArgument(Object object, Field field, Object value)
          throws CliException
  {
    try
    {
      boolean modifyAccess = false;

      if (!field.isAccessible())
      {
        field.setAccessible(true);
        modifyAccess = true;
      }

      field.set(object, value);

      if (modifyAccess)
      {
        field.setAccessible(false);
      }
    }
    catch (Exception ex)
    {
      throw new CliException(ex);
    }
  }
}
