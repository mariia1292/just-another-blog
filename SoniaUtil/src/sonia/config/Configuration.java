/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.util.Set;

/**
 *
 * @author sdorra
 */
public interface Configuration
{

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public boolean contains(String key);

  /**
   * Method description
   *
   *
   * @return
   */
  public Set<String> keySet();

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Boolean getBoolean(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Boolean getBoolean(String key, Boolean def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Boolean[] getBooleans(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Boolean[] getBooleans(String key, Boolean[] def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Double getDouble(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Double getDouble(String key, Double def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Double[] getDoubles(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Double[] getDoubles(String key, Double[] def);

  /**
   * Method description
   *
   *
   * @return
   */
  public ExceptionHandler getExceptionHandler();

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Float getFloat(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Float getFloat(String key, Float def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Float[] getFloats(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Float[] getFloats(String key, Float[] def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Integer getInteger(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Integer getInteger(String key, Integer def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Integer[] getIntegers(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Integer[] getIntegers(String key, Integer[] def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Long getLong(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Long getLong(String key, Long def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public Long[] getLongs(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Long[] getLongs(String key, Long[] def);

  /**
   * Method description
   *
   *
   * @param clazz
   * @param key
   *
   * @return
   */
  public <T>T getObject(Class<T> clazz, String key);

  /**
   * Method description
   *
   *
   * @param clazz
   * @param key
   * @param def
   *
   * @return
   */
  public <T>T getObject(Class<T> clazz, String key, T def);

  /**
   * Method description
   *
   *
   * @param clazz
   * @param key
   *
   * @return
   */
  public <T>T[] getObjects(Class<T> clazz, String key);

  /**
   * Method description
   *
   *
   * @param clazz
   * @param key
   * @param def
   *
   * @return
   */
  public <T>T[] getObjects(Class<T> clazz, String key, T[] def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String getString(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public String getString(String key, String def);

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   */
  public String[] getStrings(String key);

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public String[] getStrings(String key, String[] def);

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isEmpty();

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param exceptionHandler
   */
  public void setExceptionHandler(ExceptionHandler exceptionHandler);
}
