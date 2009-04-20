/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.jmx.SessionInformation;

import sonia.plugin.service.Service;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public class GlobalStatusBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public GlobalStatusBean()
  {
    super();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public SessionInformation getInformation()
  {
    return information;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<String> getStatusProviders()
  {
    return statusProviders;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private SessionInformation information;

  /** Field description */
  @Service(Constants.SERVCIE_GLOBALSTATUSROVIDER)
  private List<String> statusProviders;
}
