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



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.authentication.RequireRole;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.template.Template;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

/**
 *
 * @author Sebastian Sdorra
 */
@RequireRole(Role.ADMIN)
public class TemplateBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public TemplateBean()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String applyTemplate()
  {
    String result = FAILURE;
    Template template = (Template) templates.getRowData();

    if (template != null)
    {
      Blog blog = getRequest().getCurrentBlog();

      blog.setTemplate(template.getPath());

      BlogDAO blogDAO = BlogContext.getDAOFactory().getBlogDAO();

      if (blogDAO.edit(getBlogSession(), blog))
      {
        result = SUCCESS;
        getMessageHandler().info(getRequest(), "changeTemplateSuccess");
      }
      else
      {
        getMessageHandler().error(getRequest(), "changeTemplateFailure");
      }
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public DataModel getTemplates()
  {
    templates = new ListDataModel();

    Blog blog = getRequest().getCurrentBlog();
    List<Template> templateList =
      BlogContext.getInstance().getTemplateManager().getTemplates(blog);

    if (templateList != null)
    {
      templates.setWrappedData(templateList);
    }

    return templates;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel templates;
}
