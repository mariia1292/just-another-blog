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



package sonia.blog.search;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.lucene.document.Document;

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.PermaObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class EntryIndexHandler extends ContentObjectIndexHandler<Entry>
{

  /** Field description */
  public static final String CATEGORY = "entry";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  @Override
  @SuppressWarnings("unchecked")
  protected Document[] createDocuments(Entry item)
  {
    List<Document> docList = new ArrayList<Document>();

    if (item.isPublished())
    {
      Document doc = createContentObjectDocument(item);

      docList.add(doc);

      List<Comment> comments =
        BlogContext.getDAOFactory().getCommentDAO().findAllActivesByEntry(item);

      if (Util.hasContent(comments))
      {
        IndexHandler<Comment> commentHandler =
          (IndexHandler<Comment>) IndexHandlerFactory.getInstance().get(
              Comment.class);

        for (Comment comment : comments)
        {
          Document[] docs = commentHandler.createDocuments(comment);

          for (Document d : docs)
          {
            docList.add(d);
          }
        }
      }
    }

    return docList.toArray(new Document[0]);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected String getCategory()
  {
    return CATEGORY;
  }

  /**
   * Method description
   *
   *
   * @param id
   *
   * @return
   */
  @Override
  protected PermaObject getObject(long id)
  {
    return BlogContext.getDAOFactory().getEntryDAO().get(id);
  }
}
