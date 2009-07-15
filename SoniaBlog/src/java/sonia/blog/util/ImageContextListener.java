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


package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;

import sonia.image.ImageFileHandler;
import sonia.image.ImageHandler;
import sonia.image.ImageMagickFileHandler;

import sonia.util.ExecUtil;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Sebastian Sdorra
 */
public class ImageContextListener implements ServletContextListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ImageContextListener.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param sce
   */
  public void contextDestroyed(ServletContextEvent sce)
  {

    // do nothing
  }

  /**
   * Method description
   *
   *
   * @param sce
   */
  public void contextInitialized(ServletContextEvent sce)
  {
    ImageFileHandler imageHandler = null;
    BlogContext ctx = BlogContext.getInstance();
    BlogConfiguration config = ctx.getConfiguration();

    try
    {
      if (Util.isBlank(config.getString(Constants.CONFIG_RESIZE_IMAGE)))
      {
        int result = ExecUtil.process("convert --help", 3000l);

        if ((result == 1) || (result == 0))
        {
          imageHandler = new ImageMagickFileHandler();
        }
      }
      else
      {
        imageHandler = (ImageFileHandler) Class.forName(
          config.getString(Constants.CONFIG_RESIZE_IMAGE)).newInstance();

        if (imageHandler instanceof ImageMagickFileHandler)
        {
          ImageMagickFileHandler imfh = (ImageMagickFileHandler) imageHandler;
          String path = config.getString(Constants.CONFIG_COMMAND_RESIZE_IMAGE);

          if (Util.hasContent(path))
          {
            File convertFile = new File(path);

            if (convertFile.exists() && (convertFile != null))
            {
              imfh.setImageMagick(path);
            }

            File identifyFile = new File(convertFile.getParentFile(),
                                         "identify");

            if (identifyFile.exists() && (identifyFile != null))
            {
              imfh.setIdentify(identifyFile.getAbsolutePath());
            }
          }

          Long timeout = config.getLong(Constants.CONFIG_COMMAND_TIMEOUT);

          if (timeout != null)
          {
            imfh.setTimeout(timeout);
          }
        }
      }
    }
    catch (Exception ex)
    {
      if (logger.isLoggable(Level.WARNING))
      {
        logger.log(Level.WARNING, null, ex);
      }
    }

    if (imageHandler == null)
    {
      imageHandler = ImageHandler.getImageFileHandler();
    }

    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer log = new StringBuffer();

      log.append("configure image converter to ").append(
          imageHandler.getClass().getName());
      logger.info(log.toString());
    }

    ctx.setImageHandler(imageHandler);
  }
}