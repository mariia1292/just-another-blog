/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
 */



package sonia.blog.scripting.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.exception.BlogException;
import sonia.blog.scripting.Script;
import sonia.blog.scripting.ScriptingContext;
import sonia.blog.scripting.ScriptingException;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 *
 * @param <T>
 */
public class ScriptingBeanController<T extends Script>
{

  /**
   * Constructs ...
   *
   *
   * @param type
   * @param context
   * @param session
   * @param detailNavigation
   */
  public ScriptingBeanController(Class<T> type, ScriptingContext context,
                                 BlogSession session, String detailNavigation)
  {
    this.type = type;
    this.context = context;
    this.session = session;
    this.detailNavigation = detailNavigation;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws Exception
   */
  public String create() throws Exception
  {
    script = type.newInstance();

    return detailNavigation;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String invoke() throws ScriptingException
  {
    save();

    if (script != null)
    {
      ExternalContext eCtx =
        FacesContext.getCurrentInstance().getExternalContext();
      BlogRequest request = getRequest(eCtx);
      BlogResponse response = getResponse(eCtx);

      try
      {
        result = context.invoke(request, response, type, script.getName(),
                                new HashMap<String, Object>());
      }
      catch (ScriptingException ex)
      {
        result = Util.getStacktraceAsString(ex);
      }
    }

    return "invoke";
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws ScriptingException
   */
  public String remove() throws ScriptingException
  {
    T s = (T) model.getRowData();

    if (s != null)
    {
      context.remove(session, s);
    }

    return "success";
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws ScriptingException
   */
  public String save() throws ScriptingException
  {
    if (script.getCreationDate() == null)
    {
      context.create(session, script);
    }
    else
    {
      context.update(session, script);
    }

    return "success";
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String select()
  {
    script = (T) model.getRowData();

    return detailNavigation;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getModel()
  {
    model = new ListDataModel();

    List<T> scripts = context.getScripts(session, type);

    if (scripts != null)
    {
      model.setWrappedData(scripts);
    }

    return model;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getResult()
  {
    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public T getScript()
  {
    return script;
  }

  /**
   * Method description
   *
   *
   * @param eCtx
   *
   * @return
   */
  private BlogRequest getRequest(ExternalContext eCtx)
  {
    BlogRequest request = null;
    Object requestObject = eCtx.getRequest();

    if (requestObject instanceof BlogRequest)
    {
      request = (BlogRequest) requestObject;
    }
    else if (requestObject instanceof HttpServletRequest)
    {
      request = new BlogRequest((HttpServletRequest) requestObject);
    }
    else
    {
      throw new BlogException("could not create request");
    }

    return request;
  }

  /**
   * Method description
   *
   *
   * @param eCtx
   *
   * @return
   */
  private BlogResponse getResponse(ExternalContext eCtx)
  {
    BlogResponse response = null;
    Object responseObject = eCtx.getResponse();

    if (responseObject instanceof BlogResponse)
    {
      response = (BlogResponse) responseObject;
    }
    else if (responseObject instanceof HttpServletResponse)
    {
      response = new BlogResponse((HttpServletResponse) responseObject);
    }
    else
    {
      throw new BlogException("could not create response");
    }

    return response;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ScriptingContext context;

  /** Field description */
  private String detailNavigation;

  /** Field description */
  private DataModel model;

  /** Field description */
  private String result;

  /** Field description */
  private T script;

  /** Field description */
  private BlogSession session;

  /** Field description */
  private Class<T> type;
}
