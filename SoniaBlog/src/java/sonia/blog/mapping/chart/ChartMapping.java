/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
