/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.injection.InjectionProvider;

import sonia.util.ServiceLocator;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public abstract class MacroParser
{

  /** Field description */
  private static MacroParser instance;

  /** Field description */
  private static Logger logger = Logger.getLogger(MacroParser.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static MacroParser getInstance()
  {
    if (instance == null)
    {
      instance = ServiceLocator.locateService(MacroParser.class,
              new RegexMacroParser());
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param environment
   * @param text
   *
   * @return
   */
  public abstract String parseText(Map<String, ?> environment, String text);

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public void load(InputStream in) throws IOException
  {
    Properties props = new Properties();

    props.load(in);

    Set<Object> keys = props.keySet();

    if (keys != null)
    {
      for (Object o : keys)
      {
        try
        {
          String key = (String) o;
          String value = props.getProperty(key);

          putMacro(key, (Class<? extends Macro>) Class.forName(value));
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   *
   * @return
   */
  public ParameterConverter putConverter(Class<?> key, ParameterConverter value)
  {
    return converter.put(key, value);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param macro
   */
  public void putMacro(String name, Class<? extends Macro> macro)
  {
    macros.put(name, macro);
  }

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public ParameterConverter removeConverter(Object key)
  {
    return converter.remove(key);
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void removeMacro(String name)
  {
    macros.remove(name);
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
  public ParameterConverter getConverter(Object key)
  {
    return converter.get(key);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public InjectionProvider getInjectionProvider()
  {
    return injectionProvider;
  }

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public Class<? extends Macro> getMacro(String name)
  {
    return macros.get(name);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param injectionProvider
   */
  public void setInjectionProvider(InjectionProvider injectionProvider)
  {
    this.injectionProvider = injectionProvider;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Map<Class<?>, ParameterConverter> converter =
    new HashMap<Class<?>, ParameterConverter>();

  /** Field description */
  protected InjectionProvider injectionProvider;

  /** Field description */
  protected Map<String, Class<? extends Macro>> macros =
    new HashMap<String, Class<? extends Macro>>();
}
