/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.logging.Logger;

import javax.servlet.ServletException;

/**
 *
 * @author sdorra
 */
public abstract class FinalMapping implements Mapping
{

  /** Field description */
  protected static Logger logger;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public FinalMapping()
  {
    logger = Logger.getLogger(getClass().getName());
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  protected abstract void handleFinalMapping(BlogRequest request,
          BlogResponse response, String[] param)
          throws IOException, ServletException;

  /**
   * Method description
   *
   *
   * @param request
   * @param resonse
   * @param param
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public boolean handleMapping(BlogRequest request, BlogResponse resonse,
                               String[] param)
          throws IOException, ServletException
  {
    handleFinalMapping(request, resonse, param);

    return false;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingNavigation getMappingNavigation()
  {
    return null;
  }
}
