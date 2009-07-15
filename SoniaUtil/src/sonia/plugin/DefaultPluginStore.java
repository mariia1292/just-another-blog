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


package sonia.plugin;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultPluginStore implements PluginStore
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultPluginStore.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DefaultPluginStore()
  {
    storeDir = new File(System.getProperty("java.io.tmpdir"), "plugin.store");

    if (!storeDir.exists() &&!storeDir.mkdirs())
    {
      throw new RuntimeException("could not create store directory");
    }
  }

  /**
   * Constructs ...
   *
   *
   * @param storeDir
   */
  public DefaultPluginStore(File storeDir)
  {
    this.storeDir = storeDir;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void startPlugin(Plugin plugin)
  {
    File file = new File(storeDir, plugin.getName());

    if (file.exists() &&!file.delete())
    {
      throw new RuntimeException("could not delete file " + file.getPath());
    }
  }

  /**
   * Method description
   *
   *
   * @param plugin
   */
  public void stopPlugin(Plugin plugin)
  {
    File file = new File(storeDir, plugin.getName());

    if (!file.exists())
    {
      try
      {
        file.createNewFile();
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param plugin
   *
   * @return
   */
  public boolean isStartAble(Plugin plugin)
  {
    File file = new File(storeDir, plugin.getName());

    return !file.exists();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File storeDir;
}