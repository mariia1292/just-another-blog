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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.myfaces.custom.fileupload.UploadedFile;

import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.User;

import sonia.config.Config;

import sonia.image.ImageFileHandler;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class AvatarBean extends AbstractBean
{

  /** Field description */
  private static final String AVATARCROP = "avatarCrop";

  /** Field description */
  private static Logger logger = Logger.getLogger(AvatarBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public AvatarBean()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String crop()
  {
    String result = SUCCESS;

    try
    {
      File avatar = getAvatarFile();

      if ((width == 50) && (height == 50))
      {
        imageHandler.cropImage(tempFile, avatar, imageFormat, x, y, width,
                               height);
      }
      else
      {
        File temp = resManager.getTempFile("avatar-", ".crop");

        imageHandler.cropImage(tempFile, temp, imageFormat, x, y, width,
                               height);
        imageHandler.scaleImageFix(temp, avatar, imageFormat, 50, 50);
        temp.delete();
      }

      updateAvatar(avatar.getName());
      tempFile.delete();
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
      result = FAILURE;
      getMessageHandler().error("unknownError");
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String upload()
  {
    String result = SUCCESS;

    if (avatarUploadFile != null)
    {
      try
      {
        InputStream in = null;
        OutputStream out = null;
        File temp = null;

        try
        {
          in = avatarUploadFile.getInputStream();
          temp = resManager.getTempFile("avatar-", ".up");
          out = new FileOutputStream(temp);
          Util.copy(in, out);
        }
        finally
        {
          if (in != null)
          {
            in.close();
          }

          if (out != null)
          {
            out.close();
          }
        }

        Dimension d = imageHandler.getDimension(temp);

        if ((d.width == 50) && (d.height == 50))
        {
          File avatar = getAvatarFile();

          if (temp.renameTo(avatar))
          {
            updateAvatar(avatar.getName());
          }
          else
          {
            getMessageHandler().error("unknownError");
          }
        }
        else if ((d.width < 50) || (d.height < 50))
        {
          File avatar = getAvatarFile();

          // TODO replace with scale with background
          imageHandler.scaleImageFix(temp, avatar, imageFormat, width, height);
          updateAvatar(avatar.getName());
          temp.delete();
        }
        else if ((d.width > 720) || (d.height > 576))
        {
          tempFile = resManager.getTempFile("avatar-", ".up");
          imageHandler.scaleImage(temp, tempFile, imageFormat, 720, 576);
          result = AVATARCROP;
          temp.delete();
        }
        else
        {
          tempFile = temp;
          result = AVATARCROP;
        }
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
        result = FAILURE;
      }
    }
    else
    {
      result = FAILURE;
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
  public UploadedFile getAvatarUploadFile()
  {
    return avatarUploadFile;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getHeight()
  {
    return height;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getImagePath()
  {
    String path = null;

    if (tempFile != null)
    {
      path = new StringBuffer("/resource/").append(tempFile.getName()).append(
        "?temp").toString();
    }

    return path;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getWidth()
  {
    return width;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getX()
  {
    return x;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getY()
  {
    return y;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param avatarUploadFile
   */
  public void setAvatarUploadFile(UploadedFile avatarUploadFile)
  {
    this.avatarUploadFile = avatarUploadFile;
  }

  /**
   * Method description
   *
   *
   * @param height
   */
  public void setHeight(int height)
  {
    this.height = height;
  }

  /**
   * Method description
   *
   *
   * @param width
   */
  public void setWidth(int width)
  {
    this.width = width;
  }

  /**
   * Method description
   *
   *
   * @param x
   */
  public void setX(int x)
  {
    this.x = x;
  }

  /**
   * Method description
   *
   *
   * @param y
   */
  public void setY(int y)
  {
    this.y = y;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   */
  private void updateAvatar(String name)
  {
    BlogSession session = getBlogSession();
    User user = session.getUser();

    if (user != null)
    {
      String oldAvatar = user.getAvatar();

      user.setAvatar(name);

      if (userDAO.edit(session, user))
      {
        if (oldAvatar != null)
        {
          File avatarDir = resManager.getDirectory(Constants.RESOURCE_AVATAR);
          File oa = new File(avatarDir, oldAvatar);

          if (oa.exists())
          {
            if (oa.delete())
            {
              if (logger.isLoggable(Level.WARNING))
              {
                StringBuffer log = new StringBuffer();

                log.append("cant delete avatar ").append(oa.getPath());
                logger.warning(log.toString());
              }
            }
          }
        }

        getMessageHandler().info("successUpdateAvatar");
      }
      else
      {
        getMessageHandler().error("failureUpdateAvatar");
      }
    }
    else
    {
      throw new IllegalStateException("user is null");
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private File getAvatarFile()
  {
    StringBuffer nameBuffer = new StringBuffer();

    nameBuffer.append(System.nanoTime()).append(".").append(imageExtension);

    File avatarDir = resManager.getDirectory(Constants.RESOURCE_AVATAR, true);

    return new File(avatarDir, nameBuffer.toString());
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private UploadedFile avatarUploadFile;

  /** Field description */
  private int height;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEFORMAT)
  private String imageFormat = Constants.DEFAULT_IMAGE_FORMAT;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEEXTENSION)
  private String imageExtension = Constants.DEFAULT_IMAGE_EXTENSION;

  /** Field description */
  @Context
  private ImageFileHandler imageHandler;

  /** Field description */
  @Context
  private ResourceManager resManager;

  /** Field description */
  private File tempFile;

  /** Field description */
  @Dao
  private UserDAO userDAO;

  /** Field description */
  private int width;

  /** Field description */
  private int x;

  /** Field description */
  private int y;
}
