/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
