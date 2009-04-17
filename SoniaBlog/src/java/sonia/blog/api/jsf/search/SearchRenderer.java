/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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

      writer.endElement("form");
    }
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
    writer.writeAttribute("type", "text", null);
    writer.writeAttribute("name", "search", null);

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
