/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.editor;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.util.SelectAction;
import sonia.blog.entity.Attachment;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public abstract class AttachmentHandler implements SelectAction
{

  /**
   * Method description
   *
   *
   * @param attachment
   *
   * @return
   */
  public abstract boolean appendHandler(Attachment attachment);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param selection
   * @param attachment
   *
   * @return
   */
  protected abstract String getOutput(BlogRequest request, String selection, Attachment attachment);

  /**
   * Method description
   *
   *
   * @param request
   * @param param
   *
   * @return
   */
  public String getOutput(BlogRequest request, Map<String, ?> param)
  {
    Attachment attachment = (Attachment) param.get("attachment");
    String selection = (String) param.get("selection");

    System.out.append( selection );

    return getOutput(request, selection, attachment);
  }
}
