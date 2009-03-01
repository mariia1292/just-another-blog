/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping.chart;

//~--- non-JDK imports --------------------------------------------------------

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.dao.BlogHitCountDAO;
import sonia.blog.api.util.HitWrapper;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;

import java.io.IOException;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class BlogUsageChartMapping extends ChartMapping
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param blog
   *
   * @throws IOException
   */
  @Override
  public void createChart(BlogRequest request, BlogResponse response, Blog blog)
          throws IOException
  {
    BlogHitCountDAO hitCountDAO =
      BlogContext.getDAOFactory().getBlogHitCountDAO();
    List<HitWrapper> hitsPerMonth = hitCountDAO.getHitsPerMonthByBlog(blog);

    if (hitsPerMonth != null)
    {
      createChart(response, hitsPerMonth);
    }
    else
    {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param hitsPerMonth
   */
  private void createChart(BlogResponse response, List<HitWrapper> hitsPerMonth)
  {
    ResourceBundle label =
      ResourceBundle.getBundle("sonia.blog.resources.label");
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    String hitLable = label.getString("hits");

    for (HitWrapper hit : hitsPerMonth)
    {
      String columnName = hit.getMonth() + "-" + hit.getYear();

      dataset.addValue(hit.getCount(), hitLable, columnName);
    }

    JFreeChart chart = ChartFactory.createLineChart(null,
                         label.getString("month"), label.getString("hits"),
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
    renderChart(response, chart, 600, 250);
  }
}
