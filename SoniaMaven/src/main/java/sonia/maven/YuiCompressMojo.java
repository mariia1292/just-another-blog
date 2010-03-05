/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.maven;

//~--- non-JDK imports --------------------------------------------------------

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.NumberFormat;

import java.util.List;

/**
 *
 * @author sdorra
 * @goal yuicompress
 */
public class YuiCompressMojo extends AbstractMojo
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
    List<File> files = MavenUtil.getFiles(yuiBasePath, yuiIncludes,
                         yuiExcludes);

    try
    {
      for (File file : files)
      {
        compress(file);
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
   * @param file
   *
   * @throws IOException
   */
  private void compress(File file) throws IOException
  {
    getLog().info("compress file " + file.getPath());

    String name = file.getName().toLowerCase();
    File temp = new File(file.getAbsolutePath() + ".temp");

    file.renameTo(temp);
    file.delete();

    FileWriter writer = new FileWriter(file);
    FileReader reader = new FileReader(temp);

    try
    {
      if (name.endsWith(".css"))
      {
        CssCompressor cc = new CssCompressor(reader);

        cc.compress(writer, 5000);
      }
      else if (name.endsWith(".js"))
      {
        JavaScriptCompressor jsc = new JavaScriptCompressor(reader, null);

        jsc.compress(writer, 5000, false, true, false, false);
      }
      else
      {
        getLog().warn("could not compress " + name);
      }
    }
    finally
    {
      if (writer != null)
      {
        writer.close();
      }

      if (reader != null)
      {
        reader.close();
      }
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
  private File yuiBasePath;

  /**
   *
   * @parameter
   */
  private String[] yuiExcludes;

  /**
   *
   * @parameter
   */
  private String[] yuiIncludes;
}
