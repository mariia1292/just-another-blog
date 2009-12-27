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

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Comment;

import sonia.util.Util;

/**
 *
 * @author Sebastian Sdorra
 */
public class CommentIndexHandler extends IndexHandler<Comment>
{

  /** Field description */
  public static final String CATEGORY = "comment";

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
  protected Document[] createDocuments(Comment item)
  {
    Document doc = null;

    if (!item.isSpam() && item.getEntry().isPublished())
    {
      doc = createBaseDocument(item, CATEGORY);
      doc.add(new Field(FIELD_AUTHOR, item.getAuthorName(), Field.Store.YES,
                        Field.Index.NOT_ANALYZED));
      doc.add(
          new Field(
              FIELD_CREATIONDATE,
              DateTools.timeToString(
                item.getCreationDate().getTime(),
                DateTools.Resolution.SECOND), Field.Store.YES,
                  Field.Index.NOT_ANALYZED));
      doc.add(new Field(FIELD_TITLE, "RE: " + item.getEntry().getTitle(),
                        Field.Store.YES, Field.Index.ANALYZED));
      doc.add(new Field(FIELD_CONTENT, Util.extractHTMLText(item.getContent()),
                        Field.Store.YES, Field.Index.ANALYZED));
      doc.add(new Field(FIELD_PARENTID, buildTypeId(item.getEntry()),
                        Field.Store.YES, Field.Index.NOT_ANALYZED));
    }

    return (doc != null)
           ? new Document[] { doc }
           : null;
  }

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  @Override
  protected Term[] createRemoveTerms(Comment item)
  {
    return new Term[] { new Term(FIELD_TYPEID, buildTypeId(item)) };
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  @Override
  protected Blog getItemBlog(Comment item)
  {
    return item.getEntry().getBlog();
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
  protected Comment getObject(long id)
  {
    return BlogContext.getDAOFactory().getCommentDAO().get(id);
  }
}
