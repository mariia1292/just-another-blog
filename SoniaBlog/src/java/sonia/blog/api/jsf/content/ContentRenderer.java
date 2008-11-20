/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.content;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Entry;
import sonia.blog.wui.BlogBean;

import sonia.jsf.base.BaseRenderer;

import sonia.macro.MacroParser;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public class ContentRenderer extends BaseRenderer
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
    if (!(component instanceof ContentComponent))
    {
      throw new IllegalArgumentException("wrong type");
    }

    ContentComponent contentComponent = (ContentComponent) component;

    if (isRendered(context, contentComponent))
    {
      Entry entry = contentComponent.getEntry();

      if (entry != null)
      {
        BlogBean blogBean =
          (BlogBean) context.getExternalContext().getSessionMap().get(
              "BlogBean");
        String content = null;

        if ((contentComponent.getTeaser() != null)
            && contentComponent.getTeaser() &&!isBlank(entry.getTeaser()))
        {
          content = entry.getTeaser();
        }
        else
        {
          content = entry.getContent();
        }

        if ((blogBean != null) && blogBean.getBlog().isAllowMacros())
        {
          BlogRequest request =
            (BlogRequest) context.getExternalContext().getRequest();
          Map<String, Object> environment = new HashMap<String, Object>();

          environment.put("facesContext", context);
          environment.put("request", request);

          if (blogBean != null)
          {
            environment.put("blog", blogBean.getBlog());
            environment.put("entry", entry);
          }

          MacroParser parser = MacroParser.getInstance();

          content = parser.parseText(environment, content);
        }

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", component);

        if (contentComponent.getStyle() != null)
        {
          writer.writeAttribute("style", contentComponent.getStyle(), null);
        }

        if (contentComponent.getStyleClass() != null)
        {
          writer.writeAttribute("class", contentComponent.getStyleClass(),
                                null);
        }

        writer.write(content);
        writer.endElement("div");
      }
    }
  }
}
