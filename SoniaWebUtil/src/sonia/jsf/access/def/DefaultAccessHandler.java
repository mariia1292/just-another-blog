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



package sonia.jsf.access.def;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.AccessHandler;
import sonia.jsf.access.Action;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultAccessHandler extends AccessHandler
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param context
   */
  @Override
  public void handleAccess(HttpServletRequest request,
                           HttpServletResponse response, FacesContext context)
  {
    if (rules != null)
    {
      for (Rule rule : rules)
      {
        if (rule.getCondition().handleCondition(request, context))
        {
          List<Action> actions = rule.getActions();

          if (actions != null)
          {
            for (Action action : actions)
            {
              action.doAction(request, response, context);
            }
          }

          if (rule.isLast())
          {
            return;
          }
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  @Override
  public synchronized void readConfig(InputStream in) throws IOException
  {
    DefaultConfigReader reader = new DefaultConfigReader();

    try
    {
      rules = reader.readConfig(in);
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.err);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Rule> rules = new ArrayList<Rule>();
}
