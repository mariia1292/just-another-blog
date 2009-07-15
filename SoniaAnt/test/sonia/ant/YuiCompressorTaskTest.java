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

import org.apache.tools.ant.types.FileSet;

import org.junit.Test;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author Sebastian Sdorra
 */
public class YuiCompressorTaskTest extends AntTestBase
{

  /** Field description */
  public static final String RESOURCE_CSS =
    "/sonia/ant/resources/yui-test-2.css";

  /** Field description */
  public static final String RESOURCE_JS = "/sonia/ant/resources/yui-test-1.js";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Test
  public void compressCssTest()
  {
    compressTest(RESOURCE_CSS, ".css");
  }

  /**
   * Method description
   *
   */
  @Test
  public void compressJsTest()
  {
    compressTest(RESOURCE_JS, ".js");
  }

  /**
   * Method description
   *
   *
   * @param resource
   * @param suffix
   */
  public void compressTest(String resource, String suffix)
  {
    YuiCompressTask task = new YuiCompressTask();

    init(task);

    try
    {
      File file = createTempFile(suffix);

      copy(resource, file);

      long orgLength = file.length();

      assertTrue(orgLength > 0);

      FileSet fs = createFileSet(file);

      task.addFileSet(fs);
      task.execute();

      File compressed = getCompressedFile(file);

      assertNotNull(compressed);
      assertTrue(compressed.exists());
      assertTrue(compressed.isFile());

      long length = compressed.length();

      assertTrue(length > 0);
      assertTrue(length < orgLength);
    }
    catch (Exception ex)
    {
      fail(ex.getLocalizedMessage());
    }

    close();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   */
  private File getCompressedFile(File file)
  {
    String name = file.getName();
    int index = name.lastIndexOf(".");
    String prefix = name.substring(0, index);
    String suffix = name.substring(index);

    name = new StringBuffer(prefix).append(".min").append(suffix).toString();

    return new File(file.getParentFile(), name);
  }
}