/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.test;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.plugin.AbstractPluginServlet;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class TestServlet extends AbstractPluginServlet
{

  /** Field description */
  private static final long serialVersionUID = 5333797481599141555L;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMapping()
  {
    return "test.html";
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    response.setContentType("text/html");

    PrintWriter writer = response.getWriter();

    writer.println("<html>");
    writer.println("  <head>");
    writer.println("    <title>Test Servlet</title>");
    writer.println("  </head>");
    writer.println("  <body>");
    writer.println("    <h1>Test Servlet</h1>");
    writer.println("    <p>Nur ein Test</p>");
    writer.println("  </body>");
    writer.println("</html>");
  }
}
