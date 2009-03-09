/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.servlet;

//~--- non-JDK imports --------------------------------------------------------

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.util.BlogWrapper;
import sonia.blog.entity.Blog;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.imageio.ImageIO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.dao.jpa.JpaDAOFactory;
import sonia.blog.entity.User;

/**
 *
 * @author sdorra
 */
public class ChartServlet extends HttpServlet
{

  /** Field description */
  private static final long serialVersionUID = -210336308531027677L;

  //~--- get methods ----------------------------------------------------------

  /**
   * Returns a short description of the servlet.
   * @return a String containing servlet description
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
    /*String typeParam = request.getParameter("type");

    if (!Util.isBlank(typeParam))
    {
      if (typeParam.equals("pie"))
      {
        createPieChart(response);
      }
      else if (typeParam.equals("line"))
      {
        createLineChart(response);
      }
    }

    createTest(request, response);*/

    JpaDAOFactory dao = (JpaDAOFactory) DAOFactory.getInstance();
    EntityManagerFactory emf = dao.getEntityManagerFactory();
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    try
    {
      for ( int i=0; i<1000; i++ )
      {
        User u = new User();
        u.setActive(true);
        u.setDisplayName( "Benutzer " + (i+1) );
        u.setEmail( "mail" + (i+1) + "@jab-test.de" );
        u.setGlobalAdmin(false);
        u.setName( "user" + (i+1) );
        u.setPassword("hallo123");
        u.setSelfManaged(false);
        em.persist(u);
      }


      em.getTransaction().commit();
    }
    catch ( Exception ex )
    {
      em.getTransaction().rollback();
      ex.printStackTrace();
    }
    finally
    {
      em.close();
    }


  }

  /**
   * Method description
   *
   *
   * @param response
   *
   * @throws IOException
   */
  private void createLineChart(HttpServletResponse response) throws IOException
  {
    response.setContentType("image/png");

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    dataset.addValue(212, "Classes", "JDK 1.0");
    dataset.addValue(504, "Classes", "JDK 1.1");
    dataset.addValue(1520, "Classes", "SDK 1.2");
    dataset.addValue(1842, "Classes", "SDK 1.3");
    dataset.addValue(2991, "Classes", "SDK 1.4");

    JFreeChart chart = ChartFactory.createLineChart(null, "date", "requests",
                         dataset, PlotOrientation.VERTICAL, false, false,
                         false);

    chart.setBackgroundPaint(Color.white);

    CategoryPlot plot = (CategoryPlot) chart.getPlot();

    plot.setBackgroundPaint(Color.lightGray);
    plot.setRangeGridlinePaint(Color.white);

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

    LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

    renderer.setBaseShapesVisible(true);
    renderer.setDrawOutlines(true);
    renderer.setUseFillPaint(true);
    renderer.setBaseFillPaint(Color.white);

    OutputStream out = null;

    try
    {
      out = response.getOutputStream();

      BufferedImage img = chart.createBufferedImage(600, 250);

      ImageIO.write(img, "png", out);
    }
    finally
    {
      if (out != null)
      {
        out.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param response
   *
   * @throws IOException
   */
  private void createPieChart(HttpServletResponse response) throws IOException
  {
    response.setContentType("image/png");

    OutputStream out = null;

    try
    {
      Date date = new Date();
      Calendar cal = new GregorianCalendar();

      cal.setTime(date);

      List<BlogWrapper> list =
        BlogContext.getDAOFactory().getBlogHitCountDAO().findByMonth(
            cal.get(Calendar.MONTH), cal.get(Calendar.YEAR));
      DefaultPieDataset dataset = new DefaultPieDataset();

      for (BlogWrapper bw : list)
      {
        dataset.setValue(bw.getBlog(), bw.getCount());
      }

      JFreeChart chart = ChartFactory.createPieChart3D(null,    // chart title
        dataset,                                                // data
        false,                                                  // include legend
        false, false);

      chart.setBorderVisible(false);
      chart.setTextAntiAlias(true);
      chart.setBackgroundPaint(Color.WHITE);

      PiePlot3D plot = (PiePlot3D) chart.getPlot();

      plot.setSectionOutlinesVisible(false);
      plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
      plot.setNoDataMessage("No data available");
      plot.setCircular(true);

      // plot.setLabelGap(0.02);
      // plot.setDepthFactor(0.1);
      // plot.setForegroundAlpha(0.6f);
      plot.setOutlineVisible(false);
      plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})"));
      out = response.getOutputStream();

      BufferedImage img = chart.createBufferedImage(512, 480);

      ImageIO.write(img, "png", out);
    }
    finally
    {
      if (out != null)
      {
        out.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param req
   * @param response
   *
   * @throws IOException
   */
  private void createTest(HttpServletRequest req, HttpServletResponse response)
          throws IOException
  {
    BlogRequest request = new BlogRequest(req);
    long id = 1l;
    String idParam = request.getParameter("id");

    if (!Util.isBlank(idParam))
    {
      try
      {
        id = Long.parseLong(idParam);
      }
      catch (NumberFormatException ex)
      {
        ex.printStackTrace();
      }
    }

    Blog blog = request.getCurrentBlog();

    response.setContentType("text/html");

    PrintWriter writer = response.getWriter();

    writer.println("<html>");
    writer.println("  <head>");
    writer.println("    <title>Test</title>");
    writer.println("  </head>");
    writer.println("  <body>");
    writer.println("    <h1>Test</h1>");

    /*
     * EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
     * Entry entry = entryDAO.find(id);
     * Entry[] entries = entryDAO.getPrevAndNextEntry(blog, entry, true);
     *
     * writer.println("    <ul>");
     *
     * if (entries[0] != null)
     * {
     * writer.println("      <li>prev:" + entries[0].getTitle() + "</li>");
     * }
     *
     * writer.println("      <li>" + entry.getTitle() + "</li>");
     *
     * if (entries[1] != null)
     * {
     * writer.println("      <li>next: " + entries[1].getTitle() + "</li>");
     * }
     *
     * writer.println("    </ul>");
     */
    writer.println("  </body>");
    writer.println("</html>");
    writer.close();
  }
}
