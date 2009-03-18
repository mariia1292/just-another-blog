/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def;

//~--- non-JDK imports --------------------------------------------------------

import sonia.jsf.access.AccessHandler;
import sonia.jsf.access.Action;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class DefaultAccessHandler extends AccessHandler
{

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param context
   */
  @Override
  public void handleAccess(HttpServletRequest request,
                           HttpServletResponse response, FacesContext context)
  {
    if (rules != null)
    {
      for (Rule rule : rules)
      {
        if (rule.getCondition().handleCondition(request, context))
        {
          List<Action> actions = rule.getActions();

          if (actions != null)
          {
            for (Action action : actions)
            {
              action.doAction(request, response, context);
            }
          }

          if (rule.isLast())
          {
            return;
          }
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  @Override
  public void readConfig(InputStream in) throws IOException
  {
    DefaultConfigReader reader = new DefaultConfigReader();

    try
    {
      List<Rule> ruleList = reader.readConfig(in);

      synchronized (rules)
      {
        rules = ruleList;
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.err);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Rule> rules = new ArrayList<Rule>();
}
