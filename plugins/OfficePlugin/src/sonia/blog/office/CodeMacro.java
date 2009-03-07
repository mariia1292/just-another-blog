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

  /** Field description */
  private static final String CSS_PREFIX =
    "<link type=\"text/css\" rel=\"stylesheet\" href=\"";

  /** Field description */
  private static final String JS_PREFIX =
    "<script language=\"javascript\" src=\"";

  /** Field description */
  private static final String SUFFIX = "\"></script>\n";

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param collapse
   */
  public void setCollapse(String collapse)
  {
    this.collapse = collapse;
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

  /**
   * Method description
   *
   *
   * @param tabSize
   */
  public void setTabSize(String tabSize)
  {
    this.tabSize = tabSize;
  }

  /**
   * Method description
   *
   *
   * @param toolbar
   */
  public void setToolbar(String toolbar)
  {
    this.toolbar = toolbar;
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
    StringBuffer jsPrefix = new StringBuffer();

    jsPrefix.append(JS_PREFIX).append(linkBase);
    jsPrefix.append("resource/syntax/scripts/");

    StringBuffer cssPrefix = new StringBuffer();

    cssPrefix.append(CSS_PREFIX).append(linkBase);
    cssPrefix.append("resource/syntax/styles/");

    StringBuffer result = new StringBuffer();

    // Core CSS
    result.append(cssPrefix).append("shCore.css").append(SUFFIX);
    result.append(cssPrefix).append("shThemeDefault.css").append(SUFFIX);

    // Core JS
    result.append(jsPrefix).append("shCore.js").append(SUFFIX);
    result.append(jsPrefix).append("shBrushXml.js").append(SUFFIX);
    lang = lang.toLowerCase();

    if (lang.equals("bash"))
    {
      result.append(jsPrefix).append("shBrushBash.js").append(SUFFIX);
    }
    else if (lang.equals("csharp") || lang.equals("c#")
             || lang.equals("c-sharp"))
    {
      result.append(jsPrefix).append("shBrushCSharp.js").append(SUFFIX);
    }
    else if (lang.equals("cpp") || lang.equals("c++") || lang.equals("c"))
    {
      result.append(jsPrefix).append("shBrushCpp.js").append(SUFFIX);
    }
    else if (lang.equals("css"))
    {
      result.append(jsPrefix).append("shBrushCss.js").append(SUFFIX);
    }
    else if (lang.equals("delphi") || lang.equals("pascal"))
    {
      result.append(jsPrefix).append("shBrushDelphi.js").append(SUFFIX);
    }
    else if (lang.equals("diff"))
    {
      result.append(jsPrefix).append("shBrushDiff.js").append(SUFFIX);
    }
    else if (lang.equals("groovy"))
    {
      result.append(jsPrefix).append("shBrushGroovy.js").append(SUFFIX);
    }
    else if (lang.equals("js") || lang.equals("javascript")
             || lang.equals("jscript"))
    {
      result.append(jsPrefix).append("shBrushJScript.js").append(SUFFIX);
    }
    else if (lang.equals("java"))
    {
      result.append(jsPrefix).append("shBrushJava.js").append(SUFFIX);
    }
    else if (lang.equals("perl"))
    {
      result.append(jsPrefix).append("shBrushPerl.js").append(SUFFIX);
    }
    else if (lang.equals("php"))
    {
      result.append(jsPrefix).append("shBrushPhp.js").append(SUFFIX);
    }
    else if (lang.equals("plain"))
    {
      result.append(jsPrefix).append("shBrushPlain.js").append(SUFFIX);
    }
    else if (lang.equals("python") || lang.equals("py"))
    {
      result.append(jsPrefix).append("shBrushPython.js").append(SUFFIX);
    }
    else if (lang.equals("ruby") || lang.equals("ror") || lang.equals("rb")
             || lang.equals("rails"))
    {
      result.append(jsPrefix).append("shBrushRuby.js").append(SUFFIX);
    }
    else if (lang.equals("scala"))
    {
      result.append(jsPrefix).append("shBrushScala.js").append(SUFFIX);
    }
    else if (lang.equals("sql"))
    {
      result.append(jsPrefix).append("shBrushSql.js").append(SUFFIX);
    }
    else if (lang.equals("vb") || lang.equals("vb.net"))
    {
      result.append(jsPrefix).append("shBrushVb.js").append(SUFFIX);
    }
    else if (lang.equals("xml") || lang.equals("html") || lang.equals("xhtml")
             || lang.equals("xslt"))
    {
      result.append(jsPrefix).append("shBrushXml.js").append(SUFFIX);
    }

    result.append("<script language=\"javascript\">\n");
    result.append("SyntaxHighlighter.config.clipboardSwf = '");
    result.append(linkBase).append("resource/syntax/scripts/clipboard.swf';\n");
    result.append("SyntaxHighlighter.config.bloggerMode = true;\n");
    result.append("SyntaxHighlighter.all();\n");
    result.append("</script>\n");
    result.append("<pre name=\"code\" class=\"brush: ").append(lang);
    result.append("; toolbar: ").append(toolbar).append("; gutter: ");
    result.append(gutter).append("; collapse: ").append(collapse);
    result.append("; tab-size: ").append(tabSize);
    result.append(";\">\n").append(body).append("\n</pre>\n");

    return result.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String collapse = "false";

  /** Field description */
  private String gutter = "true";

  /** Field description */
  private String lang = "java";

  /** Field description */
  private String tabSize = "2";

  /** Field description */
  private String toolbar = "true";
}
