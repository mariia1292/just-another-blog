/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

/**
 *
 * @author sdorra
 */
public interface ParameterConverter
{

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   *
   * @throws ConvertException
   */
  public Object convert(String value) throws ConvertException;
}
