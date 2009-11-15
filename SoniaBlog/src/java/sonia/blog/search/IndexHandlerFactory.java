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



package sonia.blog.search;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;
import sonia.blog.entity.PermaObject;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class IndexHandlerFactory
{

  /** Field description */
  private static IndexHandlerFactory instance;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(IndexHandlerFactory.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  private IndexHandlerFactory()
  {
    handlerMap = new HashMap<Class<? extends PermaObject>,
                             IndexHandler<? extends PermaObject>>();
    put(Comment.class, new CommentIndexHandler());
    put(Entry.class, new EntryIndexHandler());
    put(Page.class, new PageIndexHandler());
    locks = new WeakHashMap<File, Semaphore>();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static IndexHandlerFactory getInstance()
  {
    if (instance == null)
    {
      instance = new IndexHandlerFactory();
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param clazz
   * @param handler
   */
  public void put(Class<? extends PermaObject> clazz,
                  IndexHandler<? extends PermaObject> handler)
  {
    handlerMap.put(clazz, handler);
  }

  /**
   * Method description
   *
   *
   * @param clazz
   */
  public void remove(Class<? extends PermaObject> clazz)
  {
    handlerMap.remove(clazz);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param clazz
   *
   * @return
   */
  public IndexHandler<? extends PermaObject> get(
          Class<? extends PermaObject> clazz)
  {
    return handlerMap.get(clazz);
  }

  /**
   * Method description
   *
   *
   *
   * @param file
   *
   * @return
   */
  public synchronized Semaphore getLock(File file)
  {
    Semaphore lock = locks.get(file);

    if (lock == null)
    {
      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer msg = new StringBuffer();

        msg.append("create lock for ").append(file.getPath());
        logger.finest(msg.toString());
      }

      lock = new Semaphore(1);
      locks.put(file, lock);
    }

    return lock;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Map<Class<? extends PermaObject>, IndexHandler<? extends PermaObject>> handlerMap;

  /** Field description */
  private Map<File, Semaphore> locks;
}
