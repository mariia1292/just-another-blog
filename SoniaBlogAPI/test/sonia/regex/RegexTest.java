/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.regex;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author sdorra
 */
public class RegexTest
{

  /**
   * Method description
   *
   */
  @Test
  public void test()
  {
    String regex = "/list/([0-9/]1)index.jab";
    String[] correct = new String[] { "/list/13/index.jab", "/list/2/index.jab",
                                      "/list/950/index.jab",
                                      "/list/index.jab" };
    String[] wrong = new String[] { "/list/abs/index.jab",
                                    "/list/12a/index.jab",
                                    "/list/12/index.jas" };
    Pattern p = Pattern.compile(regex);

    for (String c : correct)
    {
      System.out.println(c);

      Matcher m = p.matcher(c);

      assertTrue(m.matches());
    }

    for (String w : wrong)
    {
      System.out.println(w);

      Matcher m = p.matcher(w);

      assertFalse(m.matches());
    }
  }
}
