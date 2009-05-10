/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class MacroResult
{

  /**
   * Constructs ...
   *
   */
  public MacroResult()
  {
    this.macros = new ArrayList<Macro>();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param macro
   */
  public void addMacro(Macro macro)
  {
    this.macros.add(macro);
  }

  /**
   * Method description
   *
   *
   * @param macro
   */
  public void removeMacro(Macro macro)
  {
    this.macros.remove(macro);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Macro> getMacros()
  {
    return macros;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getText()
  {
    return text;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param text
   */
  public void setText(String text)
  {
    this.text = text;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Macro> macros;

  /** Field description */
  private String text;
}
