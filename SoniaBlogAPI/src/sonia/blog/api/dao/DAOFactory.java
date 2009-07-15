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


package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;

import sonia.plugin.service.ServiceReference;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class DAOFactory
{

  /** Field description */
  private static DAOFactory instance;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static DAOFactory getInstance()
  {
    if (instance == null)
    {
      instance = createDAOFactory();
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private static DAOFactory createDAOFactory()
  {
    BlogContext context = BlogContext.getInstance();
    ServiceReference<DAOFactory> reference =
      context.getServiceRegistry().get(DAOFactory.class, Constants.SERVCIE_DAO);

    return reference.get();
  }

  /**
   * Method description
   *
   *
   * @throws DAOException
   */
  public abstract void close() throws DAOException;

  /**
   * Method description
   *
   *
   * @throws DAOException
   */
  public abstract void init() throws DAOException;

  /**
   * Method description
   *
   */
  public abstract void install();

  /**
   * Method description
   *
   */
  public abstract void update();

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract AttachmentDAO getAttachmentDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract BlogDAO getBlogDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract BlogHitCountDAO getBlogHitCountDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract CategoryDAO getCategoryDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract CommentDAO getCommentDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract Object getConnection();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract EntryDAO getEntryDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract PageDAO getPageDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract TagDAO getTagDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract TrackbackDAO getTrackbackDAO();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract UserDAO getUserDAO();
}