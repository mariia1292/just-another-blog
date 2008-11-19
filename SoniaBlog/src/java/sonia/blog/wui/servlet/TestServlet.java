/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.servlet;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.ServiceReference;

import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class TestServlet extends HttpServlet
{

  /**
   * Returns a short description of the servlet.
   *
   * @return
   */
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
    response.setContentType("text/html;charset=UTF-8");

    PrintWriter out = response.getWriter();

    try
    {
      BlogContext context = BlogContext.getInstance();
      ServiceReference reference =
        context.getServiceRegistry().getServiceReference(
            Constants.SERVCIE_ENCRYPTION);
      String passwordString = "password";

      if ((reference != null) && (reference.getImplementation() != null))
      {
        Encryption enc = (Encryption) reference.getImplementation();

        passwordString = enc.encrypt(passwordString);
      }

      Blog blog = context.getDefaultBlog();
      EntityManager em = context.getEntityManager();
      User user = new User();

      user.setActive(true);
      user.setDisplayName("Test User");
      user.setEmail("test@test.de");
      user.setName("test");
      user.setPassword(passwordString);

      BlogMember member = new BlogMember(blog, user, Role.READER);

      em.getTransaction().begin();

      try
      {
        em.persist(user);
        em.persist(member);
        em.getTransaction().commit();
      }
      catch (Exception ex)
      {
        em.getTransaction().rollback();
      }
      finally
      {
        em.close();
      }
    }
    finally
    {
      out.close();
    }
  }
}
