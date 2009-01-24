/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.tagcloud;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.dao.TagDAO;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.util.TagWrapper;
import sonia.blog.entity.Blog;

import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public class TagCloudRenderer extends BaseRenderer
{

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   *
   * @throws IOException
   */
  @Override
  public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException
  {
    if (!(component instanceof TagCloudComponent))
    {
      throw new IllegalArgumentException();
    }

    TagCloudComponent cmp = (TagCloudComponent) component;

    if (isRendered(context, cmp))
    {
      BlogRequest request =
        (BlogRequest) context.getExternalContext().getRequest();
      Blog blog = request.getCurrentBlog();
      TagDAO tagDAO = BlogContext.getDAOFactory().getTagDAO();
      ResponseWriter writer = context.getResponseWriter();

      writer.startElement("div", component);

      if (!isBlank(cmp.getStyle()))
      {
        writer.writeAttribute("style", cmp.getStyle(), null);
      }

      if (!isBlank(cmp.getStyleClass()))
      {
        writer.writeAttribute("class", cmp.getStyleClass(), null);
      }

      String id = cmp.getClientId(context);

      writer.writeAttribute("id", id, null);

      List<TagWrapper> tags = tagDAO.findByBlogAndCount(blog);

      if ((tags != null) &&!tags.isEmpty())
      {
        encodeTags(request, writer, tags, cmp.getMaxItems(),
                   cmp.getMinPercentage(), cmp.getMaxPercentage());
      }

      writer.endElement("div");
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param writer
   * @param tags
   * @param maxItems
   * @param minPercentage
   * @param maxPercentage
   *
   * @throws IOException
   */
  private void encodeTags(BlogRequest request, ResponseWriter writer,
                          List<TagWrapper> tags, int maxItems,
                          int minPercentage, int maxPercentage)
          throws IOException
  {
    double max = 0;
    double min = -1;

    if ((maxItems > 0) && (tags.size() > maxItems))
    {
      Collections.sort(tags);
      tags = tags.subList(0, maxItems);
      min = tags.get(maxItems - 1).getCount();
      max = tags.get(0).getCount();
      Collections.sort(tags, new IdComparator());
    }
    else
    {
      for (TagWrapper tag : tags)
      {
        if (tag.getCount() > max)
        {
          max = tag.getCount();
        }

        if ((min < 0) || (tag.getCount() < min))
        {
          min = tag.getCount();
        }
      }
    }

    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();

    for (TagWrapper tag : tags)
    {
      long value = tag.getCount();
      double size = 0;

      if (value == max)
      {
        size = maxPercentage;
      }
      else if (value == min)
      {
        size = minPercentage;
      }
      else
      {
        size = (value - min) * ((maxPercentage - minPercentage) / max - min)
               + minPercentage;
      }

      writer.startElement("a", null);
      writer.writeAttribute("title", tag.getTag().getName() + " - " + value,
                            null);
      writer.writeAttribute("href",
                            linkBuilder.buildLink(request, tag.getTag()), null);
      writer.writeAttribute("style",
                            "white-space: nowrap; font-size: "
                            + Math.round(size) + "%;", null);
      writer.write(tag.getTag().getName());
      writer.endElement("a");
      writer.write("\n");
    }
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 09/01/02
   * @author     Enter your name here...
   */
  private class IdComparator implements Comparator<TagWrapper>
  {

    /**
     * Method description
     *
     *
     * @param o1
     * @param o2
     *
     * @return
     */
    public int compare(TagWrapper o1, TagWrapper o2)
    {
      return o1.getTag().getId().compareTo(o2.getTag().getId());
    }
  }
}
