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

import freemarker.cache.ClassTemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import sonia.blog.api.macro.TemplateParser;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class FreemarkerTemplateParser implements TemplateParser
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(FreemarkerTemplateParser.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public FreemarkerTemplateParser()
  {
    config = new Configuration();
    config.setTemplateLoader(
        new ClassTemplateLoader(FreemarkerTemplateParser.class, "/"));
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param parameter
   * @param path
   *
   * @return
   *
   * @throws IOException
   */
  public String parseTemplate(Map<String, Object> parameter, String path)
          throws IOException
  {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    parseTemplate(new OutputStreamWriter(baos), parameter, path);

    return baos.toString();
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param parameter
   * @param path
   *
   * @throws IOException
   */
  public void parseTemplate(Writer writer, Map<String, Object> parameter,
                            String path)
          throws IOException
  {
    if (path.startsWith("/"))
    {
      path = path.substring(1);
    }

    try
    {
      Template template = config.getTemplate(path);

      if (template != null)
      {
        template.process(parameter, writer);
      }
      else
      {
        writer.write("-- template not found --");
      }
    }
    catch (TemplateException ex)
    {
      logger.log(Level.SEVERE, null, ex);

      throw new IOException(ex.getMessage());
    }
    finally
    {
      writer.close();
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Configuration config;
}
