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


package sonia.blog.wui.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.model.DataModel;

/**
 *
 * @author Sebastian Sdorra
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