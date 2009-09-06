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



package sonia.cache;

//~--- JDK imports ------------------------------------------------------------

import java.text.NumberFormat;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
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
public class CacheMBean implements DynamicMBean
{

  /** Field description */
  private static Logger logger = Logger.getLogger(CacheMBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param cache
   */
  public CacheMBean(ObjectCache cache)
  {
    this.cache = cache;
    numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumFractionDigits(2);
    numberFormat.setMinimumFractionDigits(2);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param actionName
   * @param params
   * @param signature
   *
   * @return
   *
   * @throws MBeanException
   * @throws ReflectionException
   */
  public Object invoke(String actionName, Object[] params, String[] signature)
          throws MBeanException, ReflectionException
  {
    if ("clear".equals(actionName))
    {
      cache.clear();
    }
    else
    {
      throw new ReflectionException(new NoSuchMethodException(actionName));
    }

    return null;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attribute
   *
   * @return
   *
   * @throws AttributeNotFoundException
   * @throws MBeanException
   * @throws ReflectionException
   */
  public Object getAttribute(String attribute)
          throws AttributeNotFoundException, MBeanException, ReflectionException
  {
    Object result = null;

    if ("size".equals(attribute))
    {
      result = cache.size();
    }
    else if ("hits".equals(attribute))
    {
      result = cache.getHits();
    }
    else if ("missed".equals(attribute))
    {
      result = cache.getMissed();
    }
    else if ("name".equals(attribute))
    {
      result = cache.getName();
    }
    else if ("type".equals(attribute))
    {
      result = cache.getClass().getName();
    }
    else if ("hitRatio".equals(attribute))
    {
      StringBuffer out = new StringBuffer();

      System.out.println( cache.getHitRatio() );

      out.append(numberFormat.format(cache.getHitRatio())).append("%");
      result = out.toString();

      System.out.println( result );
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param attributes
   *
   * @return
   */
  public AttributeList getAttributes(String[] attributes)
  {
    AttributeList list = new AttributeList();

    try
    {
      for (String attribute : attributes)
      {
        list.add(new Attribute(attribute, getAttribute(attribute)));
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return list;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public MBeanInfo getMBeanInfo()
  {
    MBeanAttributeInfo[] attributeInfo = getAttributeInformation();
    MBeanOperationInfo[] operationInfo = getOperationInformation();
    MBeanInfo info = new MBeanInfo(cache.getClass().getName(),
                                   CacheMBean.class.getName() + " for cache "
                                   + cache.getClass().getName(), attributeInfo,
                                     null, operationInfo, null);

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
  public void setAttribute(Attribute attribute)
          throws AttributeNotFoundException, InvalidAttributeValueException,
                 MBeanException, ReflectionException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @param attributes
   *
   * @return
   */
  public AttributeList setAttributes(AttributeList attributes)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private MBeanAttributeInfo[] getAttributeInformation()
  {
    MBeanAttributeInfo[] attributeInfo = new MBeanAttributeInfo[6];

    attributeInfo[0] = new MBeanAttributeInfo("name", String.class.getName(),
            "the name of the cache", true, false, false);
    attributeInfo[1] = new MBeanAttributeInfo("type", String.class.getName(),
            "the type of the cache", true, false, false);
    attributeInfo[2] = new MBeanAttributeInfo("size", Integer.class.getName(),
            "the current size of the cache", true, false, false);
    attributeInfo[3] = new MBeanAttributeInfo("hits", Integer.class.getName(),
            "the hit count of the cache", true, false, false);
    attributeInfo[4] = new MBeanAttributeInfo("missed",
            Integer.class.getName(), "the missed count of the cache", true,
            false, false);
    attributeInfo[5] = new MBeanAttributeInfo("hitRatio",
            String.class.getName(), "the hitRatio of the cache", true, false,
            false);

    return attributeInfo;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private MBeanOperationInfo[] getOperationInformation()
  {
    MBeanOperationInfo[] operationInfo = new MBeanOperationInfo[1];

    operationInfo[0] = new MBeanOperationInfo("clear", "clears the cache",
            null, "void", MBeanOperationInfo.ACTION);

    return operationInfo;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ObjectCache cache;

  /** Field description */
  private NumberFormat numberFormat;
}
