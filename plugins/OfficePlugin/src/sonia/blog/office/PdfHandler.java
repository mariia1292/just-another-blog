/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.editor.AttachmentHandler;
import sonia.blog.entity.Attachment;

import sonia.util.Util;

/**
 *
 * @author sdorra
 */
public class PdfHandler extends AttachmentHandler
{

  /** Field description */
  private static final String LABLE = "PDFViewer";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   *
   * @return
   */
  @Override
  public boolean appendHandler(Attachment attachment)
  {
    return Util.getExtension(attachment.getName()).equalsIgnoreCase("pdf");
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
   * @param request
   * @param selection
   * @param attachment
   *
   * @return
   */
  @Override
  protected String getOutput(BlogRequest request, String selection,
                             Attachment attachment)
  {
    String text = (selection != null)
                  ? selection
                  : attachment.getName();
    StringBuffer result = new StringBuffer();

    result.append("{pdfviewer:id=").append(attachment.getId()).append("}");
    result.append(text).append("{/pdfviewer}");

    return result.toString();
  }
}
