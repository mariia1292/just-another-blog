/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class AttachmentMapping extends AbstractAttachmentMapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AttachmentMapping.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    boolean found = true;

    if ((param != null) && (param.length > 0))
    {
      try
      {
        long id = Long.parseLong(param[0]);
        AttachmentDAO attachmentDAO =
          BlogContext.getDAOFactory().getAttachmentDAO();
        Blog blog = request.getCurrentBlog();
        Attachment attachment = attachmentDAO.get(id);

        if (((attachment != null) && attachment.isBlog(blog))
            && (((attachment.getEntry() != null)
                 && attachment.getEntry()
                   .isPublished()) || ((attachment.getPage() != null)
                                       && attachment.getPage()
                                         .isPublished()) || request
                                           .isUserInRole(Role.ADMIN) || request
                                           .isUserInRole(Role.AUTHOR)))
        {
          File out = getFile(response, attachment);

          printFile(response, attachment.getName(), attachment.getMimeType(),
                    out);
        }
        else
        {
          found = false;
        }
      }
      catch (NumberFormatException ex)
      {
        logger.log(Level.FINE, null, ex);
        found = false;
      }
    }
    else
    {
      found = false;
    }

    if (!found)
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Logger getLogger()
  {
    return logger;
  }
}
