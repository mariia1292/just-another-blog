/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.resolver;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;

//~--- JDK imports ------------------------------------------------------------

import com.sun.facelets.impl.DefaultResourceResolver;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class ResourceResolver extends DefaultResourceResolver
{

  /** Field description */
  private static final String PREFIX_TEMPLATE = "/custom-template/";

  /** Field description */
  private static final String PREFIX_VIEW = "/view/";

  /** Field description */
  private static final String URL_PREFIX = "file://";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ResourceResolver.class.getName());

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

    if (path.startsWith(PREFIX_VIEW))
    {
      url = getClass().getResource("/jab" + path);
    }
    else if (path.startsWith(PREFIX_TEMPLATE))
    {
      File dir = getTemplateDirectory();

      if ((dir != null) && dir.exists())
      {
        String filePath = path.substring(PREFIX_TEMPLATE.length());
        File file = new File(dir, filePath);

        if (file.exists())
        {
          try
          {
            url = new URL(URL_PREFIX + file.getAbsolutePath());
          }
          catch (MalformedURLException ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }
        }
      }
    }
    else
    {
      url = super.resolveUrl(path);
    }

    return url;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private File getTemplateDirectory()
  {
    if (templateDirectory == null)
    {
      ResourceManager resManager =
        BlogContext.getInstance().getResourceManager();

      if (resManager != null)
      {
        templateDirectory =
          resManager.getDirectory(Constants.RESOURCE_TEMPLATE);
      }
    }

    return templateDirectory;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File templateDirectory;
}
