/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.ant;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sdorra
 */
public class MercurialVersionTest {

    public MercurialVersionTest() {
    }

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