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


package sonia.jobqueue;

//~--- JDK imports ------------------------------------------------------------

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
public class JobQueueMBean implements DynamicMBean
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(JobQueueMBean.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param queue
   */
  public JobQueueMBean(JobQueue<?> queue)
  {
    this.queue = queue;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param name
   * @param args
   * @param sig
   *
   * @return
   *
   * @throws MBeanException
   * @throws ReflectionException
   */
  public Object invoke(String name, Object[] args, String[] sig)
          throws MBeanException, ReflectionException
  {
    Object result = null;

    if (name.equals("start") && ((args == null) || (args.length == 0))
        && ((sig == null) || (sig.length == 0)))
    {
      if (!queue.isRunning())
      {
        this.queue.start();
      }
    }
    else if (name.equals("stop") && ((args == null) || (args.length == 0))
             && ((sig == null) || (sig.length == 0)))
    {
      if (queue.isRunning())
      {
        this.queue.stop();
      }
    }
    else
    {
      throw new ReflectionException(new NoSuchMethodException(name));
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param key
   *
   * @return
   *
   * @throws AttributeNotFoundException
   * @throws MBeanException
   * @throws ReflectionException
   */
  public Object getAttribute(String key)
          throws AttributeNotFoundException, MBeanException, ReflectionException
  {
    Object result = null;

    if (key.equals("HandlerCount"))
    {
      result = queue.getHandlerCount();
    }
    else if (key.equals("JobCount"))
    {
      result = queue.count();
    }
    else if (key.equals("IsRunning"))
    {
      result = queue.isRunning();
    }
    else if (key.equals("TimeoutLimit"))
    {
      result = queue.getTimeoutLimit();
    }
    else if (key.equals("MaxJobCount"))
    {
      result = queue.getMaxJobs();
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

    for (String key : attributes)
    {
      try
      {
        Object value = getAttribute(key);

        if (value != null)
        {
          list.add(new Attribute(key, value));
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
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
    MBeanAttributeInfo[] attributes = buildAttributeInfo();
    MBeanOperationInfo[] operations = buildOperationInfo();
    MBeanInfo info = new MBeanInfo(JobQueue.class.getName(), "", attributes,
                                   null, operations, null);

    return info;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param arg0
   *
   * @throws AttributeNotFoundException
   * @throws InvalidAttributeValueException
   * @throws MBeanException
   * @throws ReflectionException
   */
  public void setAttribute(Attribute arg0)
          throws AttributeNotFoundException, InvalidAttributeValueException,
                 MBeanException, ReflectionException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @param arg0
   *
   * @return
   */
  public AttributeList setAttributes(AttributeList arg0)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private MBeanAttributeInfo[] buildAttributeInfo()
  {
    return new MBeanAttributeInfo[] {
      new MBeanAttributeInfo("IsRunning", Boolean.class.getName(), "", true,
                             false, false),
      new MBeanAttributeInfo("HandlerCount", Integer.class.getName(), "", true,
                             false, false),
      new MBeanAttributeInfo("TimeoutLimit", Integer.class.getName(), "", true,
                             false, false),
      new MBeanAttributeInfo("JobCount", Integer.class.getName(), "", true,
                             false, false),
      new MBeanAttributeInfo("MaxJobCount", Integer.class.getName(), "", true,
                             false, false) };
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private MBeanOperationInfo[] buildOperationInfo()
  {
    MBeanOperationInfo[] operationInfo = new MBeanOperationInfo[] {
                                           new MBeanOperationInfo("start",
                                             "starts the jobqueue", null,
                                             "void", MBeanOperationInfo.ACTION),
            new MBeanOperationInfo("stop", "stops the jobqueue", null, "void",
                                   MBeanOperationInfo.ACTION) };

    return operationInfo;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private JobQueue<?> queue;
}