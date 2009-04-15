/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.util.Set;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ReflectionException;

/**
 *
 * @author sdorra
 */
public class ModifyableConfigurationMBean extends ConfigurationMBean
{

  /**
   * Constructs ...
   *
   *
   * @param config
   */
  public ModifyableConfigurationMBean(ModifyableConfiguration config)
  {
    super(config);
    this.config = config;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public MBeanInfo getMBeanInfo()
  {
    Set<String> keys = config.keySet();

    // SortedSet ???
    MBeanAttributeInfo[] attributeInfo = getAttributeInformation(keys, true);
    MBeanOperationInfo[] operationInfo = getOperationInformation();
    MBeanInfo info =
      new MBeanInfo(
          config.getClass().getName(),
          ModifyableConfigurationMBean.class.getName() + " for config "
          + config.getClass().getName(), attributeInfo, null, operationInfo,
            null);

    return info;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attribute
   *
   * @throws AttributeNotFoundException
   * @throws InvalidAttributeValueException
   * @throws MBeanException
   * @throws ReflectionException
   */
  @Override
  public void setAttribute(Attribute attribute)
          throws AttributeNotFoundException, InvalidAttributeValueException,
                 MBeanException, ReflectionException
  {
    config.set(attribute.getName(), attribute.getValue());
  }

  /**
   * Method description
   *
   *
   * @param attributes
   *
   * @return
   */
  @Override
  public AttributeList setAttributes(AttributeList attributes)
  {
    Attribute[] attrs = (Attribute[]) attributes.toArray(new Attribute[0]);
    AttributeList retlist = new AttributeList();

    for (Attribute attr : attrs)
    {
      String name = attr.getName();
      Object value = attr.getValue();

      config.set(name, value);
      retlist.add( attr );
    }

    return retlist;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ModifyableConfiguration config;
}
