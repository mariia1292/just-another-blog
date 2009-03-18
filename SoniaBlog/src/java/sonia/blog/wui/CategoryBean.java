/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Category;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author sdorra
 */
public class CategoryBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public CategoryBean()
  {
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

    return SUCCESS;
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

    return SUCCESS;
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
      if (categoryDAO.remove(cat))
      {
        getMessageHandler().info("removeCategorySuccess");
      }
      else
      {
        result = FAILURE;
        getMessageHandler().error("categoryActionFailure");
      }
    }
    else
    {
      result = FAILURE;
      getMessageHandler().warn("categoryHasEntries");
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

    category.setBlog(getRequest().getCurrentBlog());

    if (category.getId() != null)
    {
      if (categoryDAO.edit(category))
      {
        getMessageHandler().info("updateCategorySuccess");
      }
      else
      {
        result = FAILURE;
        getMessageHandler().error("categoryActionFailure");
      }
    }
    else
    {
      if (categoryDAO.add(category))
      {
        getMessageHandler().info("createCategorySuccess");
      }
      else
      {
        result = FAILURE;
        getMessageHandler().error("categoryActionFailure");
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
