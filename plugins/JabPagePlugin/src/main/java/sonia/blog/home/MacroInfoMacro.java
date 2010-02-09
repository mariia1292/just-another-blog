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



package sonia.blog.home;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Context;
import sonia.blog.api.macro.AbstractBlogMacro;
import sonia.blog.entity.ContentObject;

import sonia.macro.MacroFactory;
import sonia.macro.MacroParser;
import sonia.macro.browse.MacroInformation;
import sonia.macro.browse.MacroInformationParameter;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class MacroInfoMacro extends AbstractBlogMacro
{

  /** Field description */
  public static final String NAME = "macro";

  /** Field description */
  public static final String TEMPLATE = "/sonia/blog/home/template/macro.html";

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
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
  protected String doBody(BlogRequest request, String linkBase,
                          ContentObject object, String body)
  {
    String result = null;

    if (Util.isNotEmpty(name))
    {
      MacroFactory factory = macroParser.getMacroFactory(name);

      if (factory != null)
      {
        MacroInformation info = factory.getInformation(request.getLocale());

        if (info != null)
        {
          Map<String, Object> env = new HashMap<String, Object>();

          env.put("macro", info);

          List<MacroInformationParameter> parameters = info.getParameter();

          if (parameters != null)
          {
            Collections.sort(parameters, new ParameterComparator());
          }
          else
          {
            parameters = new ArrayList<MacroInformationParameter>();
          }

          env.put("parameters", parameters);
          result = parseTemplate(env, TEMPLATE);
        }
        else
        {
          result = new StringBuffer("not information for macro ").append(
            name).append(" found").toString();
        }
      }
      else
      {
        result = "macro not found";
      }
    }
    else
    {
      result = "name parameter is required";
    }

    return result;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 10/02/09
   * @author         Enter your name here...
   */
  private static class ParameterComparator
          implements Comparator<MacroInformationParameter>
  {

    /**
     * Method description
     *
     *
     * @param t
     * @param o
     *
     * @return
     */
    public int compare(MacroInformationParameter t, MacroInformationParameter o)
    {
      return t.getName().compareTo(o.getName());
    }
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Context
  private MacroParser macroParser;

  /** Field description */
  private String name;
}
