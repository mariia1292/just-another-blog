/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.taglist;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.util.TagWrapper;
import sonia.blog.entity.Blog;

import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author sdorra
 */
public class TagListRenderer extends BaseRenderer
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(TagListRenderer.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   * @param component
   *
   * @throws IOException
   */
  @Override @SuppressWarnings("unchecked")
  public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException
  {
    if (!(component instanceof TagListComponent))
    {
      throw new IllegalArgumentException();
    }

    TagListComponent cmp = (TagListComponent) component;

    if (isRendered(context, cmp))
    {
      BlogRequest request =
        (BlogRequest) context.getExternalContext().getRequest();
      Blog blog = request.getCurrentBlog();
      EntityManager em = BlogContext.getInstance().getEntityManager();
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

      try
      {
        Query q = em.createNamedQuery("Tag.findFromBlogAndCount");

        q.setParameter("blog", blog);

        List<TagWrapper> tags = q.getResultList();

        if ((tags != null) &&!tags.isEmpty())
        {
          encodeTags(request, writer, tags, cmp.getMinPercentage(),
                     cmp.getMaxPercentage());
        }
      }
      catch (NoResultException ex) {}
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
      finally
      {
        em.close();
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
   * @param minPercentage
   * @param maxPercentage
   *
   * @throws IOException
   */
  private void encodeTags(BlogRequest request, ResponseWriter writer,
                          List<TagWrapper> tags, int minPercentage,
                          int maxPercentage)
          throws IOException
  {
    double max = 0;
    double min = -1;

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
}
