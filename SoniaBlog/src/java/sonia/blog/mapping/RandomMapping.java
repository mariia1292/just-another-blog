/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.mapping.FilterMapping;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Entry;
import sonia.blog.util.BlogUtil;
import sonia.blog.wui.BlogBean;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;

/**
 *
 * @author sdorra
 */
public class RandomMapping extends FilterMapping
{

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingNavigation getMappingNavigation()
  {
    return null;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected String handleFilterMapping(BlogRequest request,
          BlogResponse response, String[] param)
          throws IOException, ServletException
  {
    String result = null;
    Random random = new Random();
    Blog blog = request.getCurrentBlog();
    EntryDAO entryDAO = BlogContext.getDAOFactory().getEntryDAO();
    Long count = entryDAO.countByBlog(blog);

    if (count > 0)
    {
      int start = random.nextInt(count.intValue());
      List<Entry> entries = entryDAO.findAllActivesByBlog(blog, start, 1);

      if ((entries != null) &&!entries.isEmpty())
      {
        BlogBean blogBean = BlogUtil.getSessionBean(request, BlogBean.class,
                              BlogBean.NAME);

        blogBean.setEntry(entries.get(0));
        result = buildTemplateViewId(blog, Constants.TEMPLATE_DETAIL);
      }
    }

    return result;
  }
}
