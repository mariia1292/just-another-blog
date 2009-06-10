/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.browse;

/**
 *
 * @author sdorra
 */
public class MetaInformationParameter
{

  /**
   * Constructs ...
   *
   *
   * @param name
   */
  public MetaInformationParameter(String name)
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
  public MetaInformationParameter(String name, String label)
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
  public MetaInformationParameter(String name, String label, String description)
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String description;

  /** Field description */
  private String label;

  /** Field description */
  private String name;
}
