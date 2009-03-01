/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.editor;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.editor.AttachmentHandler;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.entity.Attachment;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

/**
 *
 * @author sdorra
 */
public class LinkHandler extends AttachmentHandler
{

  /** Field description */
  private static final String LABLE = "Link";

  /** Field description */
  private static final String OUTPUT = "<a href=\"{1}\">{0}</a>";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   *
   * @return
   */
  public boolean appendHandler(Attachment attachment)
  {
    return true;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLable()
  {
    return LABLE;
  }

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param selection
   * @param attachment
   *
   * @return
   */
  public String getOutput(BlogRequest request, String selection,
                          Attachment attachment)
  {
    String name = Util.isBlank(selection)
                  ? attachment.getName()
                  : selection;
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    String url = linkBuilder.buildLink(request, attachment);

    return MessageFormat.format(OUTPUT, name, url);
  }
}
