/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.install;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.webstart.repository.DefaultRepositoryManager;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sonia.blog.webstart.JnlpContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class InstallServlet extends HttpServlet
{

  /** Field description */
  private static final String URI_FORM = "/install/form.jsp";

  /** Field description */
  private static final String URI_INDEX = "/index.jsp";

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
    String repositoryPath = request.getParameter("repoPath");

    if (Util.isNotEmpty(repositoryPath))
    {
      File repository = new File(repositoryPath);

      if (repository.exists() || repository.mkdirs())
      {
        InstallManager.install(getServletContext(), repository);
        response.sendRedirect(request.getContextPath() + URI_INDEX);
      }
      else
      {
        sendError(request, response, "repository not found");
      }
    }
    else
    {
      sendError(request, response, "parameter not found");
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param message
   *
   * @throws IOException
   */
  private void sendError(HttpServletRequest request,
                         HttpServletResponse response, String message)
          throws IOException
  {
    String uri = new StringBuffer(request.getContextPath()).append(
                     URI_FORM).append("?error=").append(message).toString();

    response.sendRedirect(uri);
  }
}
