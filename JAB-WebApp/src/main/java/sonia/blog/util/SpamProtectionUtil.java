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



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.jsf.spam.SpamLabelRenderer;
import sonia.blog.api.spam.SpamInputProtection;

import sonia.plugin.service.ServiceReference;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

/**
 *
 * @author Sebastian Sdorra
 */
public class SpamProtectionUtil
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(SpamProtectionUtil.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public static SpamInputProtection getSpamInputMethod()
  {
    ServiceReference<SpamInputProtection> spamServiceReference =
      BlogContext.getInstance().getServiceRegistry().get(
          SpamInputProtection.class, Constants.SERVICE_SPAMPROTECTIONMETHOD);
    SpamInputProtection method = null;
    String configString =
      BlogContext.getInstance().getConfiguration().getString(
          Constants.CONFIG_SPAMMETHOD);

    if (!Util.isBlank(configString))
    {
      if (!configString.equalsIgnoreCase("none"))
      {
        List<SpamInputProtection> list = spamServiceReference.getAll();

        for (SpamInputProtection sp : list)
        {
          if (sp.getClass().getName().equals(configString))
          {
            method = sp;
          }
        }

        if (method == null)
        {
          StringBuffer log = new StringBuffer();

          log.append("method ").append(configString);
          log.append(" not found, using default");
          logger.warning(log.toString());
          method = list.get(0);
        }
      }
    }
    else
    {
      method = spamServiceReference.get();
    }

    return method;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param parameterName
   *
   * @return
   */
  public static boolean isSpamQuestionAnsweredCorrect(BlogRequest request,
          String parameterName)
  {
    boolean result = true;
    SpamInputProtection sip = getSpamInputMethod();

    if (sip != null)
    {
      result = false;

      String spamAnswer = request.getParameter(parameterName);

      if (Util.hasContent(spamAnswer))
      {
        HttpSession session = request.getSession(true);

        if (session != null)
        {
          String rightAnswer =
            (String) session.getAttribute(SpamLabelRenderer.REQUESTKEY);

          if (spamAnswer.equals(rightAnswer))
          {
            result = true;
          }
        }
      }
    }

    return result;
  }
}
