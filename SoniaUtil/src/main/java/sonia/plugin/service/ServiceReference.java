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



package sonia.plugin.service;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 *
 * @param <T>
 */
public class ServiceReference<T>
{

  /**
   * Constructs ...
   *
   *
   * @param type
   * @param path
   */
  public ServiceReference(Class<T> type, String path)
  {
    this.type = type;
    this.path = path;
    this.implementations = new ArrayList<T>();
  }

  /**
   * Constructs ...
   *
   *
   * @param type
   * @param path
   * @param implementations
   */
  public ServiceReference(Class<T> type, String path, List<T> implementations)
  {
    this.type = type;
    this.path = path;
    this.implementations = implementations;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param implementation
   *
   * @return
   */
  public ServiceReference<T> add(T implementation)
  {
    implementations.add(implementation);

    return this;
  }

  /**
   * Method description
   *
   *
   * @param pos
   * @param implementation
   *
   * @return
   */
  public ServiceReference<T> add(int pos, T implementation)
  {
    implementations.add(pos, implementation);

    return this;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ServiceReference<T> clear()
  {
    implementations.clear();

    return this;
  }

  /**
   * Method description
   *
   *
   * @param implementation
   *
   * @return
   */
  public boolean contains(T implementation)
  {
    return implementations.contains(implementation);
  }

  /**
   * Method description
   *
   *
   * @param implementation
   *
   * @return
   */
  public ServiceReference<T> remove(T implementation)
  {
    implementations.remove(implementation);

    return this;
  }

  /**
   * Method description
   *
   *
   * @param pos
   *
   * @return
   */
  public ServiceReference<T> remove(int pos)
  {
    implementations.remove(pos);

    return this;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param pos
   *
   * @return
   */
  public T get(int pos)
  {
    return implementations.get(pos);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public T get()
  {
    return implementations.get(0);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<T> getAll()
  {
    return implementations;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPath()
  {
    return path;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Class<T> getType()
  {
    return type;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<T> implementations;

  /** Field description */
  private String path;

  /** Field description */
  private Class<T> type;
}
