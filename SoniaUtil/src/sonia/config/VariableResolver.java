/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.config;

/**
 *
 * @author sdorra
 */
public interface VariableResolver
{

  /**
   * Method description
   *
   *
   * @param configuration
   * @param provider
   * @param variable
   *
   * @return
   */
  public String resolveVariable(Configuration configuration, String provider,
                                String variable);
}
