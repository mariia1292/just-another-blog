/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.widget;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.lang.StringEscapeUtils;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.macro.browse.BlogMacroWidget;
import sonia.blog.entity.ContentObject;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseRenderer;

import sonia.macro.browse.MacroWidget;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class WidgetRenderer extends BaseRenderer
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(WidgetRenderer.class.getName());

  //~--- methods --------------------------------------------------------------

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
    if (!(component instanceof WidgetComponent))
    {
      throw new IllegalArgumentException(
          "component is not an instance of WidgetComponent");
    }

    WidgetComponent widgetComponent = (WidgetComponent) component;
    BlogMacroWidget widget = getWidget(widgetComponent);

    if (widget != null)
    {
      BlogRequest request = getRequest(context);
      ContentObject object = widgetComponent.getObject();
      String name = widgetComponent.getName();
      String param = widgetComponent.getParam();

      if (widgetComponent.getResult())
      {
        String result = widget.getResult(request, object, name, param);

        if (Util.hasContent(result))
        {
          result = escape(result);
          context.getResponseWriter().write(result);
        }
      }
      else
      {
        String formEl = widget.getFormElement(request, object, name, param);

        if (Util.hasContent(formEl))
        {
          context.getResponseWriter().write(formEl);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param result
   *
   * @return
   */
  private String escape(String result)
  {
    return StringEscapeUtils.escapeHtml(result).replace(" ",
            "&nbsp;").replace("'", "&#039;").replace("\n", "<br />").replace("\r", "");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   *
   * @return
   */
  private BlogRequest getRequest(FacesContext context)
  {
    return BlogUtil.getBlogRequest(context.getExternalContext().getRequest());
  }

  /**
   * Method description
   *
   *
   * @param component
   *
   * @return
   */
  private BlogMacroWidget getWidget(WidgetComponent component)
  {
    BlogMacroWidget widget = null;
    Class<? extends MacroWidget> macroClass = component.getWidget();

    if ((macroClass != null)
        && BlogMacroWidget.class.isAssignableFrom(macroClass))
    {
      try
      {
        widget = (BlogMacroWidget) macroClass.newInstance();
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }

    return widget;
  }
}
