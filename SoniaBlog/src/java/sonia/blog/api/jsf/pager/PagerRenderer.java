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


package sonia.blog.api.jsf.pager;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.base.BaseRenderer;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Sebastian Sdorra
 */
public class PagerRenderer extends BaseRenderer
{

  /**
   * Constructs ...
   *
   */
  public PagerRenderer() {}

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   */
  @Override
  public void decode(FacesContext context, UIComponent component)
  {
    if (!(component instanceof PagerComponent))
    {
      throw new IllegalArgumentException();
    }

    PagerComponent pager = (PagerComponent) component;

    if (isRendered(context, pager))
    {
      String id = pager.getClientId(context);
      Map<String, String> parameters =
        context.getExternalContext().getRequestParameterMap();
      String response = parameters.get(id);

      if (!Util.isBlank(response))
      {
        String dataTableId = pager.getFor();
        UIData data = (UIData) component.findComponent(dataTableId);
        Integer a = pager.getShowPages();
        int showPages = (a == null)
                        ? 0
                        : a.intValue();
        int first = data.getFirst();
        int itemCount = data.getRowCount();
        int pageSize = data.getRows();

        if (pageSize <= 0)
        {
          pageSize = itemCount;
        }

        if (response.equals("<"))
        {
          first -= pageSize;
        }
        else if (response.equals(">"))
        {
          first += pageSize;
        }
        else if (response.equals("<<"))
        {
          first -= pageSize * showPages;
        }
        else if (response.equals(">>"))
        {
          first += pageSize * showPages;
        }
        else
        {
          int page = Integer.parseInt(response);

          first = (page - 1) * pageSize;
        }

        if (first + pageSize > itemCount)
        {
          first = itemCount - pageSize;
        }

        if (first < 0)
        {
          first = 0;
        }

        data.setFirst(first);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   *
   * @throws IOException
   */
  @Override
  public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException
  {
    if (!(component instanceof PagerComponent))
    {
      throw new IllegalArgumentException();
    }

    PagerComponent pager = (PagerComponent) component;

    if (isRendered(context, pager))
    {
      String id = pager.getClientId(context);
      UIComponent parent = component;

      if (!(parent instanceof UIForm))
      {
        parent = component.getParent();
      }

      String formId = parent.getClientId(context);
      UIData dataTable = (UIData) pager.findComponent(pager.getFor());
      Integer a = pager.getShowPages();
      int showPages = (a == null)
                      ? 0
                      : a.intValue();
      ResponseWriter writer = context.getResponseWriter();
      int first = dataTable.getFirst();
      int itemCount = dataTable.getRowCount();
      int pageSize = dataTable.getRows();

      if (pageSize <= 0)
      {
        pageSize = itemCount;
      }

      int pages = itemCount / pageSize;

      if (itemCount % pageSize != 0)
      {
        pages++;
      }

      int currentPage = first / pageSize;

      if (first >= itemCount - pageSize)
      {
        currentPage = pages - 1;
      }

      int startPage = 0;
      int endPage = pages;

      if (showPages > 0)
      {
        startPage = (currentPage / showPages) * showPages;
        endPage = Math.min(startPage + showPages, pages);
      }

      writeStartDiv(writer, pager);

      if (currentPage > 0)
      {
        writeLink(writer, pager, formId, id, "<", false);
      }

      if (startPage > 0)
      {
        writeLink(writer, pager, formId, id, "<<", false);
      }

      for (int i = startPage; i < endPage; i++)
      {
        boolean selected = i == currentPage;

        writeLink(writer, pager, formId, id, "" + (i + 1), selected);
      }

      if (endPage < pages)
      {
        writeLink(writer, pager, formId, id, ">>", false);
      }

      if (first < itemCount - pageSize)
      {
        writeLink(writer, pager, formId, id, ">", false);
      }

      writeHiddenField(writer, component, id);
      writer.endElement("div");
    }
  }

  /**
   * Method description
   *
   *
   * @param formId
   * @param id
   * @param value
   *
   * @return
   */
  private String oncklickCode(String formId, String id, String value)
  {
    StringBuffer msg = new StringBuffer();

    msg.append("document.forms['").append(formId).append("']['").append(id);
    msg.append("'].value='").append(value).append("'; document.forms['");
    msg.append(formId).append("'].submit(); return false;");

    return msg.toString();
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param component
   * @param id
   *
   * @throws IOException
   */
  private void writeHiddenField(ResponseWriter writer, UIComponent component,
                                String id)
          throws IOException
  {
    writer.startElement("input", component);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name", id, null);
    writer.endElement("input");
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param pager
   * @param formId
   * @param id
   * @param value
   * @param selected
   *
   * @throws IOException
   */
  private void writeLink(ResponseWriter writer, PagerComponent pager,
                         String formId, String id, String value,
                         boolean selected)
          throws IOException
  {
    writer.writeText(" ", null);
    writer.startElement("a", pager);

    if (selected)
    {
      String style = pager.getSelectedStyle();

      if (!Util.isBlank(style))
      {
        writer.writeAttribute("style", style, null);
      }

      String styleClass = pager.getSelectedStyleClass();

      if (!Util.isBlank(styleClass))
      {
        writer.writeAttribute("class", styleClass, null);
      }
    }

    writer.writeAttribute("href", "#", null);
    writer.writeAttribute("onclick", oncklickCode(formId, id, value), null);
    writer.writeText(value, null);
    writer.endElement("a");
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param pager
   *
   * @throws IOException
   */
  private void writeStartDiv(ResponseWriter writer, PagerComponent pager)
          throws IOException
  {
    writer.startElement("div", pager);

    String style = pager.getStyle();

    if (!Util.isBlank(style))
    {
      writer.writeAttribute("sytle", style, null);
    }

    String styleClass = pager.getStyleClass();

    if (!Util.isBlank(styleClass))
    {
      writer.writeAttribute("class", styleClass, null);
    }
  }
}