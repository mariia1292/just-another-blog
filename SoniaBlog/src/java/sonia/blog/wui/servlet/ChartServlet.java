/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui.servlet;

//~--- non-JDK imports --------------------------------------------------------

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.encoders.SunJPEGEncoderAdapter;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.util.BlogWrapper;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.imageio.ImageIO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}
