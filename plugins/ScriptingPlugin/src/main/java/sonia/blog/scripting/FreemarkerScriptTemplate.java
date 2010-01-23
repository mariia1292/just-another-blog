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

import freemarker.cache.StringTemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.StringWriter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class FreemarkerScriptTemplate extends ScriptTemplate
{

  /**
   * Constructs ...
   *
   *
   * @param templateContent
   */
  public FreemarkerScriptTemplate(String templateContent)
  {
    super(templateContent);
    config = new Configuration();

    StringTemplateLoader loader = new StringTemplateLoader();

    loader.putTemplate("template", templateContent);
    config.setTemplateLoader(loader);
    parameter = new HashMap<String, Object>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   * @param value
   */
  public void put(String key, Object value)
  {
    parameter.put(key, value);
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws IOException
   * @throws ScriptingException
   */
  public String render() throws ScriptingException, IOException
  {
    StringWriter writer = new StringWriter();
    Template template = config.getTemplate("template");

    try
    {
      template.process(parameter, writer);
    }
    catch (TemplateException ex)
    {
      throw new ScriptingException(ex);
    }

    return writer.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Configuration config;

  /** Field description */
  private Map<String, Object> parameter;
}
