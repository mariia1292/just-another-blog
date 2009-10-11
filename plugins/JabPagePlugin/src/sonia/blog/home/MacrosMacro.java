/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Context;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;
import sonia.macro.Macro;
import sonia.macro.MacroParser;
import sonia.macro.browse.MacroInformation;
import sonia.macro.browse.MacroInformationProvider;

/**
 *
 * @author Sebastian Sdorra
 */
public class MacrosMacro extends AbstractBlogMacro
{
  public static final String NAME = "macros";

  public static final String TEMPLATE = "/sonia/blog/home/template/macros.html";

  @Override
  protected String doBody(BlogRequest request, String linkBase, ContentObject object, String body)
  {
    Map<String,Object> parameters = new HashMap<String, Object>();

    parameters.put("macros", getMacros(request));
    parameters.put("style", style);
    parameters.put("styleClass", styleClass);
    return parseTemplate(parameters, TEMPLATE);
  }

  private List<MacroInformation> getMacros(BlogRequest request){
    List<MacroInformation> macros = new ArrayList<MacroInformation>();
    Locale locale = request.getLocale();
    MacroInformationProvider provider = macroParser.getInformationProvider();
    Iterator<Class<? extends Macro>> it = macroParser.getMacros();
    while ( it.hasNext() ){
      Class<? extends Macro> clazz = it.next();
      MacroInformation info = provider.getInformation(clazz, locale);
      if ( info != null ){
        macros.add(info);
      }
    }
    return macros;
  }

  public void setStyle(String style)
  {
    this.style = style;
  }

  public void setStyleClass(String styleClass)
  {
    this.styleClass = styleClass;
  }



  @Context
  private MacroParser macroParser;
  private String style;
  private String styleClass;
}
