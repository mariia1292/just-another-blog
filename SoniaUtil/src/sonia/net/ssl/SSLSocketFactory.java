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



package sonia.net.ssl;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.security.SecureRandom;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;

/**
 *
 * @author Sebastian Sdorra
 */
public class SSLSocketFactory extends SocketFactory
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(SSLSocketFactory.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  private SSLSocketFactory()
  {
    try
    {
      SSLContext sslContext = SSLContext.getInstance("TLS");

      sslContext.init(null, new X509TrustManager[] { new X509TrustManager() },
                      new SecureRandom());
      factory = (SocketFactory) sslContext.getSocketFactory();
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
   * @return
   */
  public static SocketFactory getDefault()
  {
    return new SSLSocketFactory();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param inetAddress
   * @param param
   *
   * @return
   *
   * @throws IOException
   */
  @Override
  public Socket createSocket(InetAddress inetAddress, int param)
          throws IOException
  {
    return factory.createSocket(inetAddress, param);
  }

  /**
   * Method description
   *
   *
   * @param str
   * @param param
   *
   * @return
   *
   * @throws IOException
   * @throws UnknownHostException
   */
  @Override
  public Socket createSocket(String str, int param)
          throws IOException, UnknownHostException
  {
    return factory.createSocket(str, param);
  }

  /**
   * Method description
   *
   *
   * @param inetAddress
   * @param param
   * @param inetAddress2
   * @param param3
   *
   * @return
   *
   * @throws IOException
   */
  @Override
  public Socket createSocket(InetAddress inetAddress, int param,
                             InetAddress inetAddress2, int param3)
          throws IOException
  {
    return factory.createSocket(inetAddress, param, inetAddress2, param3);
  }

  /**
   * Method description
   *
   *
   * @param str
   * @param param
   * @param inetAddress
   * @param param3
   *
   * @return
   *
   * @throws IOException
   * @throws UnknownHostException
   */
  @Override
  public Socket createSocket(String str, int param, InetAddress inetAddress,
                             int param3)
          throws IOException, UnknownHostException
  {
    return factory.createSocket(str, param, inetAddress, param3);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private SocketFactory factory;
}
