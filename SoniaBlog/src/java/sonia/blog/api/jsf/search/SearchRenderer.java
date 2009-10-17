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



package sonia.blog.api.jsf.search;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;

import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author Sebastian Sdorra
 */
public class SearchRenderer extends BaseRenderer
{

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
    if (!(component instanceof SearchComponent))
    {
      throw new IllegalArgumentException("wrong type");
    }

    ResponseWriter writer = context.getResponseWriter();
    SearchComponent searchCompnent = (SearchComponent) component;

    if (isRendered(context, searchCompnent))
    {
      renderBegin(context, searchCompnent, writer);

      if (searchCompnent.isAutoComplete())
      {
        renderAutoComplete(context, searchCompnent, writer);
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
  public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException
  {
    if (!(component instanceof SearchComponent))
    {
      throw new IllegalArgumentException("wrong type");
    }

    SearchComponent searchComponent = (SearchComponent) component;

    if (isRendered(context, searchComponent))
    {
      ResponseWriter writer = context.getResponseWriter();

      writer.endElement("div");
      writer.endElement("form");
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param searchCompnent
   * @param writer
   *
   * @throws IOException
   */
  private void renderAutoComplete(FacesContext context,
                                  SearchComponent searchCompnent,
                                  ResponseWriter writer)
          throws IOException
  {
    writer.startElement("script", searchCompnent);
    writer.writeAttribute("type", "text/javascript", null);
    writer.write("if (typeof jQuery != 'undefined') { ");

    if (context.getExternalContext().getRequestMap().get(
            "sonia.blog.resource.autocomplete") == null)
    {
      writer.write("$(\"head\").append(\"<link rel=\\\"stylesheet\\\" ");
      writer.write("type=\\\"text/css\\\" href=\\\"");

      // write href
      writer.write(context.getExternalContext().getRequestContextPath());
      writer.write("/resources/jquery/plugins/css/autocomplete.css");
      writer.write("\\\" /> \"); ");
      writer.write("$.getScript(\"");
      writer.write(context.getExternalContext().getRequestContextPath());
      writer.write("/resources/jquery/plugins/js/jquery.autocomplete.js");
      writer.write("\", function(){ ");
      renderAutoCompleteScript(context, searchCompnent, writer);
      writer.write("}); ");
      context.getExternalContext().getRequestMap().put(
          "sonia.blog.resource.autocomplete", Boolean.TRUE);
    }
    else
    {
      writer.write("$.ready((function(){");
      renderAutoCompleteScript(context, searchCompnent, writer);
      writer.write("}));");
    }

    writer.write("}");
    writer.endElement("script");
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param searchCompnent
   * @param writer
   *
   * @throws IOException
   */
  private void renderAutoCompleteScript(FacesContext context,
          SearchComponent searchCompnent, ResponseWriter writer)
          throws IOException
  {
    String clientId = searchCompnent.getClientId(context);

    clientId = clientId.replaceAll(":", "\\:");
    writer.write("$(\"#");
    writer.write(clientId);
    writer.write("\").autocomplete(\"");
    writer.write(context.getExternalContext().getRequestContextPath());
    writer.write("/quicksearch.json\", {id: \"result_");
    writer.write(clientId);
    writer.write("\"});");
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param searchComponent
   * @param writer
   *
   * @throws IOException
   */
  private void renderBegin(FacesContext context,
                           SearchComponent searchComponent,
                           ResponseWriter writer)
          throws IOException
  {
    BlogRequest request =
      (BlogRequest) context.getExternalContext().getRequest();
    boolean link = (searchComponent.getType() != null)
                   && searchComponent.getType().equals("link");
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String searchUri = linkBuilder.buildLink(request, "/search.jab");

    writer.startElement("form", searchComponent);
    writer.writeAttribute("action", searchUri, null);
    writer.writeAttribute("method", "get", null);
    writer.startElement("div", null);

    if (searchComponent.getStyle() != null)
    {
      writer.writeAttribute("style", searchComponent.getStyle(), null);
    }

    if (searchComponent.getStyleClass() != null)
    {
      writer.writeAttribute("class", searchComponent.getStyleClass(), null);
    }

    if (searchComponent.getTarget() != null)
    {
      writer.writeAttribute("target", searchComponent.getTarget(), null);
    }

    writer.startElement("input", searchComponent);
    writer.writeAttribute("id", searchComponent.getClientId(context), null);
    writer.writeAttribute("type", "text", null);
    writer.writeAttribute("name", "search", null);

    if (searchComponent.isAutoComplete())
    {
      writer.writeAttribute("autocomplete", "off", null);
    }

    String value = searchComponent.getValue();

    if (value == null)
    {
      value = "";
    }

    writer.writeAttribute("value", value, null);
    writer.endElement("input");

    if (link)
    {
      writer.startElement("a", searchComponent);
      writer.writeAttribute("onclick", "submit();", null);
    }
    else
    {
      writer.startElement("input", searchComponent);
      writer.writeAttribute("type", "submit", null);
    }

    if (searchComponent.getTitle() != null)
    {
      writer.writeAttribute("title", searchComponent.getTitle(), null);
    }

    if (link)
    {
      if (searchComponent.getValue() != null)
      {
        writer.write(searchComponent.getValue());
        writer.endElement("a");
      }
    }
    else
    {
      if (searchComponent.getLabel() != null)
      {
        writer.writeAttribute("value", searchComponent.getLabel(), null);
      }

      writer.endElement("input");
    }
  }
}
