/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.maven;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author sdorra
 * @goal hgversion
 */
public class HgVersionMojo extends AbstractMojo
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
    StringBuffer cmd = new StringBuffer();

    cmd.append(hgPath);

    if (repositoryPath != null)
    {
      cmd.append(" -R ").append(repositoryPath);
    }

    cmd.append(" -q tip");

    String version = getVersion(cmd.toString());

    if ((version != null) && (version.length() > 0))
    {
      if ((propertyName != null) && (propertyName.length() > 0))
      {
        project.getProperties().put(propertyName, version);
      }

      if ((path != null) && (path.length() > 0))
      {
        writeVersionFile(version);
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param version
   *
   * @return
   */
  private String parseVersionString(String version)
  {
    String[] parts = version.split(":");
    StringBuffer out = new StringBuffer();

    if (prefix != null)
    {
      out.append(prefix);
    }

    out.append(parts[0]);

    if (suffix != null)
    {
      out.append(suffix);
    }

    return out.toString();
  }

  /**
   * Method description
   *
   *
   * @param version
   */
  private void writeVersionFile(String version)
  {
    BufferedWriter writer = null;

    try
    {
      File parent = path.getParentFile();

      if (!parent.exists())
      {
        parent.mkdirs();
      }

      writer = new BufferedWriter(new FileWriter(path));
      writer.write(version);
    }
    catch (IOException ex)
    {
      getLog().error(ex);
    }
    finally
    {
      if (writer != null)
      {
        try
        {
          writer.close();
        }
        catch (IOException ex)
        {

          // do nothing
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param p
   *
   * @return
   *
   * @throws IOException
   * @throws InterruptedException
   */
  private String getOutput(Process p) throws IOException, InterruptedException
  {
    String result = null;
    BufferedReader reader = null;

    try
    {
      int r = p.waitFor();

      if (r == 0)
      {
        reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        result = reader.readLine();
      }
      else
      {
        getLog().error("hg exit with " + r);
      }
    }
    finally
    {
      if (reader != null)
      {
        reader.close();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param command
   *
   * @return
   */
  private String getVersion(String command)
  {
    String version = null;

    try
    {
      Process p = Runtime.getRuntime().exec(command);

      version = getOutput(p);

      if (version != null)
      {
        version = parseVersionString(version);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return version;
  }

  //~--- fields ---------------------------------------------------------------

  /**
   * The path to the mercurial binary
   *
   * @parameter expression="${hg.path}" default-value="hg"
   */
  private String hgPath;

  /**
   * The path to the output file
   *
   * @parameter
   */
  private File path;

  /**
   * The prefix of the output
   *
   * @parameter
   */
  private String prefix;

  /**
   * The maven project.
   *
   * @parameter expression="${project}"
   * @readonly
   */
  private MavenProject project;

  /**
   * The prefix of the output
   *
   * @parameter default-value="sonia.maven.revision"
   */
  private String propertyName;

  /**
   * The path to the mercurial repository
   *
   * @parameter
   */
  private String repositoryPath;

  /**
   * The suffix of the output
   *
   * @parameter
   */
  private String suffix;
}
