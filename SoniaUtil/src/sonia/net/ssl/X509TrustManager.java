/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.net.ssl;

//~--- JDK imports ------------------------------------------------------------

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 * @author sdorra
 */
public class X509TrustManager implements javax.net.ssl.X509TrustManager
{

  /**
   * Method description
   *
   *
   * @param x509Certificate
   * @param str
   *
   * @throws java.security.cert.CertificateException
   */
  public void checkClientTrusted(X509Certificate[] x509Certificate, String str)
          throws java.security.cert.CertificateException {}

  /**
   * Method description
   *
   *
   * @param x509Certificate
   * @param str
   *
   * @throws CertificateException
   */
  public void checkServerTrusted(X509Certificate[] x509Certificate, String str)
          throws CertificateException {}

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public X509Certificate[] getAcceptedIssuers()
  {
    return new X509Certificate[0];
  }
}
