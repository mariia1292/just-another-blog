/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.Dao;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.api.macro.LinkResource;
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.macro.browse.CheckboxWidget;
import sonia.blog.api.macro.browse.SelectWidget;
import sonia.blog.api.macro.browse.StringInputWidget;
import sonia.blog.api.macro.browse.StringTextAreaWidget;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;

import sonia.macro.browse.MacroInfo;
import sonia.macro.browse.MacroInfoParameter;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
@MacroInfo(
  name = "code",
  displayName = "macro.code.displayName",
  description = "macro.code.description",
  resourceBundle = "sonia.blog.resources.label",
  bodyWidget = StringTextAreaWidget.class,
  widgetParam = "cols=110;rows=25"
)
public class CodeMacro extends AbstractBlogMacro implements WebMacro
{

  /** Field description */
  public static final String CONFIG_THEME = "office.code.theme";

  /** Field description */
  public static final String LANG_BASH = "bash";

  /** Field description */
  public static final String LANG_COLDFUSION = "coldfusion";

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
  public static final String LANG_ERLANG = "erlang";

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
  public static final String THEME_ECLIPSE = "eclipse";

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
  @MacroInfoParameter(
    displayName = "macro.code.lang.displayName",
    description = "macro.code.lang.description",
    widget = SelectWidget.class,
    widgetParam = "options=bash|c++|c#|coldfusion|css|delphi|diff|erlang|groovy|*java|javaScript:js|perl|python|ruby|scala|sql|vb|xml"
  )
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
  @MacroInfoParameter(
    displayName = "macro.code.tabSize.displayName",
    description = "macro.code.tabSize.description",
    widget = StringInputWidget.class,
    widgetParam = "regex=[0-9]+"
  )
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
  @MacroInfoParameter(
    displayName = "macro.code.toolbar.displayName",
    description = "macro.code.toolbar.description",
    widget = CheckboxWidget.class,
    widgetParam = "checked=true"
  )
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
    resources.add(
        buildStyleSheet(
          70, linkBase + "resources/syntaxhighlighter/styles/shCore.css"));

    // Core JS
    resources.add(
        new ScriptResource(
            80, linkBase + "resources/syntaxhighlighter/scripts/shCore.js"));
    theme = getTheme(request.getCurrentBlog());

    // Theme CSS
    if (theme.equalsIgnoreCase(THEME_DJANGO))
    {
      resources.add(
          buildStyleSheet(
            71,
            linkBase + "resources/syntaxhighlighter/styles/shThemeDjango.css"));
    }
    else if (theme.equalsIgnoreCase(THEME_EMACS))
    {
      resources.add(
          buildStyleSheet(
            72,
            linkBase + "resources/syntaxhighlighter/styles/shThemeEmacs.css"));
    }
    else if (theme.equalsIgnoreCase(THEME_FADETOGREY))
    {
      resources.add(
          buildStyleSheet(
            73,
            linkBase
            + "resources/syntaxhighlighter/styles/shThemeFadeToGrey.css"));
    }
    else if (theme.equalsIgnoreCase(THEME_MIDNIGHT))
    {
      resources.add(
          buildStyleSheet(
            74,
            linkBase
            + "resources/syntaxhighlighter/styles/shThemeMidnight.css"));
    }
    else if (theme.equalsIgnoreCase(THEME_RDARK))
    {
      resources.add(
          buildStyleSheet(
            75,
            linkBase + "resources/syntaxhighlighter/styles/shThemeRDark.css"));
    }
    else if (theme.equalsIgnoreCase(THEME_ECLIPSE))
    {
      resources.add(
          buildStyleSheet(
            76,
            linkBase
            + "resources/syntaxhighlighter/styles/shThemeEclipse.css"));
    }
    else
    {
      resources.add(
          buildStyleSheet(
            77,
            linkBase
            + "resources/syntaxhighlighter/styles/shThemeDefault.css"));
    }

    lang = lang.toLowerCase();

    if (lang.equals("bash"))
    {
      resources.add(
          new ScriptResource(
              81,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushBash.js"));
    }
    else if (lang.equals("csharp") || lang.equals("c#")
             || (lang.equals("c-sharp")))
    {
      resources.add(
          new ScriptResource(
              82,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushCSharp.js"));
    }
    else if (lang.equals("cpp") || lang.equals("c++") || (lang.equals("c")))
    {
      resources.add(
          new ScriptResource(
              83,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushCpp.js"));
    }
    else if (lang.equals("coldfusion") || lang.equals("cf"))
    {
      resources.add(
          new ScriptResource(
              84,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushColdFusion.js"));
    }
    else if (lang.equals("css"))
    {
      resources.add(
          new ScriptResource(
              85,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushCss.js"));
    }
    else if (lang.equals("delphi") || (lang.equals("pascal")))
    {
      resources.add(
          new ScriptResource(
              86,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushDelphi.js"));
    }
    else if (lang.equals("diff"))
    {
      resources.add(
          new ScriptResource(
              87,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushDiff.js"));
    }
    else if (lang.equals("erlang") || lang.equals("erl"))
    {
      resources.add(
          new ScriptResource(
              88,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushErlang.js"));
    }
    else if (lang.equals("groovy"))
    {
      resources.add(
          new ScriptResource(
              89,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushGroovy.js"));
    }
    else if (lang.equals("js") || lang.equals("javascript")
             || (lang.equals("jscript")))
    {
      resources.add(
          new ScriptResource(
              90,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushJScript.js"));
    }
    else if (lang.equals("java"))
    {
      resources.add(
          new ScriptResource(
              91,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushJava.js"));
    }
    else if (lang.equals("perl"))
    {
      resources.add(
          new ScriptResource(
              92,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushPerl.js"));
    }
    else if (lang.equals("php"))
    {
      resources.add(
          new ScriptResource(
              93,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushPhp.js"));
    }
    else if (lang.equals("plain"))
    {
      resources.add(
          new ScriptResource(
              94,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushPlain.js"));
    }
    else if (lang.equals("python") || (lang.equals("py")))
    {
      resources.add(
          new ScriptResource(
              95,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushPython.js"));
    }
    else if (lang.equals("ruby") || lang.equals("ror") || lang.equals("rb"))
    {
      resources.add(
          new ScriptResource(
              96,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushRuby.js"));
    }
    else if (lang.equals("scala"))
    {
      resources.add(
          new ScriptResource(
              97,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushScala.js"));
    }
    else if (lang.equals("sql"))
    {
      resources.add(
          new ScriptResource(
              98,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushSql.js"));
    }
    else if (lang.equals("vb") || (lang.equals("vb.net")))
    {
      resources.add(
          new ScriptResource(
              99,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushVb.js"));
    }
    else if (lang.equals("xml") || lang.equals("html") || lang.equals("xhtml")
             || (lang.equals("xslt")))
    {
      resources.add(
          new ScriptResource(
              100,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushXml.js"));
    }
    else if (lang.equals("actionscript3") || lang.equals("as3"))
    {
      resources.add(
          new ScriptResource(
              101,
              linkBase + "resources/syntaxhighlighter/scripts/shBrushAS3.js"));
    }
    else if (lang.equals("jfx") || lang.equals("javafx"))
    {
      resources.add(
          new ScriptResource(
              102,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushJavaFX.js"));
    }
    else if (lang.equals("powershell") || lang.equals("ps"))
    {
      resources.add(
          new ScriptResource(
              103,
              linkBase
              + "resources/syntaxhighlighter/scripts/shBrushPowerShell.js"));
    }

    StringBuffer result = new StringBuffer();

    result.append("<script type=\"text/javascript\">\n");
    result.append("SyntaxHighlighter.config.clipboardSwf = '");
    result.append(linkBase).append(
        "resources/syntaxhighlighter/scripts/clipboard.swf';\n");
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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  private String getTheme(Blog blog)
  {
    String result = blogDAO.getParameter(blog, CONFIG_THEME);

    if (Util.isBlank(result))
    {
      result = THEME_DEFAULT;
    }

    return result;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Dao
  private BlogDAO blogDAO;

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
  private String theme;

  /** Field description */
  private String toolbar = "true";
}
