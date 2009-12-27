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



package sonia.blog.wui.model;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.entity.Entry;

import sonia.jsf.model.DataPage;
import sonia.jsf.model.PagedListDataModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class EntryDataModel extends PagedListDataModel<Entry>
{

  /**
   * Constructs ...
   *
   *
   * @param session
   * @param pageSize
   */
  public EntryDataModel(BlogSession session, int pageSize)
  {
    super(pageSize);
    this.session = session;
    this.entryDAO = BlogContext.getDAOFactory().getEntryDAO();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param startRow
   * @param pageSize
   *
   * @return
   */
  @Override
  public DataPage<Entry> fetchPage(int startRow, int pageSize)
  {
    List<Entry> list = null;
    long size = entryDAO.countModifyAbleEntries(session);

    if (size > startRow)
    {
      list = entryDAO.getAllModifyAbleEntries(session, startRow, pageSize);
    }
    else
    {
      list = new ArrayList<Entry>();
    }

    return new DataPage<Entry>((int) size, startRow, list);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private EntryDAO entryDAO;

  /** Field description */
  private BlogSession session;
}
