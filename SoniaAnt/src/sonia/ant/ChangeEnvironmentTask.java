/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.ant;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author sdorra
 */
public class ChangeEnvironmentTask extends Task
{

  /**
   * Method description
   *
   *
   * @throws BuildException
   */
  @Override
  public void execute() throws BuildException
  {
    if ((environment == null) || (environment.length() == 0))
    {
      throw new BuildException("environment is required");
    }

    if ((path == null) || (path.length() == 0))
    {
      path = ".";
    }

    File file = new File(path);

    if (file.exists() && file.isDirectory())
    {
      changeEnvironment(file);
    }
    else
    {
      throw new BuildException(path + " not found or is no directory");
    }
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param environment
   */
  public void setEnvironment(String environment)
  {
    this.environment = environment;
  }

  /**
   * Method description
   *
   *
   * @param path
   */
  public void setPath(String path)
  {
    this.path = path;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param file
   */
  private void changeEnvironment(File file)
  {
    if (file.isDirectory())
    {
      for (File child : file.listFiles())
      {
        changeEnvironment(child);
      }
    }
    else
    {
      if (file.getName().endsWith("." + environment))
      {
        String filePath = file.getPath();
        File orginal = new File(filePath.substring(0,
                         filePath.length() - (environment.length() + 1)));

        if (orginal.exists())
        {
          orginal.delete();
        }

        System.out.println("change environment for file " + orginal.getPath()
                           + " to " + environment);
        file.renameTo(orginal);
        file.delete();
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String environment;

  /** Field description */
  private String path;
}
