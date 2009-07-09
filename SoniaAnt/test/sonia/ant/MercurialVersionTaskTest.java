/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.ant;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

/**
 *
 * @author sdorra
 */
public class MercurialVersionTaskTest
{

  /**
   * Constructs ...
   *
   */
  public MercurialVersionTaskTest() {}

  //~--- methods --------------------------------------------------------------

  /**
   * Test of execute method, of class MercurialVersion.
   */
  @Test
  public void test()
  {
    MercurialVersionTask hgVersion = new MercurialVersionTask();

    hgVersion.execute();
  }
}
