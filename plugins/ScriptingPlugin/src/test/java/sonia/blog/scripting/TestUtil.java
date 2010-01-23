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



package sonia.blog.scripting;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import sonia.blog.api.app.BlogSession;
import sonia.blog.entity.Blog;
import sonia.blog.entity.User;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import javax.security.auth.login.LoginContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class TestUtil
{

  /**
   * Method description
   *
   *
   * @return
   */
  public static BlogSession createGlobalAdminSession()
  {
    LoginContext loginContext = mock(LoginContext.class);
    User user = new User();

    user.setId(12l);
    user.setActive(true);
    user.setDisplayName("GlobalAdmin");
    user.setEmail("ga@junit.org");
    user.setGlobalAdmin(true);
    user.setName("ga");
    user.setPassword("secret");

    Blog blog = new Blog();

    blog.setId(12l);
    blog.setActive(true);
    blog.setIdentifier("blog.junit.org");
    blog.setTitle("junit blog");

    return new BlogSession(loginContext, user, blog);
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws IOException
   */
  public static File createTempDirectory() throws IOException
  {
    File directory = File.createTempFile("script-", "-junit");

    if (directory.exists())
    {
      assertTrue(directory.delete());
    }

    assertTrue(directory.mkdirs());

    return directory;
  }

  /**
   * Method description
   *
   */
  @Test
  public void testCreateGlobalAdminSession()
  {
    assertNotNull(createGlobalAdminSession());
  }
}
