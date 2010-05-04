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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.text.NumberFormat;

import java.util.List;

/**
 *
 * @author sdorra
 * @goal jscompress
 */
public class ClosureCompilerMojo extends AbstractMojo
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
    String genClasspath = MavenUtil.buildClasspath(classpath);
    List<File> files = MavenUtil.getFiles(jsBasePath, jsIncludes, jsExcludes);

    try
    {
      for (File file : files)
      {
        compress(genClasspath, file);
      }
    }
    catch (Exception ex)
    {
      if (ex instanceof MojoFailureException)
      {
        throw(MojoFailureException) ex;
      }
      else
      {
        getLog().error(ex);

        throw new MojoFailureException(ex.getMessage());
      }
    }
  }

  /**
   * Method description
   *
   *
   *
   * @param genClasspath
   * @param file
   *
   * @throws IOException
   * @throws InterruptedException
   * @throws MojoFailureException
   */
  private void compress(String genClasspath, File file)
          throws IOException, InterruptedException, MojoFailureException
  {
    getLog().info("compress file " + file.getPath());

    File temp = new File(file.getAbsolutePath() + ".temp");

    file.renameTo(temp);
    file.delete();

    StringBuffer cmd = new StringBuffer(System.getProperty("java.home"));

    cmd.append(File.separator).append("bin").append(File.separator);
    cmd.append("java -classpath ").append(genClasspath);
    cmd.append(" com.google.javascript.jscomp.CommandLineRunner --js=");
    cmd.append(temp.getAbsolutePath()).append(" --js_output_file=");
    cmd.append(file.getAbsolutePath());

    Process p = Runtime.getRuntime().exec(cmd.toString());
    int status = p.waitFor();

    if (status != 0)
    {
      BufferedReader reader = null;

      try
      {
        reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        String line = reader.readLine();

        while (line != null)
        {
          getLog().warn(line);
          line = reader.readLine();
        }
      }
      finally
      {
        if (reader != null)
        {
          reader.close();
        }
      }

      throw new MojoFailureException("closure returns with error " + status);
    }

    double percentage = (1d - ((double) file.length()
                               / (double) temp.length())) * 100;
    NumberFormat format = NumberFormat.getInstance();

    format.setMinimumFractionDigits(2);
    format.setMaximumFractionDigits(2);

    String percentageString = format.format(percentage);
    StringBuffer msg = new StringBuffer(file.getPath()).append(" [");

    msg.append(percentageString).append("%").append("]");
    getLog().info(msg);
    temp.delete();
  }

  //~--- fields ---------------------------------------------------------------

  /**
   *
   * @parameter
   */
  private String[] classpath;

  /**
   *
   * @parameter
   */
  private File jsBasePath;

  /**
   *
   * @parameter
   */
  private String[] jsExcludes;

  /**
   *
   * @parameter
   */
  private String[] jsIncludes;
}
