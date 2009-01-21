/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.macro.Macro;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public class CodeMacro implements Macro
{

  /** Field description */
  public static final String NAME = "code";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param environment
   * @param body
   * @param parameters
   *
   * @return
   */
  public String excecute(Map<String, ?> environment, String body,
                         Map<String, String> parameters)
  {
    String lang = parameters.get("lang");

    if (Util.isBlank(lang))
    {
      lang = "java";
    }

    String linkPrefix = (String) environment.get("linkBase");

    linkPrefix += "resource/dp/syntax/";

    String gutterString = parameters.get("gutter");
    boolean gutter = (!Util.isBlank(gutterString)
                      && gutterString.equals("true"));
    String controlString = parameters.get("controls");
    boolean controls = (!Util.isBlank(controlString)
                        && controlString.equals("true"));

    return buildBody(lang, linkPrefix, body, gutter, controls);
  }

  /**
   * Method description
   *
   *
   * @param lang
   * @param linkPrefix
   * @param body
   * @param gutter
   * @param controls
   *
   * @return
   */
  private String buildBody(String lang, String linkPrefix, String body,
                           boolean gutter, boolean controls)
  {
    String result = "";

    result += "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + linkPrefix
              + "css/SyntaxHighlighter.css\"></link>\n";
    result += "<script language=\"javascript\" src=\"" + linkPrefix
              + "js/shCore.js\"></script>\n";
    lang = lang.toLowerCase();

    if (lang.equals("csharp") || lang.equals("c#") || lang.equals("c-sharp"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushCsharp.js\"></script>\n";
    }
    else if (lang.equals("cpp") || lang.equals("c++") || lang.equals("c"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushCpp.js\"></script>\n";
    }
    else if (lang.equals("css"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushCss.js\"></script>\n";
    }
    else if (lang.equals("delphi") || lang.equals("pascal"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushDelphi.js\"></script>\n";
    }
    else if (lang.equals("js") || lang.equals("javascript")
             || lang.equals("jscript"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushJScript.js\"></script>\n";
    }
    else if (lang.equals("java"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushJava.js\"></script>\n";
    }
    else if (lang.equals("php"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushPhp.js\"></script>\n";
    }
    else if (lang.equals("python") || lang.equals("py"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushPython.js\"></script>\n";
    }
    else if (lang.equals("ruby") || lang.equals("ror") || lang.equals("rb")
             || lang.equals("rails"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushRuby.js\"></script>\n";
    }
    else if (lang.equals("sql"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushSql.js\"></script>\n";
    }
    else if (lang.equals("vb") || lang.equals("vb.net"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushVb.js\"></script>\n";
    }
    else if (lang.equals("xml") || lang.equals("html") || lang.equals("xhtml")
             || lang.equals("xslt"))
    {
      result += "<script language=\"javascript\" src=\"" + linkPrefix
                + "js/shBrushXml.js\"></script>\n";
    }

    result += "<pre name=\"code\" class=\"" + lang + "\">\n";
    result += body;
    result += "</pre>\n";
    result += "<script language=\"javascript\">\n";
    result += "dp.SyntaxHighlighter.BloggerMode();\n";
    result += "dp.SyntaxHighlighter.HighlightAll('code', " + gutter + ", "
              + controls + ", false, 1, false);\n";
    result += "</script>\n";

    return result;
  }
}
