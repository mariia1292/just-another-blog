/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Test;

import sonia.macro.browse.MacroInfo;
import sonia.macro.browse.MacroInfoParameter;
import sonia.macro.browse.MacroInformation;
import sonia.macro.browse.MacroInformationParameter;
import sonia.macro.browse.MacroInformationProvider;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author sdorra
 */
public class MacroInformationTest
{

  /**
   * Method description
   *
   */
  @Test
  public void getMetaInformationTest()
  {
    MacroInformationProvider provider = MacroInformationProvider.getInstance();

    assertNotNull(provider);

    MacroInformation info = provider.getInformation(InformationMacro.class,
                              Locale.GERMAN);

    assertEquals("info", info.getName());
    assertEquals("Information", info.getDisplayName());
    assertEquals("Gibt Infos", info.getDescription());
    assertNull(info.getIcon());

    List<MacroInformationParameter> params = info.getParameter();

    assertNotNull(params);
    assertTrue(params.size() == 2);

    boolean param1 = false;
    boolean param2 = false;

    for (MacroInformationParameter param : params)
    {
      if (param.getName().equals("test"))
      {
        param1 = true;
        assertEquals("Der Test", param.getLabel());
        assertNull(param.getDescription());
      }
      else if (param.getName().equals("test2"))
      {
        param2 = true;
        assertEquals("Der zweite Test", param.getLabel());
        assertEquals("Nicht so gut wie der erste", param.getDescription());
      }
    }

    assertTrue(param1);
    assertTrue(param2);
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/06/08
   * @author         Enter your name here...
   */
  @MacroInfo(
    name = "info",
    displayName = "Information",
    description = "Gibt Infos"
  )
  private static class InformationMacro implements Macro
  {

    /**
     * Method description
     *
     *
     * @param environment
     * @param body
     *
     * @return
     */
    public String doBody(Map<String, ?> environment, String body)
    {
      return "-- Hallo --";
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param notListed
     */
    public void setNotListed(int notListed)
    {
      this.notListed = notListed;
    }

    /**
     * Method description
     *
     *
     * @param test
     */
    @MacroInfoParameter(displayName = "Der Test")
    public void setTest(String test)
    {
      this.test = test;
    }

    /**
     * Method description
     *
     *
     * @param test2
     */
    @MacroInfoParameter(displayName = "Der zweite Test",
                        description = "Nicht so gut wie der erste")
    public void setTest2(String test2)
    {
      this.test2 = test2;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private int notListed;

    /** Field description */
    private String test;

    /** Field description */
    private String test2;
  }
}
