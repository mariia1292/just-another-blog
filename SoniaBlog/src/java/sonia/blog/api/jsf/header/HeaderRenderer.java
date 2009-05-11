/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.header;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.macro.WebResource;

import sonia.jsf.base.BaseRenderer;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public class HeaderRenderer extends BaseRenderer
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
    if (!(component instanceof HeaderComponent))
    {
      throw new IllegalArgumentException();
    }

    HeaderComponent header = (HeaderComponent) component;
    ResponseWriter writer = context.getResponseWriter();

    if (header.getComments())
    {
      writer.writeComment("start header");
    }

    List<WebResource> resources = header.getResources(context);

    if (Util.hasContent(resources))
    {
      for (WebResource resource : resources)
      {
        writer.write(resource.toHTML());
        writer.write("\n");
      }
    }

    if (header.getComments())
    {
      writer.writeComment("end header");
    }
  }
}
