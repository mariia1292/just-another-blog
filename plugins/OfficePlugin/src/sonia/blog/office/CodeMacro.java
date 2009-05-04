/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

import sonia.config.Config;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class CodeMacro extends AbstractBlogMacro
{

  /** Field description */
  public static final String ATTRIBUTE_VAR = "sonia.office.code";

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
  @SuppressWarnings("unchecked")
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

    result.append("\n");

    boolean appendCore = false;
    Map<String, Object> requestMap =
      facesContext.getExternalContext().getRequestMap();
    Set<String> ca = (Set<String>) requestMap.get(ATTRIBUTE_VAR);

    if (ca == null)
    {
      appendCore = true;
      ca = new HashSet<String>();
    }

    if (appendCore)
    {

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
    }

    lang = lang.toLowerCase();

    if (lang.equals("bash") &&!ca.contains(LANG_BASH))
    {
      result.append(jsPrefix).append("shBrushBash.js").append(SUFFIX);
      ca.add(LANG_BASH);
    }
    else if (lang.equals("csharp") || lang.equals("c#")
             || (lang.equals("c-sharp") &&!ca.contains(LANG_CSAHRP)))
    {
      result.append(jsPrefix).append("shBrushCSharp.js").append(SUFFIX);
    }
    else if (lang.equals("cpp") || lang.equals("c++")
             || (lang.equals("c") &&!ca.contains(LANG_CPP)))
    {
      result.append(jsPrefix).append("shBrushCpp.js").append(SUFFIX);
      result.append(LANG_CPP);
    }
    else if (lang.equals("css") &&!ca.contains(LANG_CSS))
    {
      result.append(jsPrefix).append("shBrushCss.js").append(SUFFIX);
      result.append(LANG_CSS);
    }
    else if (lang.equals("delphi")
             || (lang.equals("pascal") &&!ca.contains(LANG_DELPHI)))
    {
      result.append(jsPrefix).append("shBrushDelphi.js").append(SUFFIX);
      ca.add(LANG_DELPHI);
    }
    else if (lang.equals("diff") &&!ca.contains(LANG_DIFF))
    {
      result.append(jsPrefix).append("shBrushDiff.js").append(SUFFIX);
      ca.add(LANG_DIFF);
    }
    else if (lang.equals("groovy") &&!ca.contains(LANG_GROOVY))
    {
      result.append(jsPrefix).append("shBrushGroovy.js").append(SUFFIX);
      ca.add(LANG_GROOVY);
    }
    else if (lang.equals("js") || lang.equals("javascript")
             || (lang.equals("jscript") &&!ca.contains(LANG_JS)))
    {
      result.append(jsPrefix).append("shBrushJScript.js").append(SUFFIX);
      ca.add(LANG_JS);
    }
    else if (lang.equals("java") &&!ca.contains(LANG_JAVA))
    {
      result.append(jsPrefix).append("shBrushJava.js").append(SUFFIX);
      ca.add(LANG_JAVA);
    }
    else if (lang.equals("perl") &&!ca.contains(LANG_PERL))
    {
      result.append(jsPrefix).append("shBrushPerl.js").append(SUFFIX);
      ca.add(LANG_PERL);
    }
    else if (lang.equals("php") &&!ca.contains(LANG_PHP))
    {
      result.append(jsPrefix).append("shBrushPhp.js").append(SUFFIX);
      ca.add(LANG_PHP);
    }
    else if (lang.equals("plain") &&!ca.contains(LANG_PLAIN))
    {
      result.append(jsPrefix).append("shBrushPlain.js").append(SUFFIX);
      ca.add(LANG_PLAIN);
    }
    else if (lang.equals("python")
             || (lang.equals("py") &&!ca.contains(LANG_PYTHON)))
    {
      result.append(jsPrefix).append("shBrushPython.js").append(SUFFIX);
      ca.add(LANG_PYTHON);
    }
    else if (lang.equals("ruby") || lang.equals("ror") || lang.equals("rb")
             || (lang.equals("rails") &&!ca.contains(LANG_RUBY)))
    {
      result.append(jsPrefix).append("shBrushRuby.js").append(SUFFIX);
      ca.add(LANG_RUBY);
    }
    else if (lang.equals("scala") &&!ca.contains(LANG_SCALA))
    {
      result.append(jsPrefix).append("shBrushScala.js").append(SUFFIX);
      ca.add(LANG_SCALA);
    }
    else if (lang.equals("sql") &&!ca.contains(LANG_SQL))
    {
      result.append(jsPrefix).append("shBrushSql.js").append(SUFFIX);
      ca.add(LANG_SQL);
    }
    else if (lang.equals("vb")
             || (lang.equals("vb.net") &&!ca.contains(LANG_VB)))
    {
      result.append(jsPrefix).append("shBrushVb.js").append(SUFFIX);
      ca.add(LANG_VB);
    }
    else if (lang.equals("xml") || lang.equals("html") || lang.equals("xhtml")
             || (lang.equals("xslt") &&!ca.contains(LANG_XML)))
    {
      result.append(jsPrefix).append("shBrushXml.js").append(SUFFIX);
      ca.add(LANG_XML);
    }

    if (appendCore)
    {
      result.append("<script language=\"javascript\">\n");
      result.append("SyntaxHighlighter.config.clipboardSwf = '");
      result.append(linkBase).append(
          "resource/syntax/scripts/clipboard.swf';\n");
      result.append("SyntaxHighlighter.config.bloggerMode = true;\n");
      result.append("SyntaxHighlighter.all();\n");
      result.append("</script>\n");
    }

    result.append("<pre name=\"code\" class=\"brush: ").append(lang);
    result.append("; toolbar: ").append(toolbar).append("; gutter: ");
    result.append(gutter).append("; collapse: ").append(collapse);
    result.append("; tab-size: ").append(tabSize);
    result.append(";\">\n").append(body).append("\n</pre>\n");
    requestMap.put(ATTRIBUTE_VAR, ca);

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
