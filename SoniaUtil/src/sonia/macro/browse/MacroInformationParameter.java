/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.browse;

/**
 *
 * @author sdorra
 */
public class MacroInformationParameter
{

  /**
   * Constructs ...
   *
   *
   * @param name
   */
  public MacroInformationParameter(String name)
  {
    this.name = name;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param label
   */
  public MacroInformationParameter(String name, String label)
  {
    this.name = name;
    this.label = label;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param label
   * @param description
   */
  public MacroInformationParameter(String name, String label,
                                   String description)
  {
    this.name = name;
    this.label = label;
    this.description = description;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Method description
   *
   *
   * @param label
   */
  public void setLabel(String label)
  {
    this.label = label;
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  public Class<? extends MacroWidget> getWidget()
  {
    return widget;
  }

  public void setWidget(Class<? extends MacroWidget> widget)
  {
    this.widget = widget;
  }

  public String getWidgetParam()
  {
    return widgetParam;
  }

  public void setWidgetParam(String widgetParam)
  {
    this.widgetParam = widgetParam;
  }

  //~--- fields ---------------------------------------------------------------



  private Class<? extends MacroWidget> widget;
  private String widgetParam;

  /** Field description */
  private String description;

  /** Field description */
  private String label;

  /** Field description */
  private String name;
}
