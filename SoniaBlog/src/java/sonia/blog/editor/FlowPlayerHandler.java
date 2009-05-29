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
  private static final String EXTENSIONS[] = new String[] { "flv", "f4v", "f4p",
          "f4a", "f4b", "mp4" };

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
    boolean result = false;
    String ext = Util.getExtension(attachment.getName());

    for (String extension : EXTENSIONS)
    {
      if (extension.equalsIgnoreCase(ext))
      {
        result = true;

        break;
      }
    }

    return result;
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
