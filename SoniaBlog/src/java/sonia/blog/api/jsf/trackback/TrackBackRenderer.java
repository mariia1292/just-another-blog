/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.trackback;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Blog;
import sonia.blog.entity.ContentObject;
import sonia.blog.util.BlogUtil;

import sonia.jsf.base.BaseRenderer;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 *
 * @author sdorra
 */
public class TrackBackRenderer extends BaseRenderer
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
  public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException
  {
    TrackBackComponent tbc = (TrackBackComponent) component;
    ContentObject co = tbc.getObject();
    BlogContext ctx = BlogContext.getInstance();
    LinkBuilder builder = ctx.getLinkBuilder();
    BlogRequest request =
      BlogUtil.getBlogRequest(context.getExternalContext().getRequest());
    Blog blog = request.getCurrentBlog();
    String linkBase = builder.buildLink(request, "/");
    StringBuffer linkBuffer = new StringBuffer();

    linkBuffer.append(linkBase).append("list/");
    linkBuffer.append(co.getId()).append(".jab");

    String link = linkBuffer.toString();
    StringBuffer tbLinkBuffer = new StringBuffer();

    tbLinkBuffer.append(linkBase).append("trackback/").append(co.getId());

    String tbLink = tbLinkBuffer.toString();
    String date = blog.getDateFormatter().format(co.getCreationDate());
    ResponseWriter writer = context.getResponseWriter();

    writer.write("<!--\n");
    writer.write("<rdf:RDF xmlns:rdf=\"");
    writer.write("http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n");
    writer.write("  xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n");
    writer.write("  xmlns:trackback=\"");
    writer.write("http://madskills.com/public/xml/rss/module/trackback/\">\n");

    // start description
    writer.write("<rdf:Description\n");

    // about
    writer.write("  rdf:about=\"");
    writer.write(link);
    writer.write("\"\n");

    // identifier
    writer.write("  dc:identifier=\"");
    writer.write(link);
    writer.write("\"\n");

    // TrackBackUrl
    writer.write("  trackback:ping=\"");
    writer.write(tbLink);
    writer.write("\"\n");

    // title
    writer.write("  dc:title=\"");
    writer.write(co.getTitle());
    writer.write("\"\n");

    // creator
    writer.write("  dc:creator=\"");
    writer.write(co.getAuthorName());
    writer.write("\"\n");

    // date
    writer.write("  dc:date=\"");
    writer.write(date);
    writer.write("\" />\n");

    // end description
    writer.write("</rdf:RDF>\n");
    writer.write("-->\n");
  }
}
