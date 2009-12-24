/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.home;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Context;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

import sonia.macro.MacroFactory;
import sonia.macro.MacroParser;
import sonia.macro.browse.MacroInformation;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class MacrosMacro extends AbstractBlogMacro
{

  /** Field description */
  public static final String NAME = "macros";

  /** Field description */
  public static final String TEMPLATE = "/sonia/blog/home/template/macros.html";

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param style
   */
  public void setStyle(String style)
  {
    this.style = style;
  }

  /**
   * Method description
   *
   *
   * @param styleClass
   */
  public void setStyleClass(String styleClass)
  {
    this.styleClass = styleClass;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  @Override
  protected String doBody(BlogRequest request, String linkBase,
                          ContentObject object, String body)
  {
    Map<String, Object> parameters = new HashMap<String, Object>();

    parameters.put("macros", getMacros(request));
    parameters.put("style", style);
    parameters.put("styleClass", styleClass);

    return parseTemplate(parameters, TEMPLATE);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private List<MacroInformation> getMacros(BlogRequest request)
  {
    List<MacroInformation> macros = new ArrayList<MacroInformation>();
    Locale locale = request.getLocale();
    Iterator<MacroFactory> it = macroParser.getMacroFactories();

    while (it.hasNext())
    {
      MacroFactory factory = it.next();
      MacroInformation info = factory.getInformation(locale);

      if (info != null)
      {
        macros.add(info);
      }
    }

    Collections.sort(macros, new MacroComparator());

    return macros;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/10/12
   * @author         Enter your name here...
   */
  private class MacroComparator implements Comparator<MacroInformation>
  {

    /**
     * Method description
     *
     *
     * @param o1
     * @param o2
     *
     * @return
     */
    public int compare(MacroInformation o1, MacroInformation o2)
    {
      return o1.getDisplayName().compareTo(o2.getDisplayName());
    }
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private MacroParser macroParser;

  /** Field description */
  private String style;

  /** Field description */
  private String styleClass;
}
