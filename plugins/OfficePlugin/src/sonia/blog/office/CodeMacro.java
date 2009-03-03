/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class CodeMacro extends AbstractBlogMacro
{

  /** Field description */
  public static final String NAME = "code";

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param controls
   */
  public void setControls(String controls)
  {
    this.controls = controls;
  }

  /**
   * Method description
   *
   *
   * @param gutter
   */
  public void setGutter(String gutter)
  {
    this.gutter = gutter;
  }

  /**
   * Method description
   *
   *
   * @param lang
   */
  public void setLang(String lang)
  {
    this.lang = lang;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param facesContext
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  @Override
  protected String doBody(FacesContext facesContext, String linkBase,
                          ContentObject object, String body)
  {
    linkBase += "resource/dp/syntax/";

    String result = "";

    result += "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + linkBase
              + "css/SyntaxHighlighter.css\"></link>\n";
    result += "<script language=\"javascript\" src=\"" + linkBase
              + "js/shCore.js\"></script>\n";
    lang = lang.toLowerCase();

    if (lang.equals("csharp") || lang.equals("c#") || lang.equals("c-sharp"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushCsharp.js\"></script>\n";
    }
    else if (lang.equals("cpp") || lang.equals("c++") || lang.equals("c"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushCpp.js\"></script>\n";
    }
    else if (lang.equals("css"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushCss.js\"></script>\n";
    }
    else if (lang.equals("delphi") || lang.equals("pascal"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushDelphi.js\"></script>\n";
    }
    else if (lang.equals("js") || lang.equals("javascript")
             || lang.equals("jscript"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushJScript.js\"></script>\n";
    }
    else if (lang.equals("java"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushJava.js\"></script>\n";
    }
    else if (lang.equals("php"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushPhp.js\"></script>\n";
    }
    else if (lang.equals("python") || lang.equals("py"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushPython.js\"></script>\n";
    }
    else if (lang.equals("ruby") || lang.equals("ror") || lang.equals("rb")
             || lang.equals("rails"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushRuby.js\"></script>\n";
    }
    else if (lang.equals("sql"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushSql.js\"></script>\n";
    }
    else if (lang.equals("vb") || lang.equals("vb.net"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
                + "js/shBrushVb.js\"></script>\n";
    }
    else if (lang.equals("xml") || lang.equals("html") || lang.equals("xhtml")
             || lang.equals("xslt"))
    {
      result += "<script language=\"javascript\" src=\"" + linkBase
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String controls = "false";

  /** Field description */
  private String gutter = "false";

  /** Field description */
  private String lang = "java";
}
