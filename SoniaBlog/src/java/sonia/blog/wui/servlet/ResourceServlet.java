/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.servlet;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class ResourceServlet extends HttpServlet
{

  /** Field description */
  private static final long serialVersionUID = -5516633939705469747L;

  //~--- get methods ----------------------------------------------------------

  /**
   * Returns a short description of the servlet.
   *
   * @return
   */
  @Override
  public String getServletInfo()
  {
    return "Short description";
  }    // </editor-fold>

  //~--- methods --------------------------------------------------------------

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

  /**
   * Handles the HTTP <code>GET</code> method.
   * @param request servlet request
   * @param response servlet response
   *
   * @throws IOException
   * @throws ServletException
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
   *
   * @throws IOException
   * @throws ServletException
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
   *
   * @throws IOException
   * @throws ServletException
   */
  protected void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
          throws ServletException, IOException
  {
    String path = request.getRequestURI();
    String contextPath = request.getContextPath();

    path = "/jab" + path.substring(contextPath.length());
    System.out.println(path);

    InputStream in = Util.findResource(path);

    if (in != null)
    {
      OutputStream out = response.getOutputStream();

      try
      {
        Util.copy(in, out);
      }
      finally
      {
        if (in != null)
        {
          in.close();
        }

        if (out != null)
        {
          out.close();
        }
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}
