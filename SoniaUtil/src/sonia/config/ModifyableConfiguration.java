/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

/**
 *
 * @author sdorra
 */
public interface ModifyableConfiguration extends Configuration
{

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void addListener(ConfigurationListener listener);

  /**
   * Method description
   *
   *
   * @param key
   */
  public void remove(String key);

  /**
   * Method description
   *
   *
   * @param listener
   */
  public void removeListener(ConfigurationListener listener);

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param object
   */
  public void set(String key, Object object);

  /**
   * Method description
   *
   *
   * @param key
   * @param object
   */
  public void setMulti(String key, Object[] object);
}
