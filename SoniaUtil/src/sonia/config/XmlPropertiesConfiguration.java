/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Properties;

/**
 *
 * @author sdorra
 */
public class XmlPropertiesConfiguration extends PropertiesConfiguration
{

  /**
   * Constructs ...
   *
   */
  public XmlPropertiesConfiguration()
  {
    super();
  }

  /**
   * Constructs ...
   *
   *
   * @param properties
   */
  public XmlPropertiesConfiguration(Properties properties)
  {
    super(properties);
  }

  /**
   * Constructs ...
   *
   *
   * @param delimeter
   */
  public XmlPropertiesConfiguration(String delimeter)
  {
    super(delimeter);
  }

  /**
   * Constructs ...
   *
   *
   * @param properties
   * @param delimeter
   */
  public XmlPropertiesConfiguration(Properties properties, String delimeter)
  {
    super(properties, delimeter);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  @Override
  public void load(InputStream in) throws IOException
  {
    properties.loadFromXML(in);
  }

  /**
   * Method description
   *
   *
   * @param out
   *
   * @throws IOException
   */
  @Override
  public void store(OutputStream out) throws IOException
  {
    properties.storeToXML(out, "SoniaConfiguration");
  }
}
