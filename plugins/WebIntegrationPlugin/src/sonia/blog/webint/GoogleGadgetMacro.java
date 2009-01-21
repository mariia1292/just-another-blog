/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webint;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class GoogleGadgetMacro extends AbstractBlogMacro
{

  /** Field description */
  public static final String NAME = "googleGadget";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param facesContext
   * @param linkBase
   * @param object
   * @param body
   * @param parameters
   *
   * @return
   */
  @Override
  protected String excecute(FacesContext facesContext, String linkBase,
                            ContentObject object, String body,
                            Map<String, String> parameters)
  {
    String result = null;

    if (!parameters.containsKey("url"))
    {
      String url = parameters.get("url");

      if (!Util.isBlank(url))
      {
        result = "<script src=\"" + url + "\"></script>";
      }
      else
      {
        result = "-- parameter value url is blank --";
      }
    }
    else
    {
      result = "-- parameter url is required --";
    }

    return result;
  }
}
