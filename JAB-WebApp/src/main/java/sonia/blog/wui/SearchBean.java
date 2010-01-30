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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.search.SearchCategory;
import sonia.blog.api.search.SearchEntry;
import sonia.blog.api.util.AbstractBean;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Sebastian Sdorra
 */
public class SearchBean extends AbstractBean
{

  /** Field description */
  public static final String NAME = "SearchBean";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public SearchBean()
  {
    super();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getCategories()
  {
    if (categories != null)
    {
      categoryModel = new ListDataModel(categories);
    }

    return categoryModel;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SearchCategory getCategory()
  {
    return category;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getCategoryLink()
  {
    String result = "#";
    SearchCategory cat = (SearchCategory) categoryModel.getRowData();

    if (cat != null)
    {
      BlogRequest request = getRequest();
      StringBuffer uri = new StringBuffer(request.getRequestURI());

      uri.append("?search=").append(searchString).append("&category=");
      uri.append(cat.getName());
      result = uri.toString();
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getPageEntries()
  {
    return (pageEntries != null)
           ? new ListDataModel(pageEntries)
           : null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getSearchResults()
  {
    return new ListDataModel(category.getEntries());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSearchString()
  {
    return searchString;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param categories
   */
  public void setCategories(List<SearchCategory> categories)
  {
    this.categories = categories;
  }

  /**
   * Method description
   *
   *
   * @param category
   */
  public void setCategory(SearchCategory category)
  {
    this.category = category;
  }

  /**
   * Method description
   *
   *
   * @param pageEntries
   */
  public void setPageEntries(List<SearchEntry> pageEntries)
  {
    this.pageEntries = pageEntries;
  }

  /**
   * Method description
   *
   *
   * @param searchString
   */
  public void setSearchString(String searchString)
  {
    this.searchString = searchString;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  public SearchCategory category;

  /** Field description */
  private List<SearchCategory> categories;

  /** Field description */
  private DataModel categoryModel;

  /** Field description */
  private List<SearchEntry> pageEntries;

  /** Field description */
  private String searchString;
}
