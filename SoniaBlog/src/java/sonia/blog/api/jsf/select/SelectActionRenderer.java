/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.select;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.util.SelectAction;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseRenderer;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.text.MessageFormat;

import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public class SelectActionRenderer extends BaseRenderer
{

  /** Field description */
  private static final String OPTION = "this.options[this.selectedIndex].value";

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
  public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException
  {
    SelectActionComponent cmp = null;

    if (!(component instanceof SelectActionComponent))
    {
      throw new IllegalArgumentException(
          "component is not an instance of "
          + SelectActionComponent.class.getName());
    }

    cmp = (SelectActionComponent) component;

    if (isRendered(context, cmp))
    {
      Map<String, ?> parameter = getParameters(component);
      Object requestObject = context.getExternalContext().getRequest();
      BlogRequest request = BlogUtil.getBlogRequest(requestObject);
      List<SelectAction> actions = cmp.getActions();
      ResponseWriter writer = context.getResponseWriter();

      writer.startElement("select", cmp);

      if (!Util.isBlank(cmp.getOnchange()))
      {
        String out = MessageFormat.format(cmp.getOnchange(), OPTION);

        writer.writeAttribute("onchange", out, null);
      }

      writer.startElement("option", cmp);
      writer.writeAttribute("value", "---", null);
      writer.write("---");
      writer.endElement("option");

      for (SelectAction action : actions)
      {
        writer.startElement("option", cmp);
        writer.writeAttribute("value", action.getOutput(request, parameter),
                              null);
        writer.writeText(action.getLable(), null);
        writer.endElement("option");
      }

      writer.endElement("select");
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public boolean getRendersChildren()
  {
    return true;
  }
}
