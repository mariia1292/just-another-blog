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



package sonia.blog.tbv;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;

/**
 *
 * @author Sebastian Sdorra
 */
public class TrackbackValidatorTest
{

  /**
   * Method description
   *
   */
  @Test
  public void testFakeBlogContext()
  {
    assertEquals(BlogContext.getInstance().getClass(), FakeBlogContext.class);
    assertEquals(BlogContext.getInstance().getLinkBuilder().buildLink(null,
            new Blog()), FakeBlogContext.URL);
  }

  /**
   * Method description
   *
   */
  @Test
  public void testIsSpam()
  {
    URL failureURL = getClass().getResource("/sonia/blog/tbv/failure.txt");
    URL failureImageURL =
      getClass().getResource("/sonia/blog/tbv/failure-2.jpg");
    URL successURL = getClass().getResource("/sonia/blog/tbv/success.txt");
    TrackbackValidator validator = new TrackbackValidator();
    BlogRequest request = createRequestMock();

    assertNotNull(request);

    Comment comment = new Comment(Comment.Type.COMMENT);

    comment.setAuthorURL(failureURL.toExternalForm());
    assertFalse(validator.isSpam(request, comment));
    comment.setType(Comment.Type.TRACKBACK_SEND);
    assertFalse(validator.isSpam(request, comment));
    comment.setType(Comment.Type.TRACKBACK_RECEIVE);
    assertTrue(validator.isSpam(request, comment));
    comment.setAuthorURL(failureImageURL.toExternalForm());
    assertTrue(validator.isSpam(request, comment));
    comment.setAuthorURL(successURL.toExternalForm());
    assertFalse(validator.isSpam(request, comment));
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private BlogRequest createRequestMock()
  {
    BlogRequest request = mock(BlogRequest.class);

    when(request.getCurrentBlog()).thenReturn(new Blog());

    return request;
  }
}
