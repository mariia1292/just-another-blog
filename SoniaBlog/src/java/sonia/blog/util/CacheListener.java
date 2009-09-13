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
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;
import sonia.blog.entity.Trackback;

import sonia.cache.ClearCondition;
import sonia.cache.ObjectCache;

import sonia.config.ConfigurationListener;
import sonia.config.ModifyableConfiguration;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class CacheListener implements DAOListener, ConfigurationListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(CacheListener.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param config
   * @param key
   */
  public void configChanged(ModifyableConfiguration config, String key)
  {
    if (firstCall)
    {
      init();
    }

    if (cache != null)
    {
      cache.clear();
    }
  }

  /**
   * Method description
   *
   *
   * @param action
   * @param item
   */
  public void handleEvent(Action action, Object item)
  {
    if (firstCall)
    {
      init();
    }

    if (cache != null)
    {
      switch (action)
      {
        case POSTADD :
        case POSTREMOVE :
        case POSTUPDATE :
          clearCache(item);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param item
   */
  private void clearCache(Object item)
  {
    Long id = getId(item);

    if (id != null)
    {
      if (logger.isLoggable(Level.FINE))
      {
        StringBuffer log = new StringBuffer();

        log.append("clear mapping cache for blog ").append(id.toString());
        logger.fine(log.toString());
      }

      cache.clear(new BlogClearCondition(id));
    }
    else
    {
      if (logger.isLoggable(Level.FINE))
      {
        logger.fine("clear whole mapping cache");
      }

      cache.clear();
    }
  }

  /**
   * Method description
   *
   */
  private void init()
  {
    cache =
      BlogContext.getInstance().getCacheManager().get(Constants.CACHE_MAPPING);

    if (cache != null)
    {
      BlogContext.getInstance().getConfiguration().addListener(this);
    }

    firstCall = false;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  private Long getId(Object item)
  {
    Long id = null;

    if (item instanceof Blog)
    {
      id = ((Blog) item).getId();
    }
    else if (item instanceof Entry)
    {
      id = ((Entry) item).getBlog().getId();
    }
    else if (item instanceof Page)
    {
      id = ((Page) item).getBlog().getId();
    }
    else if (item instanceof Comment)
    {
      id = ((Comment) item).getEntry().getBlog().getId();
    }
    else if (item instanceof Category)
    {
      id = ((Category) item).getBlog().getId();
    }
    else if (item instanceof Trackback)
    {
      id = ((Trackback) item).getEntry().getId();
    }

    return id;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/09/13
   * @author         Enter your name here...
   */
  private static class BlogClearCondition implements ClearCondition
  {

    /**
     * Constructs ...
     *
     *
     * @param id
     */
    public BlogClearCondition(Long id)
    {
      this.id = id.toString();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param key
     *
     * @return
     */
    public boolean matches(Object key)
    {
      return key.toString().startsWith(id + ":");
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private String id;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ObjectCache cache;

  /** Field description */
  private boolean firstCall = true;
}
