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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogJob;
import sonia.blog.api.dao.DAOListener;
import sonia.blog.entity.Blog;
import sonia.blog.entity.PermaObject;

import sonia.jobqueue.JobException;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class SearchIndexListener implements DAOListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(SearchIndexListener.class.getName());

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
    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("handle ").append(action.toString());
      msg.append(" with ").append(item.getClass().getName());
      msg.append("[").append(item.getId()).append("]");
      logger.finest(msg.toString());
    }

    switch (action)
    {
      case POSTADD :
      case POSTUPDATE :
      case POSTREMOVE :
        handleIndexEvent(action, item);
    }
  }

  /**
   * Method description
   *
   *
   * @param action
   * @param item
   */
  private void handleIndexEvent(Action action, PermaObject item)
  {
    IndexHandler handler =
      IndexHandlerFactory.getInstance().get(item.getClass());

    if (handler != null)
    {
      IndexEventJOB job = new IndexEventJOB(handler, action, item);

      BlogContext.getInstance().getJobQueue().add(job);
    }
    else if (logger.isLoggable(Level.WARNING))
    {
      StringBuffer msg = new StringBuffer("could not find a handler for ");

      msg.append(item.getClass().getName());
      logger.warning(msg.toString());
    }
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/11/13
   * @author         Enter your name here...
   */
  private static class IndexEventJOB implements BlogJob
  {

    /**
     * Constructs ...
     *
     *
     * @param handler
     * @param action
     * @param item
     */
    public IndexEventJOB(IndexHandler handler, Action action, PermaObject item)
    {
      this.handler = handler;
      this.action = action;
      this.item = item;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @throws JobException
     */
    public void excecute() throws JobException
    {
      switch (action)
      {
        case POSTADD :
          handler.persits(item);

          break;

        case POSTUPDATE :
          handler.update(item);

          break;

        case POSTREMOVE :
          handler.remove(item);

          break;
      }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public Blog getBlog()
    {
      return handler.getBlog(item);
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getDescription()
    {
      return "adds, remove or update documents in the search index";
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getName()
    {
      return getClass().getSimpleName();
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private Action action;

    /** Field description */
    private IndexHandler handler;

    /** Field description */
    private PermaObject item;
  }
}
