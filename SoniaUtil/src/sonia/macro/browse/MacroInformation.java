/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public class MacroInformation
{

  /**
   * Constructs ...
   *
   *
   * @param name
   */
  public MacroInformation(String name)
  {
    this.name = name;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param displayName
   */
  public MacroInformation(String name, String displayName)
  {
    this.name = name;
    this.displayName = displayName;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param displayName
   * @param description
   */
  public MacroInformation(String name, String displayName, String description)
  {
    this.name = name;
    this.displayName = displayName;
    this.description = description;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param displayName
   * @param description
   * @param icon
   */
  public MacroInformation(String name, String displayName, String description,
                          String icon)
  {
    this.name = name;
    this.displayName = displayName;
    this.description = description;
    this.icon = icon;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param displayName
   * @param description
   * @param icon
   * @param parameter
   */
  public MacroInformation(String name, String displayName, String description,
                          String icon,
                          List<MacroInformationParameter> parameter)
  {
    this.name = name;
    this.displayName = displayName;
    this.description = description;
    this.icon = icon;
    this.parameter = parameter;
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
  public String getDisplayName()
  {
    return Util.hasContent(displayName)
           ? displayName
           : name;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getIcon()
  {
    return icon;
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

  /**
   * Method description
   *
   *
   * @return
   */
  public List<MacroInformationParameter> getParameter()
  {
    return parameter;
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
   * @param displayName
   */
  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  /**
   * Method description
   *
   *
   * @param icon
   */
  public void setIcon(String icon)
  {
    this.icon = icon;
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

  /**
   * Method description
   *
   *
   * @param parameter
   */
  public void setParameter(List<MacroInformationParameter> parameter)
  {
    this.parameter = parameter;
  }

  public Class<? extends MacroWidget> getBodyWidget()
  {
    return bodyWidget;
  }

  public void setBodyWidget(Class<? extends MacroWidget> bodyWidget)
  {
    this.bodyWidget = bodyWidget;
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

  private Class<? extends MacroWidget> bodyWidget;

  private String widgetParam;

  /** Field description */
  private String description;

  /** Field description */
  private String displayName;

  /** Field description */
  private String icon;

  /** Field description */
  private String name;

  /** Field description */

  /** Field description */
  private List<MacroInformationParameter> parameter;
}
