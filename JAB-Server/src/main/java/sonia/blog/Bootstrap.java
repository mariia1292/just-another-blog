/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.lang.reflect.Method;

import java.net.URL;
import java.net.URLClassLoader;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sdorra
 */
public class Bootstrap
{

  /**
   * Method description
   *
   *
   *
   * @param argv
   * @throws Exception
   */
  public static void main(String[] argv) throws Exception
  {
    File webapp = new File("/tmp/jab/webapp");
    File libDir = new File(webapp, "WEB-INF" + File.separator + "lib");
    List<URL> classpath = new ArrayList<URL>();

    for (File lib : libDir.listFiles())
    {
      System.out.println(lib.getPath());
      classpath.add(lib.toURI().toURL());
    }

    File classesDir = new File(webapp, "WEB-INF" + File.separator + "classes");

    System.out.println(classesDir.getPath());
    classpath.add(classesDir.toURI().toURL());

    ClassLoader loader = new URLClassLoader(classpath.toArray(new URL[0]));

    Thread.currentThread().setContextClassLoader(loader);

    Class appClass = loader.loadClass("sonia.blog.App");
    Class[] argTypes = { argv.getClass() };

    // Now find the method
    Method m = appClass.getMethod("main", argTypes);

    // Create the actual argument array
    Object passedArgv[] = { argv };

    // Now invoke the method.
    m.invoke(null, passedArgv);
  }
}
