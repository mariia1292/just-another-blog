/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.webstart.repository.FileObject;

import sonia.util.Util;

import sonia.web.util.WebUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class JnlpServlet extends HttpServlet
{

  /** Field description */
  private static final String MAPPING = "/jnlp/";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public JnlpServlet()
  {
    this.context = JnlpContext.getInstance();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   *
   * @return
   */
  public boolean isModified(HttpServletRequest request, FileObject object)
  {
    boolean result = true;
    Date modifiedSince = WebUtil.getIfModifiedSinceDate(request);

    if (modifiedSince != null)
    {
      if (modifiedSince.getTime() == object.getLastModifiedDate().getTime())
      {
        result = false;
      }
    }

    if (result)
    {
      String inmEtag = request.getHeader(WebUtil.HEADER_ETAG);

      if (Util.isNotEmpty(inmEtag) && inmEtag.equals(createETag(object)))
      {
        result = false;
      }
    }

    return result;
  }

  //~--- methods --------------------------------------------------------------

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

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
      String path = getRepositoryPath(request);
      FileObject object = context.getRepositoryManager().getFileObject(path);

      if (object != null)
      {
        proccessRequest(request, response, object);

        if (path.endsWith(".jnlp"))
        {
          context.getStatisticManager().increase(path);
        }
      }
      else
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
    }
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  private String createETag(FileObject object)
  {
    return new StringBuffer("W/\"").append(object.getLength()).append(
        object.getLastModifiedDate().getTime()).append("\"").toString();
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param object
   *
   * @throws IOException
   * @throws ServletException
   */
  private void proccessRequest(HttpServletRequest request,
                               HttpServletResponse response, FileObject object)
          throws ServletException, IOException
  {
    response.setContentType(object.getMimeType());
    response.setContentLength(object.getLength());
    response.setDateHeader("Last-Modified",
                           object.getLastModifiedDate().getTime());
    response.setHeader("Etag", createETag(object));

    if (isModified(request, object))
    {
      OutputStream out = response.getOutputStream();
      InputStream in = object.getInputStream();

      try
      {
        Util.copy(in, out);
      }
      finally
      {
        out.close();
        in.close();
      }
    }
    else
    {
      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private String getRepositoryPath(HttpServletRequest request)
  {
    String uri = request.getRequestURI();
    int index = request.getContextPath().length();

    index += MAPPING.length();

    return uri.substring(index);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private JnlpContext context;
}
