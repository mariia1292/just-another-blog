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



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.config.XmlConfiguration;

import sonia.security.cipher.DefaultCipher;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Date;

/**
 *
 * @author Sebastian Sdorra
 */
public class BlogConfiguration extends XmlConfiguration
{

  /**
   * Constructs ...
   *
   */
  public BlogConfiguration()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void load() throws IOException
  {
    File file = BlogContext.getInstance().getConfigFile();

    if (file.exists())
    {
      FileInputStream fis = new FileInputStream(file);

      try
      {
        load(fis);

        String key = getString(Constants.CONFIG_SECUREKEY);

        if (Util.hasContent(key))
        {
          setCipher(new DefaultCipher(key.toCharArray()));
        }
      }
      finally
      {
        if (fis != null)
        {
          fis.close();
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void store() throws IOException
  {
    File file = BlogContext.getInstance().getConfigFile();
    File backupDir = new File(file.getParentFile(), "backup");

    if (!backupDir.exists())
    {
      backupDir.mkdirs();
    }

    File backupFile = new File(backupDir,
                               "config-" + new Date().getTime() + ".xml");

    file.renameTo(backupFile);

    FileOutputStream fos = new FileOutputStream(file);

    try
    {
      store(fos);
    }
    catch (IOException ex)
    {
      if (!file.exists() && backupFile.exists())
      {
        backupFile.renameTo(file);
      }

      throw ex;
    }
    finally
    {
      if (fos != null)
      {
        fos.close();
      }
    }
  }
}
