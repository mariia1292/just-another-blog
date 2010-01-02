/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.cli;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author sdorra
 */
public class CliParserTest
{

  /**
   * Method description
   *
   *
   * @throws CliException
   */
  @Test
  public void parseAndConvertTest() throws CliException
  {
    String[] arguments = new String[] { "-o", "1", "-t", "2.0" };
    Test1 test1 = new Test1();
    CliParser parser = new CliParser();

    parser.parse(test1, arguments);
    assertEquals(test1.one, Integer.valueOf(1));
    assertEquals(test1.two, Double.valueOf(2.0));
  }

  /**
   * Method description
   *
   *
   * @throws CliException
   */
  @Test(expected = CliRequiredException.class)
  public void parseRequiredTest() throws CliException
  {
    String[] arguments = new String[] {};
    CliParser parser = new CliParser();

    parser.parse(new Test2(), arguments);
  }

  /**
   * Method description
   *
   *
   * @throws CliException
   */
  @Test
  public void parseTest() throws CliException
  {
    String[] arguments = new String[] { "-v", "1.0", "--help" };
    Test1 test1 = new Test1();
    CliParser parser = new CliParser();

    parser.parse(test1, arguments);
    assertEquals(test1.enable, Boolean.FALSE);
    assertEquals(test1.showHelp, Boolean.TRUE);
    assertEquals(test1.version, "1.0");
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 10/01/02
   * @author         Enter your name here...
   */
  private class Test1
  {

    /** Field description */
    @Argument("e")
    private boolean enable = Boolean.FALSE;

    /** Field description */
    @Argument(value = "o", longName = "one")
    private Integer one = 2;

    /** Field description */
    @Argument(
      value = "h",
      longName = "help",
      description = "shows this help"
    )
    private Boolean showHelp = Boolean.FALSE;

    /** Field description */
    @Argument(value = "t", longName = "two")
    private Double two = 1.0;

    /** Field description */
    @Argument("v")
    private String version;
  }


  /**
   * Class description
   *
   *
   * @version        Enter version here..., 10/01/02
   * @author         Enter your name here...
   */
  private class Test2
  {

    /** Field description */
    @Argument(
      value = "o",
      longName = "one",
      required = true
    )
    private Integer one = 2;
  }
}
