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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.DAOListener;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;
import sonia.blog.entity.PermaObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class CleanFileSystemListener implements DAOListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(CleanFileSystemListener.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param action
   * @param item
   */
  public void handleEvent(Action action, PermaObject item)
  {
    if (action == Action.POSTREMOVE)
    {
      if (item instanceof Entry)
      {
        Entry entry = (Entry) item;

        clean(entry.getBlog().getId(), item.getId(),
              Constants.RESOURCE_ENTRIES);
      }
      else if (item instanceof Page)
      {
        Page page = (Page) item;

        clean(page.getBlog().getId(), item.getId(), Constants.RESOURCE_PAGES);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param blogId
   * @param id
   * @param type
   */
  private void clean(long blogId, long id, String type)
  {
    File attachmentDir =
      BlogContext.getInstance().getResourceManager().getDirectory(
          Constants.RESOURCE_ATTACHMENT);
    StringBuffer path = new StringBuffer();

    path.append(blogId).append(File.separator).append(type);
    path.append(File.separator).append(id);

    File directory = new File(attachmentDir, path.toString());

    if (logger.isLoggable(Level.FINER))
    {
      StringBuffer msg = new StringBuffer("try to delete directory ");

      msg.append(directory.getPath());
      logger.info(msg.toString());
    }

    if (directory.exists())
    {
      Util.delete(directory);
    }
    else if (logger.isLoggable(Level.FINER))
    {
      StringBuffer msg =
        new StringBuffer("could not find attachment directory: ");

      logger.finer(msg.append(directory.getPath()).toString());
    }
  }
}
