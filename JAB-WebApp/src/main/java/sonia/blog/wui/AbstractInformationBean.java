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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.PageDAO;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class AbstractInformationBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public AbstractInformationBean()
  {
    super();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract Blog getBlog();

  /**
   * Method description
   *
   * @return
   */
  public long getAttachmentCount()
  {
    AttachmentDAO attachmentDAO =
      BlogContext.getDAOFactory().getAttachmentDAO();

    return attachmentDAO.count(getBlog());
  }

  /**
   * Method description
   *
   * @return
   */
  public long getCategoryCount()
  {
    CategoryDAO categegoryDAO = BlogContext.getDAOFactory().getCategoryDAO();

    return categegoryDAO.count(getBlog());
  }

  /**
   * Method description
   *
   * @return
   */
  public long getCommentCount()
  {
    Blog b = getRequest().getCurrentBlog();
    CommentDAO commentDAO = BlogContext.getDAOFactory().getCommentDAO();

    return commentDAO.count(getBlog());
  }

  /**
   * Method description
   *
   * @return
   */
  public long getEntryCount()
  {
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();

    return entryDAO.count(getBlog());
  }

  /**
   * Method description
   *
   * @return
   */
  public long getMemberCount()
  {
    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

    return userDAO.count(getBlog());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getPageCount()
  {
    PageDAO pageDAO = BlogContext.getDAOFactory().getPageDAO();

    return pageDAO.count(getBlog());
  }
}
