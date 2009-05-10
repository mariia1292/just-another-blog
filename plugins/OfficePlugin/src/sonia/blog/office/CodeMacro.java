/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

import sonia.config.Config;

/**
 *
 * @author sdorra
 */
public class CodeMacro extends AbstractBlogMacro
{

  /** Field description */
  public static final String CONFIG_THEME = "office.code.theme";

  /** Field description */
  public static final String LANG_BASH = "bash";

  /** Field description */
  public static final String LANG_CPP = "c++";

  /** Field description */
  public static final String LANG_CSAHRP = "c#";

  /** Field description */
  public static final String LANG_CSS = "css";

  /** Field description */
  public static final String LANG_DELPHI = "delphi";

  /** Field description */
  public static final String LANG_DIFF = "diff";

  /** Field description */
  public static final String LANG_GROOVY = "groovy";

  /** Field description */
  public static final String LANG_JAVA = "java";

  /** Field description */
  public static String LANG_JS = "js";

  /** Field description */
  public static final String LANG_PERL = "perl";

  /** Field description */
  public static final String LANG_PHP = "php";

  /** Field description */
  public static final String LANG_PLAIN = "plain";

  /** Field description */
  public static final String LANG_PYTHON = "python";

  /** Field description */
  public static final String LANG_RUBY = "ruby";

  /** Field description */
  public static final String LANG_SCALA = "scala";

  /** Field description */
  public static final String LANG_SQL = "sql";

  /** Field description */
  public static final String LANG_VB = "vb";

  /** Field description */
  public static final String LANG_XML = "xml";

  /** Field description */
  public static final String NAME = "code";

  /** Field description */
  public static final String THEME_DEFAULT = "default";

  /** Field description */
  public static final String THEME_DJANGO = "django";

  /** Field description */
  public static final String THEME_EMACS = "emacs";

  /** Field description */
  public static final String THEME_FADETOGREY = "fadetogrey";

  /** Field description */
  public static final String THEME_MIDNIGHT = "midnight";

  /** Field description */
  public static final String THEME_RDARK = "rdark";

  /** Field description */
  private static final String CSS_PREFIX = "addCSS(\"";

  /** Field description */
  private static final String JS_PREFIX = "addScript(\"";

  /** Field description */
  private static final String SUFFIX = "\")\n";

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
   *
   * @param request
   * @param linkBase
   * @param object
   * @param body
   *
   * @return
   */
  @Override
  @SuppressWarnings("unchecked")
  protected String doBody(BlogRequest request, String linkBase,
                          ContentObject object, String body)
  {
    StringBuffer jsPrefix = new StringBuffer();

    jsPrefix.append(JS_PREFIX).append(linkBase);
    jsPrefix.append("resource/syntax/scripts/");

    StringBuffer cssPrefix = new StringBuffer();

    cssPrefix.append(CSS_PREFIX).append(linkBase);
    cssPrefix.append("resource/syntax/styles/");

    StringBuffer result = new StringBuffer();

    result.append("<script type=\"text/javascript\">\n");

    // Core CSS
    result.append(cssPrefix).append("shCore.css").append(SUFFIX);

    // Core JS
    result.append(jsPrefix).append("shCore.js").append(SUFFIX);

    // Theme CSS
    if (theme.equalsIgnoreCase(THEME_DJANGO))
    {
      result.append(cssPrefix).append("shThemeDjango.css").append(SUFFIX);
    }
    else if (theme.equalsIgnoreCase(THEME_EMACS))
    {
      result.append(cssPrefix).append("shThemeEmacs.css").append(SUFFIX);
    }
    else if (theme.equalsIgnoreCase(THEME_FADETOGREY))
    {
      result.append(cssPrefix).append("shThemeFadeToGrey.css").append(SUFFIX);
    }
    else if (theme.equalsIgnoreCase(THEME_MIDNIGHT))
    {
      result.append(cssPrefix).append("shThemeMidnight.css").append(SUFFIX);
    }
    else if (theme.equalsIgnoreCase(THEME_RDARK))
    {
      result.append(cssPrefix).append("shThemeRDark.css").append(SUFFIX);
    }
    else
    {
      result.append(cssPrefix).append("shThemeDefault.css").append(SUFFIX);
    }

    lang = lang.toLowerCase();

    if (lang.equals("bash"))
    {
      result.append(jsPrefix).append("shBrushBash.js").append(SUFFIX);
    }
    else if (lang.equals("csharp") || lang.equals("c#")
             || (lang.equals("c-sharp")))
    {
      result.append(jsPrefix).append("shBrushCSharp.js").append(SUFFIX);
    }
    else if (lang.equals("cpp") || lang.equals("c++") || (lang.equals("c")))
    {
      result.append(jsPrefix).append("shBrushCpp.js").append(SUFFIX);
    }
    else if (lang.equals("css"))
    {
      result.append(jsPrefix).append("shBrushCss.js").append(SUFFIX);
      result.append(LANG_CSS);
    }
    else if (lang.equals("delphi") || (lang.equals("pascal")))
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
             || (lang.equals("jscript")))
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
    else if (lang.equals("python") || (lang.equals("py")))
    {
      result.append(jsPrefix).append("shBrushPython.js").append(SUFFIX);
    }
    else if (lang.equals("ruby") || lang.equals("ror") || lang.equals("rb")
             || (lang.equals("rails")))
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
    else if (lang.equals("vb") || (lang.equals("vb.net")))
    {
      result.append(jsPrefix).append("shBrushVb.js").append(SUFFIX);
    }
    else if (lang.equals("xml") || lang.equals("html") || lang.equals("xhtml")
             || (lang.equals("xslt")))
    {
      result.append(jsPrefix).append("shBrushXml.js").append(SUFFIX);
    }

    result.append("SyntaxHighlighter.config.clipboardSwf = '");
    result.append(linkBase).append("resource/syntax/scripts/clipboard.swf';\n");
    result.append("SyntaxHighlighter.config.bloggerMode = true;\n");
    result.append("SyntaxHighlighter.all();\n");
    result.append("</script>\n");
    result.append("<pre class=\"brush: ").append(lang);
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
  @Config(CONFIG_THEME)
  private String theme = "default";

  /** Field description */
  private String toolbar = "true";
}
