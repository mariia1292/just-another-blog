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

import sonia.blog.api.app.BlogSession;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Tag;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public interface EntryDAO extends GenericDAO<Entry>
{

  /**
   * Method description
   *
   *
   * @param category
   *
   * @return
   */
  public long count(Category category);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long count(Blog blog);

  /**
   * Method description
   *
   *
   * @param session
   *
   * @return
   */
  public long countModifyAbleEntries(BlogSession session);

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   *
   * @return
   */
  public List<Date> getAllCalendarDates(Blog blog, Date startDate,
          Date endDate);

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   * @param start
   * @param max
   *
   * @return
   */
  public List<Date> getAllCalendarDates(Blog blog, Date startDate,
          Date endDate, int start, int max);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param active
   * @return
   */
  public List<Entry> getAll(boolean active);

  /**
   * Method description
   *
   *
   *
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(boolean active, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, boolean active);

  /**
   * Method description
   *
   *
   * @param blog
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, boolean active, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, Date startDate, Date endDate);

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, Date startDate, Date endDate, int start,
                            int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param tag
   * @param active
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, Tag tag, boolean active);

  /**
   * Method description
   *
   *
   * @param blog
   * @param tag
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, Tag tag, boolean active, int start,
                            int max);

  /**
   * Method description
   *
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Entry> getAll(Blog blog);

  /**
   * Method description
   *
   *
   * @param blog
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, int start, int max);

  /**
   * Method description
   *
   *
   * @param category
   *
   * @return
   */
  public List<Entry> getAll(Category category);

  /**
   * Method description
   *
   *
   * @param category
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(Category category, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param author
   * @param published
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAll(Blog blog, User author, Boolean published,
                            int start, int max);

  /**
   *
   *
   * @param authorSession
   * @param start
   * @param max
   *
   * @return
   */
  public List<Entry> getAllModifyAbleEntries(BlogSession authorSession,
          int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Blog blog, Entry entry, Boolean published);

  /**
   * Method description
   *
   *
   * @param category
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Category category, Entry entry, Boolean published);

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param tag
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Blog blog, Tag tag, Entry entry, Boolean published);

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Blog blog, Date startDate, Date endDate,
                            Entry entry, Boolean published);

  /**
   * Method description
   *
   *
   * @param blog
   * @param author
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getNextEntry(Blog blog, User author, Entry entry,
                            Boolean published);

  /**
   * Method description
   *
   *
   * @param blog
   * @param startDate
   * @param endDate
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Blog blog, Date startDate, Date endDate,
                                Entry entry, Boolean published);

  /**
   * Method description
   *
   *
   * @param blog
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Blog blog, Entry entry, Boolean published);

  /**
   * Method description
   *
   *
   * @param category
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Category category, Entry entry,
                                Boolean published);

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param tag
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Blog blog, Tag tag, Entry entry,
                                Boolean published);

  /**
   * Method description
   *
   *
   * @param blog
   * @param author
   * @param entry
   * @param published
   *
   * @return
   */
  public Entry getPreviousEntry(Blog blog, User author, Entry entry,
                                Boolean published);
}
