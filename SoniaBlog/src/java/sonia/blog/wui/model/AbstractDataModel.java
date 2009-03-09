/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.model.DataModel;

/**
 *
 * @author sdorra
 */
public abstract class AbstractDataModel extends DataModel
{

  /**
   * Constructs ...
   *
   *
   * @param pageSize
   */
  protected AbstractDataModel(int pageSize)
  {
    this.pageSize = pageSize;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract long countData();

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  protected abstract List<?> getData(int start, int max);

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public int getRowCount()
  {
    rowCount = new Long(countData()).intValue();

    return rowCount;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Object getRowData()
  {
    Object data = null;

    if ((index >= 0) && isRowAvailable())
    {
      if ((values == null)
          || ((index >= (lastFetchedIndex + pageSize))
              || (index < lastFetchedIndex)))
      {
        values = getData(index, index);
        lastFetchedIndex = index;
      }

      data = values.get(index - lastFetchedIndex);
    }

    return data;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public int getRowIndex()
  {
    return index;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public boolean isRowAvailable()
  {
    if (rowCount < 0)
    {
      getRowCount();
    }

    return index < rowCount;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param rowIndex
   */
  @Override
  public void setRowIndex(int rowIndex)
  {
    this.index = rowIndex;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int index = 0;

  /** Field description */
  private int lastFetchedIndex;

  /** Field description */
  private int pageSize;

  /** Field description */
  private int rowCount = -1;

  /** Field description */
  private List<?> values;
}
