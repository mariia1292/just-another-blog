/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sdorra
 */
public abstract class AbstractBlogMacroWidget implements BlogMacroWidget
{

  /**
   * Method description
   *
   *
   * @param param
   *
   * @return
   */
  protected Map<String, String> buildParameterMap(String param)
  {
    Map<String, String> map = new HashMap<String, String>();

    if (Util.hasContent(param))
    {
      String[] parts = param.split(";");

      if ((parts != null) && (parts.length > 0))
      {
        for (String part : parts)
        {
          String[] parameterPair = part.split("=");

          if ((parameterPair != null) && (parameterPair.length > 1))
          {
            map.put(parameterPair[0], parameterPair[1]);
          }
        }
      }
    }

    return map;
  }
}
