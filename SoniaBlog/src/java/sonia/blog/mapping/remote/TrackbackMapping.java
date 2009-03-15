/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping.remote;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Trackback;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;

/**
 *
 * @author sdorra
 */
public class TrackbackMapping extends FinalMapping
{

  /** Field description */
  public static final int ERRORCODE_NOPOST = 1;

  /** Field description */
  public static final int ERRORCODE_NOURL = 2;

  /** Field description */
  public static final String ERRORMSG_NOPOST = "only post method is allowed";

  /** Field description */
  public static final String ERRORMSG_NOURL = "url parameter is missing";

  /** Field description */
  private static final String METHOD_POST = "post";

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
    response.setContentType("text/xml");

    PrintWriter writer = response.getWriter();

    writer.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
    writer.println("<response>");

    if (request.getMethod().equalsIgnoreCase(METHOD_POST))
    {
      Trackback trackback = null;
      String url = request.getParameter("url");

      if (!Util.isBlank(url))
      {
        trackback = new Trackback(url);

        String title = request.getParameter("title");

        if (!Util.isBlank(title))
        {
          trackback.setTitle(title);
        }

        String excerpt = request.getParameter("excerpt");

        if (!Util.isBlank(excerpt))
        {
          trackback.setExcerpt(excerpt);
        }

        String blogname = request.getParameter("blog_name");

        if (!Util.isBlank(blogname))
        {
          trackback.setBlogname(blogname);
        }

        // TODO addTrackback
      }
      else
      {
        writeError(writer, ERRORCODE_NOURL, ERRORMSG_NOURL);
      }
    }
    else
    {
      writeError(writer, ERRORCODE_NOPOST, ERRORMSG_NOPOST);
    }

    writer.println("</response>");
    writer.close();
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param code
   * @param msg
   */
  private void writeError(PrintWriter writer, int code, String msg)
  {
    writer.write("<error>" + code + "</error>\n");
    writer.write("<message>");
    writer.write(msg);
    writer.write("</message>\n");
  }
}
