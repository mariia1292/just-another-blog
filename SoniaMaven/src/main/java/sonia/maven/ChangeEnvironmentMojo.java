/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.maven;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author sdorra
 * @goal changeenv
 */
public class ChangeEnvironmentMojo extends AbstractMojo
{

  /**
   * Method description
   *
   *
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    if ((environment == null) || (environment.length() == 0))
    {
      throw new MojoFailureException("parameter environment is required");
    }

    changeEnvironment(new File(environmentBasePath));
  }

  /**
   * Method description
   *
   *
   * @param file
   *
   * @throws MojoFailureException
   */
  private void changeEnvironment(File file) throws MojoFailureException
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

        if (file.renameTo(orginal))
        {
          StringBuffer log = new StringBuffer();

          log.append("change environment for file ").append(orginal.getPath());
          log.append(" to ").append(environment);
          getLog().info(log);
        }
        else
        {
          StringBuffer log = new StringBuffer();

          log.append("failed to change environment for file ");
          log.append(orginal.getPath());
          log.append(" to ").append(environment);

          throw new MojoFailureException(log.toString());
        }
      }
    }
  }

  //~--- fields ---------------------------------------------------------------

  /**
   * @parameter
   */
  private String environment;

  /**
   * @parameter default="."
   */
  private String environmentBasePath;
}
