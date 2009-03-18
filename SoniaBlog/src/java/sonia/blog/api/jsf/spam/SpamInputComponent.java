/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.jsf.spam;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.component.UIInput;

/**
 *
 * @author sdorra
 */
public class SpamInputComponent extends UIInput
{

  /** Field description */
  public static final String FAMILY = "sonia.blog.spamInput";

  /** Field description */
  public static final String RENDERER = "sonia.blog.spamInput.renderer";

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public SpamInputComponent()
  {
    setRendererType(RENDERER);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String getFamily()
  {
    return FAMILY;
  }
}
