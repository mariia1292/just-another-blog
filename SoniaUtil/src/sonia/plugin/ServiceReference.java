/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class ServiceReference
{

  /**
   * Constructs ...
   *
   *
   */
  public ServiceReference()
  {
    this.serviceImplementationList = new ArrayList<Object>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param impl
   *
   * @return
   */
  public ServiceReference addImplementation(Object impl)
  {
    this.serviceImplementationList.add(impl);

    return this;
  }

  /**
   * Method description
   *
   *
   * @param impl
   *
   * @return
   */
  public ServiceReference removeImplementation(Object impl)
  {
    this.serviceImplementationList.remove(impl);

    return this;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Object getImplementation()
  {
    return serviceImplementationList.get(0);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List getImplementations()
  {
    return serviceImplementationList;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Object> serviceImplementationList;
}
