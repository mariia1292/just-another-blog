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
import java.io.IOException;

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
      getLog().error(ex);

      throw new MojoFailureException(ex.getMessage());
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
   */
  private void compress(String genClasspath, File file)
          throws IOException, InterruptedException
  {
    getLog().info("compress file " + file.getPath());

    File temp = new File(file.getAbsolutePath() + ".temp");

    file.renameTo(temp);
    file.delete();

    StringBuffer cmd = new StringBuffer(System.getProperty("java.home"));

    cmd.append(File.separator).append("bin").append(File.separator);
    cmd.append("java -classpath ").append(genClasspath);
    cmd.append(" com.google.javascript.jscomp.CompilerRunner --js=");
    cmd.append(temp.getAbsolutePath()).append(" --js_output_file=");
    cmd.append(file.getAbsolutePath());

    Process p = Runtime.getRuntime().exec(cmd.toString());

    p.waitFor();
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
