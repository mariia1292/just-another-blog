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



package sonia.blog.api.search;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.entity.Blog;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Sebastian Sdorra
 */
public class SearchContext
{

  /**
   * Constructs ...
   *
   */
  public SearchContext()
  {
    providers =
      BlogContext.getInstance().getServiceRegistry().get(SearchProvider.class,
        Constants.SERVICE_SEARCHPROVIDER).getAll();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param session must be a Admin- or a GlobalAdminSession
   * @param blog
   *
   *
   * @throws SearchException
   */
  public void reIndex(BlogSession session, Blog blog) throws SearchException
  {
    if (Util.hasContent(providers))
    {
      for (SearchProvider provider : providers)
      {
        if (provider.isReindexable(session, blog))
        {
          provider.reIndex(session, blog);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param locale
   * @param query
   *
   * @return
   *
   * @throws SearchException
   */
  public List<SearchCategory> search(Blog blog, Locale locale, String query)
          throws SearchException
  {
    List<SearchCategory> categories = new ArrayList<SearchCategory>();

    if (Util.hasContent(providers))
    {
      for (SearchProvider provider : providers)
      {
        categories.addAll(provider.search(blog, locale, query));
      }
    }

    return categories;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<SearchProvider> providers;
}
