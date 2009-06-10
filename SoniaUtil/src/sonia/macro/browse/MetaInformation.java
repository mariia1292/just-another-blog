/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.browse;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public class MetaInformation
{

  /**
   * Constructs ...
   *
   *
   * @param name
   */
  public MetaInformation(String name)
  {
    this.name = name;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param description
   */
  public MetaInformation(String name, String description)
  {
    this.name = name;
    this.description = description;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param description
   * @param icon
   */
  public MetaInformation(String name, String description, String icon)
  {
    this.name = name;
    this.description = description;
    this.icon = icon;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param description
   * @param icon
   * @param parameter
   */
  public MetaInformation(String name, String description, String icon,
                         List<MetaInformationParameter> parameter)
  {
    this.name = name;
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
  public List<MetaInformationParameter> getParameter()
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
  public void setParameter(List<MetaInformationParameter> parameter)
  {
    this.parameter = parameter;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String description;

  /** Field description */
  private String icon;

  /** Field description */
  private String name;

  /** Field description */
  private List<MetaInformationParameter> parameter;
}
