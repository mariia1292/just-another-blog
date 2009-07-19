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



package sonia.blog.devel;

/**
 *
 * @author Sebastian Sdorra
 */
public class RequestStatisticInformation
        implements Comparable<RequestStatisticInformation>
{

  /**
   * Constructs ...
   *
   *
   * @param requestUri
   */
  public RequestStatisticInformation(String requestUri)
  {
    this.requestUri = requestUri;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param loadTime
   */
  public void add(long loadTime)
  {
    this.hits++;
    this.loadTime += loadTime;
  }

  /**
   * Method description
   *
   *
   * @param o
   *
   * @return
   */
  public int compareTo(RequestStatisticInformation o)
  {
    int result = 1;

    if (o != null)
    {
      double r = getAverageLoadTime() - o.getAverageLoadTime();

      result = new Long(Math.round(r)).intValue();
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public double getAverageLoadTime()
  {
    return loadTime / hits;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getHits()
  {
    return hits;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRequestUri()
  {
    return requestUri;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int hits = 0;

  /** Field description */
  private long loadTime = 0l;

  /** Field description */
  private String requestUri;
}
