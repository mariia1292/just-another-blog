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



package sonia.jsf.model;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.model.DataModel;

/**
 *
 * @author Sebastian Sdorra
 *
 * @param <T>
 */
public abstract class PagedListDataModel<T> extends DataModel
{

  /**
   * Constructs ...
   *
   *
   * @param pageSize
   */
  public PagedListDataModel(int pageSize)
  {
    super();
    this.pageSize = pageSize;
    this.rowIndex = -1;
    this.page = null;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param startRow
   * @param pageSize
   *
   * @return
   */
  public abstract DataPage<T> fetchPage(int startRow, int pageSize);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public int getRowCount()
  {
    return getPage().getDatasetSize();
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
    if (rowIndex < 0)
    {
      throw new IllegalArgumentException(
          "Invalid rowIndex for PagedListDataModel; not within page");
    }

    if (page == null)
    {
      page = fetchPage(rowIndex, pageSize);
    }

    if (rowIndex == page.getStartRow())
    {
      page = fetchPage(rowIndex, pageSize);
    }

    int datasetSize = page.getDatasetSize();
    int startRow = page.getStartRow();
    int nRows = page.getData().size();
    int endRow = startRow + nRows;

    if (rowIndex >= datasetSize)
    {
      throw new IllegalArgumentException("Invalid rowIndex");
    }

    if (rowIndex < startRow)
    {
      page = fetchPage(rowIndex, pageSize);
      startRow = page.getStartRow();
    }
    else if (rowIndex >= endRow)
    {
      page = fetchPage(rowIndex, pageSize);
      startRow = page.getStartRow();
    }

    return page.getData().get(rowIndex - startRow);
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
    return rowIndex;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public Object getWrappedData()
  {
    return page.getData();
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
    DataPage<T> p = getPage();

    if (p == null)
    {
      return false;
    }

    int ri = getRowIndex();

    if (ri < 0)
    {
      return false;
    }
    else if (ri >= p.getDatasetSize())
    {
      return false;
    }
    else
    {
      return true;
    }
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param index
   */
  @Override
  public void setRowIndex(int index)
  {
    rowIndex = index;
  }

  /**
   * Method description
   *
   *
   * @param o
   */
  @Override
  public void setWrappedData(Object o)
  {
    throw new UnsupportedOperationException("setWrappedData");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private DataPage<T> getPage()
  {
    if (page != null)
    {
      return page;
    }

    int ri = getRowIndex();
    int startRow = ri;

    if (ri == -1)
    {
      startRow = 0;
    }

    page = fetchPage(startRow, pageSize);

    return page;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected DataPage<T> page;

  /** Field description */
  protected int pageSize;

  /** Field description */
  protected int rowIndex;
}
