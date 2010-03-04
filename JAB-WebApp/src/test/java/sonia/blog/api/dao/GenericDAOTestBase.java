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

import org.junit.Before;
import org.junit.Test;

import sonia.blog.api.app.BlogSession;
import sonia.blog.entity.PermaObject;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 *
 * @param <T>
 */
public abstract class GenericDAOTestBase<T extends PermaObject>
{

  /** Field description */
  private static int counter = 0;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract GenericDAO<T> getDAO();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param id
   * @return
   */
  protected abstract T createExampleItem(int id);

  /**
   * Method description
   *
   *
   * @param item
   */
  protected abstract void edit(T item);

  /**
   * Method description
   *
   */
  @Before
  public void initDAO()
  {
    dao = getDAO();
    gaSession = DAOTestHelper.createGlobalAdminSession();
  }

  /**
   * Method description
   *
   */
  @Test
  public void testAdd()
  {
    T item = createExampleItem();

    assertTrue(dao.add(gaSession, item));

    Long id = item.getId();

    assertNotNull(id);

    T o = dao.get(id);

    assertNotNull(o);
    assertEquals(o, item);
  }

  /**
   * Method description
   *
   */
  @Test
  public void testCount()
  {
    T item = createExampleItem();

    assertTrue(dao.add(gaSession, item));
    item = createExampleItem();
    assertTrue(dao.add(gaSession, item));
    item = createExampleItem();
    assertTrue(dao.add(gaSession, item));
    assertTrue(dao.count() >= 3);
  }

  /**
   * Method description
   *
   */
  @Test
  public void testEdit()
  {
    T item = createExampleItem();

    assertTrue(dao.add(gaSession, item));

    Long id = item.getId();

    edit(item);
    assertTrue(dao.edit(gaSession, item));

    T o = dao.get(id);

    assertNotNull(o);
    assertEquals(o, item);
  }

  /**
   * Method description
   *
   */
  @Test
  public void testGetAll()
  {
    T item = createExampleItem();

    assertTrue(dao.add(gaSession, item));

    List<T> list = dao.getAll();

    assertNotNull(list);
    assertFalse(list.isEmpty());
    assertTrue(list.contains(item));
    list = dao.getAll(0, 1);
    assertNotNull(list);
    assertFalse(list.isEmpty());
    assertTrue(list.size() == 1);
  }

  /**
   * Method description
   *
   */
  @Test
  public void testIsEmpty()
  {
    T item = createExampleItem();

    assertTrue(dao.add(gaSession, item));
    assertFalse(dao.isEmpty());
  }

  /**
   * Method description
   *
   */
  @Test
  public void testRemove()
  {
    T item = createExampleItem();

    assertTrue(dao.add(gaSession, item));

    Long id = item.getId();

    assertNotNull(dao.get(id));
    assertTrue(dao.remove(gaSession, item));
    assertNull(dao.get(id));
  }

  /**
   * Method description
   *
   *
   * @return
   */
  protected T createExampleItem()
  {
    counter++;

    return createExampleItem(counter);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected GenericDAO<T> dao;

  /** Field description */
  protected BlogSession gaSession;
}
