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
import sonia.blog.api.app.FakeBlogContext;
import sonia.blog.entity.Blog;
import sonia.blog.entity.User;

import static org.mockito.Mockito.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class DAOTestHelper
{

  /** Field description */
  public static DAOFactory daoFactory;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static BlogSession createGlobalAdminSession()
  {
    Blog blog = new Blog();

    blog.setId(123l);
    blog.setActive(true);
    blog.setIdentifier("adminblog");
    blog.setTitle("Admin Blog");

    User user = new User();

    user.setId(123l);
    user.setName("globaladmin");
    user.setDisplayName("Global Admin");
    user.setPassword("admin");
    user.setEmail("global@admin.de");
    user.setGlobalAdmin(true);

    Subject subject = new Subject();

    subject.getPrincipals().add(user);

    LoginContext ctx = mock(LoginContext.class);

    when(ctx.getSubject()).thenReturn(subject);

    return new BlogSession(ctx, user, blog);
  }

  /**
   * Method description
   *
   */
  public static void initLogger()
  {
    Logger logger = Logger.getLogger("sonia");
    ConsoleHandler h = new ConsoleHandler();

    h.setLevel(Level.FINEST);
    logger.addHandler(h);
    logger.setLevel(Level.FINEST);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static DAOFactory getDAOFactory()
  {
    if (daoFactory == null)
    {
      daoFactory = FakeBlogContext.getDAOFactory();
      daoFactory.init();
      daoFactory.install();
    }

    return daoFactory;
  }
}
