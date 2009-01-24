/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

/**
 *
 * @author sdorra
 */
public class InitParamConfiguration extends StringBasedConfiguration
{

  /**
   * Constructs ...
   *
   *
   * @param context
   */
  public InitParamConfiguration(ServletContext context)
  {
    this.context = context;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public boolean contains(String key)
  {
    return getString(key) != null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<String> keySet()
  {
    Set<String> keys = new HashSet<String>();
    Enumeration enm = context.getInitParameterNames();

    while (enm.hasMoreElements())
    {
      keys.add((String) enm.nextElement());
    }

    return keys;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String getString(String key)
  {
    return context.getInitParameter(key);
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String[] getStrings(String key)
  {
    String[] result = null;
    String value = getString(key);

    if (value != null)
    {
      result = value.split(";");
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEmpty()
  {
    Enumeration enm = context.getInitParameterNames();

    return (enm != null) && enm.hasMoreElements();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServletContext context;
}
