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



package sonia.blog.devel;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.Context;
import sonia.blog.api.util.AbstractBean;

import sonia.cache.CacheManager;
import sonia.cache.ObjectCache;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Sebastian Sdorra
 */
public class DevCacheBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public DevCacheBean()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param event
   */
  public void clear(ActionEvent event)
  {
    CacheModelEntry entry = (CacheModelEntry) model.getRowData();

    if (entry != null)
    {
      entry.getCache().clear();
    }
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void clearAll(ActionEvent event)
  {
    cacheManger.clearAll();
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void reset(ActionEvent event)
  {
    CacheModelEntry entry = (CacheModelEntry) model.getRowData();

    if (entry != null)
    {
      entry.getCache().reset();
    }
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void resetAll(ActionEvent event)
  {
    cacheManger.resetAll();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getCaches()
  {
    model = new ListDataModel();

    Map<String, ObjectCache> caches = cacheManger.getCaches();

    if (Util.hasContent(caches))
    {
      List<CacheModelEntry> entries = new ArrayList<CacheModelEntry>();

      for (Entry<String, ObjectCache> entry : caches.entrySet())
      {
        entries.add(new CacheModelEntry(entry.getKey(), entry.getValue()));
      }

      model.setWrappedData(entries);
    }

    return model;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private CacheManager cacheManger;

  /** Field description */
  private DataModel model;
}
