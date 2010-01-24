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



package sonia.blog.scripting;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.exception.BlogException;
import sonia.blog.api.exception.BlogSecurityException;
import sonia.blog.entity.Role;

import sonia.util.FileUtil;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class ScriptingContext
{

  /** Field description */
  public static final String SERVICE_TEMPLATEENGINE = "/script/template";

  /** Field description */
  private static ScriptingContext instance;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  private ScriptingContext()
  {
    repository =
      BlogContext.getInstance().getResourceManager().getDirectory("script");
    syntaxMap = new HashMap<String, String>();
    syntaxMap.put("ecmascript", "js");
    syntaxMap.put("groovy", "java");
    syntaxMap.put("jruby", "ruby");
    syntaxMap.put("python", "python");
    syntaxMap.put("freemarker", "html");
  }

  /**
   * Only for tests
   *
   * @param repository
   */
  ScriptingContext(File repository)
  {
    this.repository = repository;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static ScriptingContext getInstance()
  {
    if (instance == null)
    {
      instance = new ScriptingContext();
    }

    return instance;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param session
   * @param script
   *
   * @throws ScriptingException
   */
  public void create(BlogSession session, Script script)
          throws ScriptingException
  {
    checkPermissions(session);

    File directory = getScriptDirectory(script);

    if (directory.exists())
    {
      throw new ScriptingException("script allready exists");
    }

    if (!directory.mkdirs())
    {
      throw new ScriptingException("could not create directory "
                                   + directory.getPath());
    }

    try
    {
      script.store(session, directory);

      if (!testMode && (script instanceof MacroScript))
      {
        BlogContext.getInstance().getMacroParser().putMacroFactory(
            script.getName(), new MacroScriptFactory(script.getName()));
      }
    }
    catch (Exception ex)
    {
      throw new ScriptingException(ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param environment
   * @param type
   * @param name
   *
   * @return
   *
   * @throws ScriptingException
   */
  public String invoke(BlogRequest request, BlogResponse response,
                       Class<? extends Script> type, String name,
                       Map<String, Object> environment)
          throws ScriptingException
  {
    if (request == null)
    {
      throw new IllegalArgumentException("request is required");
    }

    BlogSession session = request.getBlogSession();

    checkPermissions(session);

    Script script = getScript(session, type, name);

    if (script == null)
    {
      StringBuffer msg = new StringBuffer("script ");

      msg.append(name).append(" not found");

      throw new ScriptingException(msg.toString());
    }

    String result = null;
    ScriptContent scriptContent = script.getControllerContent();

    if (scriptContent == null)
    {
      throw new ScriptingException("script content is empty");
    }

    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName(scriptContent.getLanguage());

    for (Map.Entry<String, Object> entry : environment.entrySet())
    {
      engine.put(entry.getKey(), entry.getValue());
    }

    if (!environment.containsKey("parameter"))
    {
      engine.put("parameter", new HashMap<String, String>());
    }

    engine.put("request", request);

    if (response != null)
    {
      engine.put("response", response);
    }

    engine.put("context", BlogContext.getInstance());
    engine.put("daoFactory", BlogContext.getDAOFactory());
    engine.put("entityFactory", new EntityFactory());
    engine.put("session", session);
    engine.put("currentBlog", request.getCurrentBlog());

    ScriptContent templateContent = script.getTemplateContent();

    if (templateContent.isValid())
    {
      if (templateContent != null)
      {
        List<ScriptTemplateEngine> engines = getTemplateEngines();

        for (ScriptTemplateEngine e : engines)
        {
          if (e.getName().equals(templateContent.getLanguage()))
          {
            engine.put("template",
                       e.createTemplate(templateContent.getContent()));

            break;
          }
        }
      }
    }

    ScriptContext ctx = engine.getContext();

    if (ctx == null)
    {
      ctx = new SimpleScriptContext();
      engine.setContext(ctx);
    }

    // fix ECMAScript print
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    ctx.setWriter(writer);

    try
    {
      engine.eval(scriptContent.getContent(), ctx);
      writer.flush();
      result = stringWriter.toString();
    }
    catch (ScriptException ex)
    {
      throw new ScriptingException(ex);
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param language
   * @param syntax
   *
   * @return
   */
  public String putSyntax(String language, String syntax)
  {
    return syntaxMap.put(language.toLowerCase(), syntax);
  }

  /**
   * Method description
   *
   *
   * @param session
   * @param script
   *
   * @throws ScriptingException
   */
  public void remove(BlogSession session, Script script)
          throws ScriptingException
  {
    checkPermissions(session);

    File directory = getScriptDirectory(script);

    if (directory.exists())
    {
      Util.delete(directory);
    }
  }

  /**
   * Method description
   *
   *
   * @param session
   * @param script
   *
   * @throws ScriptingException
   */
  public void update(BlogSession session, Script script)
          throws ScriptingException
  {
    checkPermissions(session);

    File directory = getScriptDirectory(script);

    if (directory.exists())
    {
      Util.delete(directory);
    }

    try
    {
      script.store(session, directory);
    }
    catch (Exception ex)
    {
      throw new ScriptingException(ex);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param type
   * @param name
   * @param <T>
   *
   * @return
   */
  public <T extends Script> T getScript(BlogSession session, Class<T> type,
          String name)
  {
    checkPermissions(session);

    File directory = getScriptDirectory(type, name);
    T script = null;

    if (directory.exists())
    {
      script = createScript(session, type, directory);
    }

    return script;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<String> getScriptLanguages()
  {
    if (scriptLanguages == null)
    {
      scriptLanguages = new ArrayList<String>();

      List<ScriptEngineFactory> factories =
        new ScriptEngineManager().getEngineFactories();

      for (ScriptEngineFactory factory : factories)
      {
        scriptLanguages.add(factory.getLanguageName());
      }
    }

    return scriptLanguages;
  }

  /**
   * Method description
   *
   *
   * @param type
   *
   * @return
   */
  public List<String> getScriptNames(Class<? extends Script> type)
  {
    List<String> names = new ArrayList<String>();
    File scriptDirectory = new File(repository, type.getName());

    if (scriptDirectory.exists())
    {
      File[] directories = FileUtil.listDirectories(scriptDirectory);

      for (File directory : directories)
      {
        if (new File(directory, Script.FILE_SCRIPT).exists())
        {
          names.add(directory.getName());
        }
      }
    }

    return names;
  }

  /**
   * Method description
   *
   *
   * @param session
   * @param type
   * @param <T>
   *
   * @return
   */
  public <T extends Script> List<T> getScripts(BlogSession session,
          Class<T> type)
  {
    checkPermissions(session);

    List<T> result = new ArrayList<T>();
    File scriptDirectory = new File(repository, type.getName());

    if (scriptDirectory.exists())
    {
      File[] directories = FileUtil.listDirectories(scriptDirectory);

      for (File directory : directories)
      {
        result.add(createScript(session, type, directory));
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param language
   *
   * @return
   */
  public String getSyntax(String language)
  {
    return syntaxMap.get(language.toLowerCase());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<String> getTemplateLanguages()
  {
    List<String> templateLanguages = new ArrayList<String>();
    List<ScriptTemplateEngine> engines = getTemplateEngines();

    for (ScriptTemplateEngine engine : engines)
    {
      templateLanguages.add(engine.getName());
    }

    return templateLanguages;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isTestMode()
  {
    return testMode;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param testMode
   */
  public void setTestMode(boolean testMode)
  {
    this.testMode = testMode;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param session
   */
  private void checkPermissions(BlogSession session)
  {
    if (session != null)
    {
      if (!session.hasRole(Role.GLOBALADMIN))
      {
        throw new BlogSecurityException("GlobalAdminSession is required");
      }
    }
    else
    {
      throw new BlogSecurityException("session is required");
    }
  }

  /**
   * Method description
   *
   *
   *
   * @param session
   * @param type
   * @param directory
   * @param <T>
   *
   * @return
   */
  private <T extends Script> T createScript(BlogSession session, Class<T> type,
          File directory)
  {
    T script = null;

    try
    {
      script = type.getDeclaredConstructor(BlogSession.class,
              File.class).newInstance(session, directory);
    }
    catch (Exception ex)
    {
      throw new BlogException("could not load script", ex);
    }

    return script;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param script
   *
   * @return
   */
  private File getScriptDirectory(Script script)
  {
    return getScriptDirectory(script.getClass(), script.getName());
  }

  /**
   * Method description
   *
   *
   * @param type
   * @param name
   *
   * @return
   */
  private File getScriptDirectory(Class type, String name)
  {
    StringBuffer buffer = new StringBuffer(type.getName());

    buffer.append(File.separator).append(name);

    return new File(repository, buffer.toString());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private List<ScriptTemplateEngine> getTemplateEngines()
  {
    return BlogContext.getInstance().getServiceRegistry().get(
        ScriptTemplateEngine.class, SERVICE_TEMPLATEENGINE).getAll();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File repository;

  /** Field description */
  private List<String> scriptLanguages;

  /** Field description */
  private Map<String, String> syntaxMap;

  /** Field description */
  private boolean testMode = false;
}
