/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.cli;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author sdorra
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Argument
{
  String value();
  String longName()    default "";
  String description() default "";
  boolean required()   default false;
}