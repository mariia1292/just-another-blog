/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.api.util;

import java.util.Map;
import sonia.blog.api.app.BlogRequest;

/**
 *
 * @author sdorra
 */
public interface SelectAction
{
  public String getLable();
  public String getOutput( BlogRequest request, Map<String,?> param );
}
