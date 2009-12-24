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

import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;

import sonia.util.Util;

/**
 *
 * @author Sebastian Sdorra
 *
 * @param <T>
 */
public abstract class ContentObjectIndexHandler<T extends ContentObject>
        extends IndexHandler<T>
{

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract String getCategory();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param item
   *
   * @return
   */
  protected Document createContentObjectDocument(T item)
  {
    Document doc = createBaseDocument(item, getCategory());

    doc.add(new Field(FIELD_AUTHOR, item.getAuthorName(), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field(FIELD_CREATIONDATE,
                      DateTools.timeToString(item.getCreationDate().getTime(),
                        DateTools.Resolution.SECOND), Field.Store.YES,
                          Field.Index.NOT_ANALYZED));
    doc.add(new Field(FIELD_TITLE, item.getTitle(), Field.Store.YES,
                      Field.Index.ANALYZED));
    doc.add(new Field(FIELD_CONTENT, Util.extractHTMLText(item.getContent()),
                      Field.Store.YES, Field.Index.ANALYZED));

    return doc;
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
  protected Term[] createRemoveTerms(T item)
  {
    String tid = buildTypeId(item);

    return new Term[] { new Term(FIELD_TYPEID, tid),
                        new Term(FIELD_PARENTID, tid) };
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
  protected Blog getItemBlog(T item)
  {
    return item.getBlog();
  }
}
