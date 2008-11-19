/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.resource;

//~--- non-JDK imports --------------------------------------------------------

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
public class ResourceRenderer extends BaseRenderer
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
    if (!(component instanceof ResourceComponent))
    {
      throw new IllegalArgumentException("wrong type");
    }

    ResourceComponent resource = (ResourceComponent) component;

    if (isRendered(context, resource))
    {
      String type = resource.getType();

      if (type == null)
      {
        type = "stylesheet";
      }

      ResponseWriter writer = context.getResponseWriter();

      if ((resource.getIePatch() != null)
          && resource.getIePatch().equals(Boolean.TRUE))
      {
        writer.write("<!--[if lte IE 7]>\n");
      }

      writer.startElement("link", component);

      if (type.equalsIgnoreCase("css") || type.equalsIgnoreCase("stylesheet"))
      {
        writer.writeAttribute("rel", "stylesheet", null);
        writer.writeAttribute("type", "text/css", null);
        writer.writeAttribute("class", "user", null);
      }
      else if (type.equalsIgnoreCase("rss"))
      {
        writer.writeAttribute("rel", "alternate", null);
        writer.writeAttribute("type", "application/rss+xml", type);
      }

      if (resource.getTitle() != null)
      {
        writer.writeAttribute("title", resource.getTitle(), null);
      }

      if (resource.getHref() != null)
      {
        writer.writeAttribute("href",
                              buildRelativeLink(context, resource.getHref()),
                              null);
      }

      writer.endElement("link");
      writer.write("\n");

      if ((resource.getIePatch() != null)
          && resource.getIePatch().equals(Boolean.TRUE))
      {
        writer.write("<![endif]-->");
      }
    }
  }
}
