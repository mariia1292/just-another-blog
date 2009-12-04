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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.util.Version;

import org.w3c.dom.Comment;

import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Page;
import sonia.blog.entity.PermaObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Locale;

/**
 *
 * @author Sebastian Sdorra
 */
public class SearchHelper
{

  /** Field description */
  private static final String CATEGORY_COMMENT = "comment";

  /** Field description */
  private static final String CATEGORY_ENTRY = "entry";

  /** Field description */
  private static final String CATEGORY_PAGE = "page";

  /** Field description */
  private static final String CATEGORY_UNKNOWN = "unknown";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public static Document buildDocument(ContentObject entry)
  {
    Document doc = new Document();

    doc.add(new Field("tid", buildTypeId(entry), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field("type", entry.getClass().getName(), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field("id", entry.getId().toString(), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field("author", entry.getAuthorName(), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field("creationDate",
                      DateTools.timeToString(entry.getCreationDate().getTime(),
                        DateTools.Resolution.SECOND), Field.Store.YES,
                          Field.Index.NOT_ANALYZED));
    doc.add(new Field("title", entry.getTitle(), Field.Store.YES,
                      Field.Index.ANALYZED));
    doc.add(new Field("content", Util.extractHTMLText(entry.getContent()),
                      Field.Store.YES, Field.Index.ANALYZED));
    doc.add(new Field("category", getCategory(entry), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));

    return doc;
  }

  /**
   * Method description
   *
   *
   *
   *
   * @param object
   *
   * @return
   */
  public static String buildTypeId(PermaObject object)
  {
    StringBuffer result = new StringBuffer();

    result.append(object.getClass().getName());
    result.append("-").append(object.getId());

    return result.toString();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public static Analyzer getAnalyzer(Blog blog)
  {
    return getAnalyzer(blog.getLocale());
  }

  /**
   * Method description
   *
   *
   * @param locale
   *
   * @return
   */
  public static Analyzer getAnalyzer(Locale locale)
  {
    return new StandardAnalyzer(Version.LUCENE_CURRENT);
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public static String getCategory(Object object)
  {
    String result = null;

    if (object instanceof Entry)
    {
      result = CATEGORY_ENTRY;
    }
    else if (object instanceof Page)
    {
      result = CATEGORY_PAGE;
    }
    else if (object instanceof Comment)
    {
      result = CATEGORY_COMMENT;
    }
    else
    {
      result = CATEGORY_UNKNOWN;
    }

    return result;
  }
}
