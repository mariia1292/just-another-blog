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



package sonia.blog.wui.servlet;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.jsf.spam.SpamLabelRenderer;
import sonia.blog.api.msg.BlogMessageHandler;
import sonia.blog.api.spam.SpamCheck;
import sonia.blog.api.spam.SpamInputProtection;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.User;
import sonia.blog.util.BlogUtil;

import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

import sonia.util.Util;
import sonia.util.ValidateUtil;

import sonia.web.msg.MessageHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sebastian Sdorra
 */
public class CommentAction extends HttpServlet
{

  /** Field description */
  private static final String PARAMETER_AUTHORMAIL = "mail";

  /** Field description */
  private static final String PARAMETER_AUTHORNAME = "name";

  /** Field description */
  private static final String PARAMETER_AUTHORURL = "url";

  /** Field description */
  private static final String PARAMETER_CONTENT = "commentContent";

  /** Field description */
  private static final String PARAMETER_ENTRYID = "entryId";

  /** Field description */
  private static final String PARAMETER_REDIRECT = "redirect";

  /** Field description */
  private static final String PARAMETER_SPAM = "spam";

  /** Field description */
  private static Logger logger =
    Logger.getLogger(CommentAction.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws ServletException
   */
  @Override
  public void init() throws ServletException
  {
    BlogContext context = BlogContext.getInstance();

    config = context.getConfiguration();

    ServiceRegistry registry = context.getServiceRegistry();

    spamCheckReference = registry.get(SpamCheck.class,
                                      Constants.SERVICE_SPAMCHECK);
    spamServiceReference = registry.get(SpamInputProtection.class,
            Constants.SERVICE_SPAMPROTECTIONMETHOD);
    commentDAO = BlogContext.getDAOFactory().getCommentDAO();
    messageHandler = getMessageHandler();
  }

  /**
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response)
          throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Processes requests for both HTTP <code>GET</code>
   * and <code>POST</code> methods.
   *
   * @param req
   * @param res
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest req, HttpServletResponse res)
          throws ServletException, IOException
  {
    BlogRequest request = BlogUtil.getBlogRequest(req);
    BlogResponse response = BlogUtil.getBlogResponse(res);
    Comment comment = new Comment();
    String entryIdString = request.getParameter(PARAMETER_ENTRYID);
    Entry entry = null;

    if (Util.hasContent(entryIdString))
    {
      Long entryId = null;

      try
      {
        entryId = Long.parseLong(entryIdString);
        entry = BlogContext.getDAOFactory().getEntryDAO().get(entryId);
      }
      catch (NumberFormatException ex)
      {
        if (logger.isLoggable(Level.FINEST))
        {
          logger.log(Level.FINEST, null, ex);
        }
      }
    }

    String redirectUri = request.getParameter(PARAMETER_REDIRECT);

    if (Util.isBlank(redirectUri))
    {
      redirectUri = "";
    }

    redirectUri =
      BlogContext.getInstance().getLinkBuilder().getRelativeLink(request,
        redirectUri);

    if (entry != null)
    {
      User author = request.getUser();
      boolean valid = true;

      if (author != null)
      {
        comment.setAuthor(author);
      }
      else
      {
        String authorName = request.getParameter(PARAMETER_AUTHORNAME);

        if (Util.hasContent(authorName))
        {
          comment.setAuthorName(authorName);
        }
        else
        {
          valid = false;
          messageHandler.warn(req, "name", "nameIsNotValid");
        }

        String authorMail = request.getParameter(PARAMETER_AUTHORMAIL);

        if (Util.hasContent(authorMail))
        {
          if (ValidateUtil.isMail(authorMail))
          {
            comment.setAuthorMail(authorMail);
          }
          else
          {
            valid = false;
            messageHandler.warn(req, "mail", "emailIsNotValid");
          }
        }

        String authorUrl = request.getParameter(PARAMETER_AUTHORURL);

        if (Util.hasContent(authorUrl))
        {
          if (ValidateUtil.isUrl(authorUrl))
          {
            comment.setAuthorURL(authorUrl);
          }
          else
          {
            valid = false;
            messageHandler.warn(req, "url", "malformedUrl");
          }
        }
      }

      String content = request.getParameter(PARAMETER_CONTENT);

      if (Util.hasContent(content))
      {
        comment.setContent(content);
      }
      else
      {
        valid = false;
        messageHandler.warn(req, "commentContent", "contentIsRequired");
      }

      if (!isSpamQuestionAnsweredCorrect(request))
      {
        valid = false;
        messageHandler.warn(req, "spam", "spamInputFailure");
      }

      if (valid)
      {
        comment.setAuthorAddress(request.getRemoteAddr());
        checkSpam(request, comment);
        entry.addComment(comment);

        if (commentDAO.add(request.getBlogSession(), comment))
        {
          messageHandler.info(req, "createCommentSuccess");
        }
        else
        {
          messageHandler.fatal(req, "createCommentFailure");
        }
      }
      else
      {
        redirectUri += "#comment-form";
      }
    }
    else
    {
      messageHandler.error(req, "createCommentFailure");
    }

    response.sendRedirect(redirectUri);
  }

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param comment
   */
  private void checkSpam(BlogRequest request, Comment comment)
  {
    comment.setSpam(false);

    if (spamCheckReference != null)
    {
      List<SpamCheck> list = spamCheckReference.getAll();

      if (Util.hasContent(list))
      {
        for (SpamCheck check : list)
        {
          if (check.isSpam(request, comment))
          {
            messageHandler.warn(request, "createCommentSpam");
            comment.setSpam(true);

            break;
          }
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private MessageHandler getMessageHandler()
  {
    return new BlogMessageHandler(
        ResourceBundle.getBundle("/sonia/blog/resources/message"));
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private SpamInputProtection getSpamInputMethod()
  {
    SpamInputProtection method = null;
    String configString = config.getString(Constants.CONFIG_SPAMMETHOD);

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
   *
   * @return
   */
  private boolean isSpamQuestionAnsweredCorrect(BlogRequest request)
  {
    boolean result = true;
    SpamInputProtection sip = getSpamInputMethod();

    if (sip != null)
    {
      result = false;

      String spamAnswer = request.getParameter(PARAMETER_SPAM);

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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private CommentDAO commentDAO;

  /** Field description */
  private BlogConfiguration config;

  /** Field description */
  private MessageHandler messageHandler;

  /** Field description */
  private ServiceReference<SpamCheck> spamCheckReference;

  /** Field description */
  private ServiceReference<SpamInputProtection> spamServiceReference;
}
