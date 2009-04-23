/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class RedirectMacro extends AbstractBlogMacro
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(RedirectMacro.class.getName());

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param url
   */
  public void setUrl(String url)
  {
    this.url = url;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param facesContext
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  @Override
  protected String doBody(FacesContext facesContext, String linkBase,
                          ContentObject object, String body)
  {
    try
    {
      facesContext.getExternalContext().redirect(url);
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return "";
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String url;
}
