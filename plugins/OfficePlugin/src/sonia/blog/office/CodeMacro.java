/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.LinkResource;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.entity.ContentObject;

import sonia.config.Config;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class CodeMacro extends AbstractBlogMacro implements WebMacro
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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<WebResource> getResources()
  {
    return resources;
  }

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
    resources = new ArrayList<WebResource>();

    // Core CSS
    resources.add(buildStyleSheet(70,
                                  linkBase
                                  + "resource/syntax/styles/shCore.css"));

    // Core JS
    resources.add(new ScriptResource(80,
                                     linkBase
                                     + "resource/syntax/scripts/shCore.js"));

    // Theme CSS
    if (theme.equalsIgnoreCase(THEME_DJANGO))
    {
      resources.add(
          buildStyleSheet(
            71, linkBase + "resource/syntax/styles/shThemeDjango.css"));
    }
    else if (theme.equalsIgnoreCase(THEME_EMACS))
    {
      resources.add(
          buildStyleSheet(
            72, linkBase + "resource/syntax/styles/shThemeEmacs.css"));
    }
    else if (theme.equalsIgnoreCase(THEME_FADETOGREY))
    {
      resources.add(
          buildStyleSheet(
            73, linkBase + "resource/syntax/styles/shThemeFadeToGrey.css"));
    }
    else if (theme.equalsIgnoreCase(THEME_MIDNIGHT))
    {
      resources.add(
          buildStyleSheet(
            74, linkBase + "resource/syntax/styles/shThemeMidnight.css"));
    }
    else if (theme.equalsIgnoreCase(THEME_RDARK))
    {
      resources.add(
          buildStyleSheet(
            75, linkBase + "resource/syntax/styles/shThemeRDark.css"));
    }
    else
    {
      resources.add(
          buildStyleSheet(
            76, linkBase + "resource/syntax/styles/shThemeDefault.css"));
    }

    lang = lang.toLowerCase();

    if (lang.equals("bash"))
    {
      resources.add(
          new ScriptResource(
              81, linkBase + "resource/syntax/scripts/shBrushBash.js"));
    }
    else if (lang.equals("csharp") || lang.equals("c#")
             || (lang.equals("c-sharp")))
    {
      resources.add(
          new ScriptResource(
              82, linkBase + "resource/syntax/scripts/shBrushCSharp.js"));
    }
    else if (lang.equals("cpp") || lang.equals("c++") || (lang.equals("c")))
    {
      resources.add(
          new ScriptResource(
              83, linkBase + "resource/syntax/scripts/shBrushCpp.js"));
    }
    else if (lang.equals("css"))
    {
      resources.add(
          new ScriptResource(
              84, linkBase + "resource/syntax/scripts/shBrushCss.js"));
    }
    else if (lang.equals("delphi") || (lang.equals("pascal")))
    {
      resources.add(
          new ScriptResource(
              85, linkBase + "resource/syntax/scripts/shBrushDelphi.js"));
    }
    else if (lang.equals("diff"))
    {
      resources.add(
          new ScriptResource(
              86, linkBase + "resource/syntax/scripts/shBrushDiff.js"));
    }
    else if (lang.equals("groovy"))
    {
      resources.add(
          new ScriptResource(
              87, linkBase + "resource/syntax/scripts/shBrushGroovy.js"));
    }
    else if (lang.equals("js") || lang.equals("javascript")
             || (lang.equals("jscript")))
    {
      resources.add(
          new ScriptResource(
              88, linkBase + "resource/syntax/scripts/shBrushJScript.js"));
    }
    else if (lang.equals("java"))
    {
      resources.add(
          new ScriptResource(
              89, linkBase + "resource/syntax/scripts/shBrushJava.js"));
    }
    else if (lang.equals("perl"))
    {
      resources.add(
          new ScriptResource(
              90, linkBase + "resource/syntax/scripts/shBrushPerl.js"));
    }
    else if (lang.equals("php"))
    {
      resources.add(
          new ScriptResource(
              91, linkBase + "resource/syntax/scripts/shBrushPhp.js"));
    }
    else if (lang.equals("plain"))
    {
      resources.add(
          new ScriptResource(
              92, linkBase + "resource/syntax/scripts/shBrushPlain.js"));
    }
    else if (lang.equals("python") || (lang.equals("py")))
    {
      resources.add(
          new ScriptResource(
              93, linkBase + "resource/syntax/scripts/shBrushPython.js"));
    }
    else if (lang.equals("ruby") || lang.equals("ror") || lang.equals("rb")
             || (lang.equals("rails")))
    {
      resources.add(
          new ScriptResource(
              94, linkBase + "resource/syntax/scripts/shBrushRuby.js"));
    }
    else if (lang.equals("scala"))
    {
      resources.add(
          new ScriptResource(
              95, linkBase + "resource/syntax/scripts/shBrushScala.js"));
    }
    else if (lang.equals("sql"))
    {
      resources.add(
          new ScriptResource(
              96, linkBase + "resource/syntax/scripts/shBrushSql.js"));
    }
    else if (lang.equals("vb") || (lang.equals("vb.net")))
    {
      resources.add(
          new ScriptResource(
              97, linkBase + "resource/syntax/scripts/shBrushVb.js"));
    }
    else if (lang.equals("xml") || lang.equals("html") || lang.equals("xhtml")
             || (lang.equals("xslt")))
    {
      resources.add(
          new ScriptResource(
              98, linkBase + "resource/syntax/scripts/shBrushXml.js"));
    }
    else if (lang.equals("actionscript3") || lang.equals("as3"))
    {
      resources.add(
          new ScriptResource(
              99, linkBase + "resource/syntax/scripts/shBrushAS3.js"));
    }
    else if (lang.equals("jfx") || lang.equals("javafx"))
    {
      resources.add(
          new ScriptResource(
              100, linkBase + "resource/syntax/scripts/shBrushJavaFX.js"));
    }
    else if (lang.equals("powershell") || lang.equals("ps"))
    {
      resources.add(
          new ScriptResource(
              101, linkBase + "resource/syntax/scripts/shBrushPowerShell.js"));
    }

    StringBuffer result = new StringBuffer();

    result.append("<script type=\"text/javascript\">\n");
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

  /**
   * Method description
   *
   *
   *
   * @param index
   * @param href
   *
   * @return
   */
  private LinkResource buildStyleSheet(int index, String href)
  {
    LinkResource stylesheet = new LinkResource(index);

    stylesheet.setType(LinkResource.TYPE_STYLESHEET);
    stylesheet.setRel(LinkResource.REL_STYLESHEET);
    stylesheet.setHref(href);

    return stylesheet;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String collapse = "false";

  /** Field description */
  private String gutter = "true";

  /** Field description */
  private String lang = "java";

  /** Field description */
  private List<WebResource> resources;

  /** Field description */
  private String tabSize = "2";

  /** Field description */
  @Config(CONFIG_THEME)
  private String theme = "default";

  /** Field description */
  private String toolbar = "true";
}
