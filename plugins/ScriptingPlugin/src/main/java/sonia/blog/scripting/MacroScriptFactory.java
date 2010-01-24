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

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;

import sonia.macro.Macro;
import sonia.macro.MacroFactory;
import sonia.macro.browse.MacroInformation;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class MacroScriptFactory implements MacroFactory
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(MacroScriptFactory.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param name
   */
  public MacroScriptFactory(String name)
  {
    this.name = name;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param parameters
   *
   * @return
   */
  public Macro createMacro(Map<String, String> parameters)
  {
    return new ScriptMacro(name, parameters);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   * TODO: implement
   *
   * @param locale
   *
   * @return
   */
  public MacroInformation getInformation(Locale locale)
  {
    return null;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 10/01/23
   * @author         Enter your name here...
   */
  private static class ScriptMacro implements Macro
  {

    /**
     * Constructs ...
     *
     *
     * @param name
     * @param parameters
     */
    public ScriptMacro(String name, Map<String, String> parameters)
    {
      this.name = name;
      this.parameters = parameters;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param environment
     * @param body
     *
     * @return
     */
    public String doBody(Map<String, Object> environment, String body)
    {
      if (parameters == null)
      {
        parameters = new HashMap<String, String>();
      }

      Map<String, Object> env = new HashMap<String, Object>();

      env.put("body", body);
      env.put("object", environment.get("object"));
      env.put("linkBase", environment.get("linkBase"));
      env.put("parameter", parameters);

      String result = null;

      try
      {
        result = ScriptingContext.getInstance().invoke(
          (BlogRequest) environment.get("request"),
          (BlogResponse) environment.get("response"), MacroScript.class, name,
          env);
      }
      catch (ScriptingException ex)
      {
        if (logger.isLoggable(Level.WARNING))
        {
          logger.log(Level.WARNING, null, ex);
        }

        result = Util.getStacktraceAsString(ex);
      }

      return result;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private String name;

    /** Field description */
    private Map<String, String> parameters;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String name;
}
