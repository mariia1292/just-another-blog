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

import sonia.blog.api.authentication.RequireRole;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Role;
import sonia.blog.scripting.MacroScript;
import sonia.blog.scripting.Script;
import sonia.blog.scripting.ScriptingContext;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.model.SelectItem;

/**
 *
 * @author Sebastian Sdorra
 */
@RequireRole(Role.GLOBALADMIN)
public class ScriptingBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public ScriptingBean()
  {
    super();
    this.context = ScriptingContext.getInstance();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public ScriptingBeanController<Script> getAdminController()
  {
    if (adminController == null)
    {
      adminController = new ScriptingBeanController<Script>(Script.class,
              context, getBlogSession(), "admin-detail");
    }

    return adminController;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public ScriptingBeanController<MacroScript> getMacroController()
  {
    if (macroController == null)
    {
      macroController =
        new ScriptingBeanController<MacroScript>(MacroScript.class, context,
                                    getBlogSession(), "macro-detail");
    }

    return macroController;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getScriptLanguages()
  {
    return createSelectItems(context.getScriptLanguages());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getTemplateLanguages()
  {
    return createSelectItems(context.getTemplateLanguages());
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param list
   *
   * @return
   */
  private SelectItem[] createSelectItems(List<String> list)
  {
    int size = list.size();
    SelectItem[] items = new SelectItem[size];

    for (int i = 0; i < size; i++)
    {
      items[i] = new SelectItem(list.get(i));
    }

    return items;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ScriptingBeanController<Script> adminController;

  /** Field description */
  private ScriptingContext context;

  /** Field description */
  private ScriptingBeanController<MacroScript> macroController;
}
