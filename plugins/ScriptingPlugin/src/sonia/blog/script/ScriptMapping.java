/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.script;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Role;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class ScriptMapping extends FinalMapping
{

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    if (!request.getBlogSession().hasRole(Role.GLOBALADMIN))
    {
      response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    String name = request.getParameter(ScriptConstants.PARAMETER_SCRIPT);
    String action = request.getParameter(ScriptConstants.PARAMETER_ACTION);

    if (Util.hasContent(name))
    {
      Script script = ScriptManager.getInstance().getScript(name);

      if (script != null)
      {
        if (action.equalsIgnoreCase(ScriptConstants.ACTION_VIEW))
        {
          viewScript(response, script);
        }
        else if (action.equalsIgnoreCase(ScriptConstants.ACTION_EXCECUTE))
        {
          script.invoke(request, response.getWriter());
        }
        else if (action.equalsIgnoreCase(ScriptConstants.ACTION_STORE))
        {
          storeScript(request, response, script);
        }
        else if (action.equalsIgnoreCase(ScriptConstants.ACTION_REMOVE))
        {
          script.remove();
        }
      }
    }
    else if (action.equalsIgnoreCase(ScriptConstants.ACTION_LIST))
    {
      listScripts(response);
    }
    else if (action.equalsIgnoreCase(ScriptConstants.ACTION_STORE))
    {
      storeScript(request, response, null);
    }
  }

  /**
   * Method description
   *
   *
   * @param writer
   * @param name
   * @param value
   */
  private void appendJsonItem(PrintWriter writer, String name, String value)
  {
    writer.append("\"").append(name).append("\": \"");

    if (value != null)
    {
      writer.append(value);
    }

    writer.append("\"");
  }

  /**
   * Method description
   *
   *
   * @param response
   *
   * @throws IOException
   */
  private void listScripts(BlogResponse response) throws IOException
  {
    response.setContentType("application/x-javascript");

    PrintWriter writer = response.getWriter();

    writer.println("[");

    List<Script> scripts = ScriptManager.getInstance().getScripts();

    if (Util.hasContent(scripts))
    {
      Iterator<Script> it = scripts.iterator();

      while (it.hasNext())
      {
        Script script = it.next();

        writer.append("{ ");
        appendJsonItem(writer, "name", script.getName());
        writer.append(", ");
        appendJsonItem(writer, "title", script.getTitle());
        writer.append(", ");
        appendJsonItem(writer, "description", script.getDescription());
        writer.append(", ");
        appendJsonItem(writer, "author", script.getAuthor());
        writer.append(" }");
        writer.println(it.hasNext()
                       ? ","
                       : "");
      }
    }

    writer.println("]");
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param script
   *
   * @throws IOException
   */
  private void storeScript(BlogRequest request, BlogResponse response,
                           Script script)
          throws IOException
  {
    response.setContentType("text/plain");

    String title = request.getParameter("title");
    String description = request.getParameter("description");
    String content = request.getParameter("content");

    if (script == null)
    {
      script = new Script(title, request.getUser().getDisplayName(),
                          description, content);
    }
    else
    {
      script.setTitle(title);
      script.setDescription(description);
      script.setContent(content);
    }

    script.store();
    response.getWriter().append(script.getName()).close();
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param script
   *
   * @throws IOException
   */
  private void viewScript(BlogResponse response, Script script)
          throws IOException
  {
    response.setContentType("text/plain");

    PrintWriter writer = response.getWriter();

    writer.write(script.getContent());
    writer.close();
  }
}
