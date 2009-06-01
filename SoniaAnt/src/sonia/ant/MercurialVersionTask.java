/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.ant;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author sdorra
 */
public class MercurialVersionTask extends Task
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
    StringBuffer buffer = new StringBuffer();

    if (hgPath != null)
    {
      buffer.append(hgPath);
    }
    else
    {
      buffer.append("hg");
    }

    if (repositoryPath != null)
    {
      buffer.append(" -R ").append(repositoryPath);
    }

    buffer.append(" -q tip");

    String version = getVersion(buffer.toString());

    if ((version != null) && (version.length() > 0))
    {
      writeVersionFile(version);
    }
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param hgPath
   */
  public void setHgPath(String hgPath)
  {
    this.hgPath = hgPath;
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

  /**
   * Method description
   *
   *
   * @param prefix
   */
  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  /**
   * Method description
   *
   *
   * @param repositoryPath
   */
  public void setRepositoryPath(String repositoryPath)
  {
    this.repositoryPath = repositoryPath;
  }

  /**
   * Method description
   *
   *
   * @param suffix
   */
  public void setSuffix(String suffix)
  {
    this.suffix = suffix;
  }

  //~--- methods --------------------------------------------------------------

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
      if (path == null)
      {
        path = "version.txt";
      }

      writer = new BufferedWriter(new FileWriter(path));
      writer.write(version);
    }
    catch (IOException ex)
    {

      // do nothing
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
        System.err.println("hg exit with " + r);
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

  /** Field description */
  private String hgPath;

  /** Field description */
  private String path;

  /** Field description */
  private String prefix;

  /** Field description */
  private String repositoryPath;

  /** Field description */
  private String suffix;
}
