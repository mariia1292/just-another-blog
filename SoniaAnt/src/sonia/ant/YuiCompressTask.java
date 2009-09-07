/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.ant;

//~--- non-JDK imports --------------------------------------------------------

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class YuiCompressTask extends Task
{

  /**
   * Method description
   *
   *
   * @param fileSet
   */
  public void addFileSet(FileSet fileSet)
  {
    if (fileSets == null)
    {
      fileSets = new ArrayList<FileSet>();
    }

    fileSets.add(fileSet);
  }

  /**
   * Method description
   *
   *
   * @throws BuildException
   */
  @Override
  public void execute() throws BuildException
  {
    if ((fileSets != null) &&!fileSets.isEmpty())
    {
      DirectoryScanner scanner = null;
      File baseDir = null;
      File file = null;

      for (FileSet set : fileSets)
      {
        scanner = set.getDirectoryScanner(getProject());
        baseDir = scanner.getBasedir();

        for (String include : scanner.getIncludedFiles())
        {
          file = new File(baseDir, include);
          compress(file);
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getLinebreak()
  {
    return linebreak;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isDisableOptimizations()
  {
    return disableOptimizations;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isMunge()
  {
    return munge;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isPreserveAllSemiColons()
  {
    return preserveAllSemiColons;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isReplaceSourceFiles()
  {
    return replaceSourceFiles;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isVerbose()
  {
    return verbose;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param disableOptimizations
   */
  public void setDisableOptimizations(boolean disableOptimizations)
  {
    this.disableOptimizations = disableOptimizations;
  }

  /**
   * Method description
   *
   *
   * @param linebreak
   */
  public void setLinebreak(int linebreak)
  {
    this.linebreak = linebreak;
  }

  /**
   * Method description
   *
   *
   * @param munge
   */
  public void setMunge(boolean munge)
  {
    this.munge = munge;
  }

  /**
   * Method description
   *
   *
   * @param preserveAllSemiColons
   */
  public void setPreserveAllSemiColons(boolean preserveAllSemiColons)
  {
    this.preserveAllSemiColons = preserveAllSemiColons;
  }

  /**
   * Method description
   *
   *
   * @param replaceSourceFiles
   */
  public void setReplaceSourceFiles(boolean replaceSourceFiles)
  {
    this.replaceSourceFiles = replaceSourceFiles;
  }

  /**
   * Method description
   *
   *
   * @param verbose
   */
  public void setVerbose(boolean verbose)
  {
    this.verbose = verbose;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   */
  private void compress(File in)
  {
    String name = in.getName();

    if (name.endsWith(".css") || name.endsWith(".js"))
    {
      Reader reader = null;
      Writer writer = null;

      try
      {
        String charset = System.getProperty("file.encoding");

        log("compress file " + in.getPath());
        reader = new InputStreamReader(new FileInputStream(in), charset);

        File out = getOutputFile(in);

        writer = new OutputStreamWriter(new FileOutputStream(out), charset);

        if (name.endsWith(".css"))
        {
          compressCSS(reader, writer);
        }
        else if (name.endsWith(".js") || name.endsWith(".json"))
        {
          compressJS(reader, writer);
        }

        if (replaceSourceFiles)
        {
          if (!in.delete())
          {
            log(new StringBuffer("cant delete source file ").append(
              in.getPath()).toString());
          }
          else
          {
            out.renameTo(in);
          }
        }
      }
      catch (IOException ex)
      {
        log(ex, Project.MSG_WARN);
      }
      finally
      {
        try
        {
          if (reader != null)
          {
            reader.close();
          }

          if (writer != null)
          {
            writer.close();
          }
        }
        catch (IOException ex)
        {
          log(ex, Project.MSG_ERR);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param reader
   * @param writer
   *
   * @throws IOException
   */
  private void compressCSS(Reader reader, Writer writer) throws IOException
  {
    CssCompressor compressor = new CssCompressor(reader);

    compressor.compress(writer, linebreak);
  }

  /**
   * Method description
   *
   *
   * @param reader
   * @param writer
   *
   * @throws IOException
   */
  private void compressJS(Reader reader, Writer writer) throws IOException
  {
    JavaScriptCompressor compressor = new JavaScriptCompressor(reader,
                                        new TaskErrorReporter());

    compressor.compress(writer, linebreak, munge, verbose,
                        preserveAllSemiColons, disableOptimizations);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   *
   * @return
   */
  private File getOutputFile(File in)
  {
    String name = in.getName();
    int index = name.lastIndexOf(".");
    String prefix = name.substring(0, index);
    String suffix = name.substring(index);

    name = new StringBuffer(prefix).append(".min").append(suffix).toString();

    return new File(in.getParentFile(), name);
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/07/08
   * @author         Enter your name here...
   */
  private class TaskErrorReporter implements ErrorReporter
  {

    /**
     * Method description
     *
     *
     * @param message
     * @param sourceName
     * @param line
     * @param lineSource
     * @param lineOffset
     */
    public void error(String message, String sourceName, int line,
                      String lineSource, int lineOffset)
    {
      logMessage(Project.MSG_ERR, message, sourceName, line, lineSource,
                 lineOffset);
    }

    /**
     * Method description
     *
     *
     * @param message
     * @param sourceName
     * @param line
     * @param lineSource
     * @param lineOffset
     *
     * @return
     */
    public EvaluatorException runtimeError(String message, String sourceName,
            int line, String lineSource, int lineOffset)
    {
      logMessage(Project.MSG_ERR, message, sourceName, line, lineSource,
                 lineOffset);

      throw new EvaluatorException(message, sourceName, lineOffset, lineSource,
                                   line);
    }

    /**
     * Method description
     *
     *
     * @param message
     * @param sourceName
     * @param line
     * @param lineSource
     * @param lineOffset
     */
    public void warning(String message, String sourceName, int line,
                        String lineSource, int lineOffset)
    {
      logMessage(Project.MSG_WARN, message, sourceName, line, lineSource,
                 lineOffset);
    }

    /**
     * Method description
     *
     *
     * @param logLevel
     * @param message
     * @param sourceName
     * @param line
     * @param lineSource
     * @param lineOffset
     */
    private void logMessage(int logLevel, String message, String sourceName,
                            int line, String lineSource, int lineOffset)
    {
      StringBuffer logMsg = new StringBuffer();

      logMsg.append("[").append(sourceName).append(":").append(line);
      logMsg.append("] ").append(message);
      log(logMsg.toString(), logLevel);
    }
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private boolean disableOptimizations = false;

  /** Field description */
  private List<FileSet> fileSets;

  /** Field description */
  private int linebreak = -1;

  /** Field description */
  private boolean preserveAllSemiColons = false;

  /** Field description */
  private boolean replaceSourceFiles = false;

  /** Field description */
  private boolean verbose = false;

  /** Field description */
  private boolean munge = true;
}
