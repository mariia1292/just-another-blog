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

import org.junit.Test;

import sonia.blog.entity.User;

import static org.junit.Assert.*;

/**
 *
 * @author Sebastian Sdorra
 */
public class UserDAOTest extends GenericDAOTestBase<User>
{

  /**
   * Method description
   *
   */
  @Test
  public void testGetByMail()
  {
    UserDAO userDAO = getDAO();
    User user = createExampleItem();

    assertTrue(userDAO.add(gaSession, user));

    User o = userDAO.getByMail(user.getEmail());

    assertEquals(user, o);
  }

  /**
   * Method description
   *
   */
  @Test
  public void testGetByX()
  {
    UserDAO userDAO = getDAO();
    User user = createExampleItem();

    user.setActive(false);
    assertTrue(userDAO.add(gaSession, user));

    User o = userDAO.get(user.getName());

    assertNotNull(o);
    assertEquals(user, o);
    o = userDAO.get(user.getName(), true);
    assertNull(o);
    o = userDAO.get(user.getName(), false);
    assertNotNull(o);
    assertEquals(user, o);
    o = userDAO.get(user.getName(), user.getPassword(), true);
    assertNull(o);
    o = userDAO.get(user.getName(), user.getPassword(), false);
    assertNotNull(o);
    assertEquals(user, o);
    o = userDAO.getByMail(user.getEmail());
    assertNotNull(o);
    assertEquals(user, o);
    o = userDAO.getByNameAndCode(user.getName(), user.getActivationCode());
    assertNotNull(o);
    assertEquals(user, o);
    o = userDAO.getByNameAndCode(user.getName(), "xxx-xx-xx-xxx");
    assertNull(o);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public UserDAO getDAO()
  {
    return DAOTestHelper.getDAOFactory().getUserDAO();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param id
   *
   * @return
   */
  @Override
  protected User createExampleItem(int id)
  {
    User user = new User();

    user.setActive(true);
    user.setName("tuser" + id);
    user.setDisplayName("Test User");
    user.setEmail("test.user" + id + "@example.com");
    user.setPassword("test");
    user.setSelfManaged(false);

    return user;
  }

  /**
   * Method description
   *
   *
   * @param item
   */
  @Override
  protected void edit(User item)
  {
    item.setHomePage("http://www.example.com");
  }
}
