/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.link;

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
public class LinkRenderer extends BaseRenderer
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
    if (!(component instanceof LinkComponent))
    {
      throw new IllegalArgumentException("wrong type");
    }

    ResponseWriter writer = context.getResponseWriter();
    LinkComponent link = (LinkComponent) component;

    if (isRendered(context, link))
    {
      renderBegin(context, link, writer);
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
    if (!(component instanceof LinkComponent))
    {
      throw new IllegalArgumentException("wrong type");
    }

    LinkComponent link = (LinkComponent) component;

    if (isRendered(context, link) && (link.getValue() == null)
        &&!link.getDisabled())
    {
      ResponseWriter writer = context.getResponseWriter();

      writer.endElement("a");
    }
  }

  /**
   * Method description
   *
   *
   * @param context
   * @param link
   * @param writer
   *
   * @throws IOException
   */
  private void renderBegin(FacesContext context, LinkComponent link,
                           ResponseWriter writer)
          throws IOException
  {
    boolean disabled = link.getDisabled();

    if (!disabled)
    {
      writer.startElement("a", link);
    }
    else
    {
      writer.startElement("span", link);
    }

    if (link.getStyle() != null)
    {
      writer.writeAttribute("style", link.getStyle(), null);
    }

    if (link.getStyleClass() != null)
    {
      writer.writeAttribute("class", link.getStyleClass(), null);
    }

    if (!disabled)
    {
      if (link.getTitle() != null)
      {
        writer.writeAttribute("title", link.getTitle(), null);
      }

      if (link.getTarget() != null)
      {
        writer.writeAttribute("target", link.getTarget(), null);
      }

      BlogRequest request =
        (BlogRequest) context.getExternalContext().getRequest();
      String uri = "#";
      LinkBuilder builder = BlogContext.getInstance().getLinkBuilder();

      if (link.getHref() != null)
      {
        uri = builder.buildLink(request, link.getHref());
      }
      else if (link.getObject() != null)
      {
        uri = builder.buildLink(request, link.getObject());
      }

      writer.writeAttribute("href", uri, null);
    }

    if (link.getValue() != null)
    {
      writer.write(link.getValue());

      if (!disabled)
      {
        writer.endElement("a");
      }
      else
      {
        writer.endElement("span");
      }
    }
  }
}
