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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultCacheMBeanManager implements CacheMBeanManager
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultCacheMBeanManager.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param server
   * @param namePrefix
   */
  public DefaultCacheMBeanManager(MBeanServer server, String namePrefix)
  {
    this.server = server;
    this.namePrefix = namePrefix;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cache
   */
  public void register(ObjectCache cache)
  {
    try
    {
      ObjectName name = getName(cache);

      if (name != null)
      {
        if (server.isRegistered(name))
        {
          server.unregisterMBean(name);
        }

        server.registerMBean(new CacheMBean(cache), name);
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Method description
   *
   *
   * @param cache
   */
  public void unregister(ObjectCache cache)
  {
    try
    {
      ObjectName name = getName(cache);

      if (name != null)
      {
        server.unregisterMBean(name);
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cache
   *
   * @return
   *
   * @throws MalformedObjectNameException
   */
  private ObjectName getName(ObjectCache cache)
          throws MalformedObjectNameException
  {
    return new ObjectName(namePrefix + cache.getName());
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String namePrefix;

  /** Field description */
  private MBeanServer server;
}
