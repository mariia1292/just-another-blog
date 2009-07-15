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


package sonia.blog.mapping.chart;

//~--- non-JDK imports --------------------------------------------------------

import org.jfree.chart.JFreeChart;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class ChartMapping extends FinalMapping
{

  /** Field description */
  private static Logger logger = Logger.getLogger(ChartMapping.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param blog
   *
   * @throws IOException
   * @throws ServletException
   */
  public abstract void createChart(BlogRequest request, BlogResponse response,
                                   Blog blog)
          throws IOException, ServletException;

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void handleFinalMapping(BlogRequest request, BlogResponse response,
                                 String[] param)
          throws IOException, ServletException
  {
    User user = request.getUser();

    if (user != null)
    {
      Blog blog = null;

      if (user.isGlobalAdmin())
      {
        String blogIdParam = request.getParameter("blogId");

        if (!Util.isBlank(blogIdParam))
        {
          try
          {
            Long blogId = Long.parseLong(blogIdParam);
            BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();

            blog = blogDAO.get(blogId);

            if (blog == null)
            {
              response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
          }
          catch (NumberFormatException ex)
          {
            logger.log(Level.WARNING, null, ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
          }
        }
        else
        {
          blog = request.getCurrentBlog();
        }
      }
      else if (request.isUserInRole(Role.ADMIN))
      {
        blog = request.getCurrentBlog();
      }
      else
      {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      }

      createChart(request, response, blog);
    }
    else
    {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param chart
   * @param width
   * @param height
   */
  protected void renderChart(BlogResponse response, JFreeChart chart,
                             int width, int height)
  {
    response.setContentType("image/png");

    OutputStream out = null;

    try
    {
      out = response.getOutputStream();

      BufferedImage img = chart.createBufferedImage(width, height);

      ImageIO.write(img, "png", out);
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      if (out != null)
      {
        try
        {
          out.close();
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @return
   *
   * @throws IOException
   */
  protected int getYear(BlogRequest request, BlogResponse response)
          throws IOException
  {
    int year = 0;
    String yearParam = request.getParameter("year");

    if (!Util.isBlank(yearParam))
    {
      try
      {
        year = Integer.parseInt(yearParam);
      }
      catch (NumberFormatException ex)
      {
        logger.log(Level.SEVERE, null, ex);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    }
    else
    {
      Calendar cal = new GregorianCalendar();

      year = cal.get(Calendar.YEAR);
    }

    return year;
  }
}