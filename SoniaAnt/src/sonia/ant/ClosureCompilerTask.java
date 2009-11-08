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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Commandline.Argument;
import org.apache.tools.ant.types.FileSet;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class ClosureCompilerTask extends Task
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

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param closureJAR
   */
  public void setClosureJAR(File closureJAR)
  {
    this.closureJAR = closureJAR;
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

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param input
   */
  private void compress(File input)
  {
    File output = getOutputFile(input);
    StringBuffer msg = new StringBuffer("compress ");

    msg.append(input.getPath());
    log(msg.toString());

    Java javaTask = new Java(this);

    javaTask.setFork(true);
    javaTask.setJar(closureJAR);

    Argument argIn = javaTask.createArg();

    argIn.setValue("--js=" + input.getPath());

    Argument argOut = javaTask.createArg();

    argOut.setValue("--js_output_file=" + output.getPath());
    javaTask.execute();

    if (replaceSourceFiles)
    {
      input.delete();
      output.renameTo(input);
    }
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File closureJAR;

  /** Field description */
  private List<FileSet> fileSets;

  /** Field description */
  private boolean replaceSourceFiles;
}
