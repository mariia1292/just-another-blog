/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
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
 * @author Sebastian Sdorra
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
