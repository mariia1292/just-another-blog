/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.cli;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Field;

/**
 *
 * @author sdorra
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
