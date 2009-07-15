/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
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
 * @author Sebastian Sdorra
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
  @SuppressWarnings("unchecked")
  public AttributeList setAttributes(AttributeList attributes)
  {
    Attribute[] attrs = (Attribute[]) attributes.toArray(new Attribute[0]);
    AttributeList retlist = new AttributeList();

    for (Attribute attr : attrs)
    {
      String name = attr.getName();
      Object value = attr.getValue();

      config.set(name, value);
      retlist.add(attr);
    }

    return retlist;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ModifyableConfiguration config;
}