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

import sonia.blog.entity.Blog;
import sonia.blog.entity.FileObject;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.UUID;

/**
 *
 * @author Sebastian Sdorra
 */
public class ResourceManager
{

  /**
   * Constructs ...
   *
   *
   * @param resourceDirectory
   */
  public ResourceManager(File resourceDirectory)
  {
    this.resourceDirectory = resourceDirectory;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   *
   * @return
   */
  public File getDirectory(String name)
  {
    return getDirectory(name, null, true);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param create
   *
   * @return
   */
  public File getDirectory(String name, boolean create)
  {
    return getDirectory(name, null, create);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param blog
   *
   * @return
   */
  public File getDirectory(String name, Blog blog)
  {
    return getDirectory(name, blog, true);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param blog
   * @param create
   *
   * @return
   */
  public File getDirectory(String name, Blog blog, boolean create)
  {
    String path = name;

    if (blog != null)
    {
      path += File.separator + blog.getId();
    }

    File file = new File(resourceDirectory, path);

    if (!file.exists() && create)
    {
      if (!file.mkdirs())
      {
        throw new RuntimeException("resource directory is read only");
      }
    }

    return file;
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public File getFile(FileObject object)
  {
    return new File(resourceDirectory, object.getFilePath());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public File getResourceDirectory()
  {
    return resourceDirectory;
  }

  /**
   * Method description
   *
   *
   * @param prefix
   * @param suffix
   *
   * @return
   */
  public File getTempFile(String prefix, String suffix)
  {
    File tempDirectory = getDirectory(Constants.RESOURCE_TEMP, true);
    String name = new StringBuffer(prefix).append(
                      UUID.randomUUID().toString()).append(suffix).toString();

    return new File(tempDirectory, name);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File resourceDirectory;
}
