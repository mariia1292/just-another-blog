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



package sonia.cache;

/**
 *
 * @author Sebastian Sdorra
 *
 */
public class CacheObject
{

  /**
   * Constructs ...
   *
   *
   * @param object
   */
  public CacheObject(Object object)
  {
    this.object = object;
    this.time = System.currentTimeMillis();
    this.lastAccess = 0;
    this.hits = 0;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param obj
   *
   * @return
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object obj)
  {
    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    final CacheObject other = (CacheObject) obj;

    if ((this.object != other.object)
        && ((this.object == null) ||!this.object.equals(other.object)))
    {
      return false;
    }

    return true;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public int hashCode()
  {
    int hash = 7;

    hash = 17 * hash + ((this.object != null)
                        ? this.object.hashCode()
                        : 0);

    return hash;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public long getHits()
  {
    return hits;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getLastAccess()
  {
    return lastAccess;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Object getObject()
  {
    return object;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTime()
  {
    return time;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  void update()
  {
    lastAccess = System.currentTimeMillis();
    hits++;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private long hits;

  /** Field description */
  private long lastAccess;

  /** Field description */
  private Object object;

  /** Field description */
  private long time;
}
