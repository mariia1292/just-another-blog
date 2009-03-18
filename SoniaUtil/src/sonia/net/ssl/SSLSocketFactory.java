/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
