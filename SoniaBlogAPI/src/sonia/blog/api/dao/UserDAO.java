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

import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public interface UserDAO extends GenericDAO<User>
{

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
   * @param filter
   *
   * @return
   */
  public long count(String filter);

  /**
   * Method description
   *
   *
   * @param filter
   * @param active
   *
   * @return
   */
  public long count(String filter, boolean active);

  /**
   * Method description
   *
   *
   * @param active
   *
   * @return
   */
  public long count(boolean active);

  /**
   * Method description
   *
   *
   * @param member
   *
   * @return
   */
  public boolean saveMember(BlogMember member);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param username
   * @param active
   *
   * @return
   */
  public User get(String username, boolean active);

  /**
   * Method description
   *
   *
   * @param username
   *
   * @return
   */
  public User get(String username);

  /**
   * Method description
   *
   *
   * @param username
   * @param password
   * @param active
   *
   * @return
   */
  public User get(String username, String password, boolean active);

  /**
   * Method description
   *
   *
   * @param filter
   * @param start
   * @param max
   *
   * @return
   */
  public List<User> getAll(String filter, int start, int max);

  /**
   * Method description
   *
   *
   * @param filter
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<User> getAll(String filter, boolean active, int start, int max);

  /**
   * Method description
   *
   *
   * @param active
   * @param start
   * @param max
   *
   * @return
   */
  public List<User> getAll(boolean active, int start, int max);

  /**
   * Method description
   *
   *
   * @param mail
   *
   * @return
   */
  public User getByMail(String mail);

  /**
   * Method description
   *
   *
   * @param username
   * @param code
   *
   * @return
   */
  public User getByNameAndCode(String username, String code);

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   *
   * @return
   */
  public BlogMember getMember(Blog blog, User user);

  /**
   * Method description
   *
   *
   * @param user
   * @param start
   * @param max
   *
   * @return
   */
  public List<BlogMember> getMembers(User user, int start, int max);

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   *
   * @return
   */
  public Role getRole(Blog blog, User user);

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   * @param user
   * @param role
   */
  public void setRole(Blog blog, User user, Role role);
}
