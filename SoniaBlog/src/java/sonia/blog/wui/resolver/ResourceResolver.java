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
 * @author Sebastian Sdorra
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
