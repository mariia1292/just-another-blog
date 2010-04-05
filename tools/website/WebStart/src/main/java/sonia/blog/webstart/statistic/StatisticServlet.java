/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.statistic;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.webstart.JnlpContext;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class StatisticServlet extends HttpServlet
{

  /** Field description */
  public static final String VARIABLE = "statistics";

  /** Field description */
  private static final String TEMPLATE = "/template/statistic.jsp";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public StatisticServlet()
  {
    this.context = JnlpContext.getInstance();
  }

  //~--- methods --------------------------------------------------------------

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
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
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
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
          throws ServletException, IOException
  {
    if (context.isInstalled())
    {
      request.setAttribute(VARIABLE,
                           context.getStatisticManager().getStatistics());

      RequestDispatcher dispatcher = request.getRequestDispatcher(TEMPLATE);

      dispatcher.forward(request, response);
    }
    else
    {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private JnlpContext context;
}
