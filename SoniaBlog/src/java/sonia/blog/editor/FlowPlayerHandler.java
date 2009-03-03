/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.editor;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.editor.AttachmentHandler;
import sonia.blog.entity.Attachment;

import sonia.util.Util;

/**
 *
 * @author sdorra
 */
public class FlowPlayerHandler extends AttachmentHandler
{

  /** Field description */
  private static final String EXTENSION = "flv";

  /** Field description */
  private static final String LABEL = "FlowPlayer";

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
    String ext = Util.getExtension(attachment.getName());

    return EXTENSION.equalsIgnoreCase(ext);
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
    return LABEL;
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
    StringBuffer result = new StringBuffer();

    result.append("{flvviewer:id=").append(attachment.getId());
    result.append("}{/flvviewer}");

    return result.toString();
  }
}
