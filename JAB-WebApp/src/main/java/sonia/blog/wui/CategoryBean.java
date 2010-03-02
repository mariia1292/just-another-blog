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
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.authentication.RequireRole;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Category;
import sonia.blog.entity.Role;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Logger;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Sebastian Sdorra
 */
@RequireRole(Role.AUTHOR)
public class CategoryBean extends AbstractBean
{

  /** Field description */
  private static final String EDIT = "edit";

  /** Field description */
  private static Logger logger = Logger.getLogger(CategoryBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public CategoryBean()
  {
    init();
    category = new Category();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String edit()
  {
    category = (Category) categories.getRowData();

    return EDIT;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String newCategory()
  {
    category = new Category();

    return EDIT;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String remove()
  {
    String result = SUCCESS;
    Category cat = (Category) categories.getRowData();

    if (entryDAO.count(cat) == 0)
    {
      if (categoryDAO.remove(getBlogSession(), cat))
      {
        getMessageHandler().info(getRequest(), "removeCategorySuccess");
      }
      else
      {
        result = FAILURE;
        getMessageHandler().error(getRequest(), "categoryActionFailure");
      }
    }
    else
    {
      result = FAILURE;
      getMessageHandler().warn(getRequest(), "categoryHasEntries");
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String save()
  {
    String result = SUCCESS;
    BlogRequest request = getRequest();
    BlogSession session = request.getBlogSession();

    category.setBlog(session.getBlog());

    if (category.getId() != null)
    {
      if (categoryDAO.edit(session, category))
      {
        getMessageHandler().info(request, "updateCategorySuccess");
      }
      else
      {
        result = FAILURE;
        getMessageHandler().error(request, "categoryActionFailure");
      }
    }
    else
    {
      if (categoryDAO.add(session, category))
      {
        getMessageHandler().info(request, "createCategorySuccess");
      }
      else
      {
        result = FAILURE;
        getMessageHandler().error(request, "categoryActionFailure");
      }
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   * @return
   */
  public DataModel getCategories()
  {
    categories = new ListDataModel();

    List<Category> categoryList =
      categoryDAO.getAll(getRequest().getCurrentBlog());

    if ((categoryList != null) &&!categoryList.isEmpty())
    {
      categories.setWrappedData(categoryList);
    }

    return categories;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Category getCategory()
  {
    return category;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param categories
   */
  public void setCategories(DataModel categories)
  {
    this.categories = categories;
  }

  /**
   * Method description
   *
   *
   * @param category
   */
  public void setCategory(Category category)
  {
    this.category = category;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel categories;

  /** Field description */
  private Category category;

  /** Field description */
  @Dao
  private CategoryDAO categoryDAO;

  /** Field description */
  @Dao
  private EntryDAO entryDAO;
}
