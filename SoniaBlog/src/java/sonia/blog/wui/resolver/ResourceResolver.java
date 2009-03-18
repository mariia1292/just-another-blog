/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.resolver;

//~--- JDK imports ------------------------------------------------------------

import com.sun.facelets.impl.DefaultResourceResolver;

import java.net.URL;

/**
 *
 * @author sdorra
 */
public class ResourceResolver extends DefaultResourceResolver
{

  /** Field description */
  private static final String PREFIX = "/view/";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   */
  @Override
  public URL resolveUrl(String path)
  {
    URL url = null;

    if (path.startsWith(PREFIX))
    {
      url = getClass().getResource("/jab" + path);
    }
    else
    {
      url = super.resolveUrl(path);
    }

    return url;
  }
}
