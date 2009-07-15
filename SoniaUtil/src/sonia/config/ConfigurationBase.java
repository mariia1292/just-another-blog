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


package sonia.config;

//~--- non-JDK imports --------------------------------------------------------

import sonia.security.cipher.Cipher;

//~--- JDK imports ------------------------------------------------------------

import java.util.regex.Pattern;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class ConfigurationBase implements Configuration
{

  /** Field description */
  protected static Pattern variablePattern =
    Pattern.compile("\\$\\{([a-zA-Z0-9-\\._]+)\\}");

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Boolean getBoolean(String key, Boolean def)
  {
    Boolean result = getBoolean(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Boolean[] getBooleans(String key, Boolean[] def)
  {
    Boolean[] result = getBooleans(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Double getDouble(String key, Double def)
  {
    Double result = getDouble(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Double[] getDoubles(String key, Double[] def)
  {
    Double[] result = getDoubles(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ExceptionHandler getExceptionHandler()
  {
    if (exceptionHandler == null)
    {
      exceptionHandler = new DefaultExceptionHandler();
    }

    return exceptionHandler;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Float getFloat(String key, Float def)
  {
    Float result = getFloat(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Float[] getFloats(String key, Float[] def)
  {
    Float[] result = getFloats(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Integer getInteger(String key, Integer def)
  {
    Integer result = getInteger(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Integer[] getIntegers(String key, Integer[] def)
  {
    Integer[] result = getIntegers(key);

    if (result == null)
    {
      result = getIntegers(key);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Long getLong(String key, Long def)
  {
    Long result = getLong(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Long[] getLongs(String key, Long[] def)
  {
    Long[] result = getLongs(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Object getObject(String key, Object def)
  {
    Object result = getObject(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param clazz
   * @param key
   * @param def
   * @param <T>
   *
   * @return
   */
  public <T> T getObject(Class<T> clazz, String key, T def)
  {
    T result = getObject(clazz, key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public Object[] getObjects(String key, Object[] def)
  {
    Object[] result = getObjects(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param clazz
   * @param key
   * @param def
   * @param <T>
   *
   * @return
   */
  public <T> T[] getObjects(Class<T> clazz, String key, T[] def)
  {
    T[] result = getObjects(clazz, key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public String getString(String key, String def)
  {
    String result = getString(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param key
   * @param def
   *
   * @return
   */
  public String[] getStrings(String key, String[] def)
  {
    String[] result = getStrings(key);

    if (result == null)
    {
      result = def;
    }

    return result;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cipher
   */
  public void setCipher(Cipher cipher)
  {
    this.cipher = cipher;
  }

  /**
   * Method description
   *
   *
   * @param exceptionHandler
   */
  public void setExceptionHandler(ExceptionHandler exceptionHandler)
  {
    this.exceptionHandler = exceptionHandler;
  }

  /**
   * Method description
   *
   *
   * @param variableResolver
   */
  public void setVariableResolver(VariableResolver variableResolver)
  {
    this.variableResolver = variableResolver;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Cipher cipher;

  /** Field description */
  protected ExceptionHandler exceptionHandler;

  /** Field description */
  protected VariableResolver variableResolver;
}