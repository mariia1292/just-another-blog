/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.injection;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author sdorra
 */
public interface Injector
{

  /**
   * Method description
   *
   *
   * @param object
   * @param field
   * @param annotation
   */
  public void inject(Object object, Field field, Annotation annotation);
}
