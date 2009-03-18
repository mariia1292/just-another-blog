/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.devel;

/**
 *
 * @author sdorra
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
