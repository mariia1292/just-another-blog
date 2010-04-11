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



package sonia.blog.template;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.template.Template;
import sonia.blog.api.template.TemplateManager;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultTemplateManager implements TemplateManager
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(DefaultTemplateManager.class.getName());

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param path
   *
   * @return
   */
  public Template getTemplate(Blog blog, String path)
  {
    init();

    Template template = null;
    TemplateKey key = new TemplateKey(blog.getIdentifier(), path);

    if (templates.containsKey(key))
    {
      template = templates.get(key);
    }
    else
    {
      getTemplates(blog);
      template = templates.get(key);
    }

    return template;
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public Template getTemplate(Blog blog)
  {
    return getTemplate(blog, blog.getTemplate());
  }

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @return
   */
  public List<Template> getTemplates(Blog blog)
  {
    init();

    List<Template> templateList = new ArrayList<Template>();

    addDirectory(templateList, templateDirectory, blog, "/template");

    if (customTemplateDirectory.exists())
    {
      File f = new File(customTemplateDirectory, "__all");

      if (f.exists())
      {
        addDirectory(templateList, f, blog, "/custom-template/__all");
      }

      if (blog.getIdentifier() != null)
      {
        f = new File(customTemplateDirectory, blog.getIdentifier());

        if (f.exists())
        {
          StringBuffer path = new StringBuffer();

          path.append("/custom-template/").append(blog.getIdentifier());
          addDirectory(templateList, f, blog, path.toString());
        }
      }
    }

    return templateList;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param templateList
   * @param directory
   * @param blog
   * @param prefix
   */
  private void addDirectory(List<Template> templateList, File directory,
                            Blog blog, String prefix)
  {
    for (File f : directory.listFiles(directoryFilter))
    {
      StringBuffer pathBuffer = new StringBuffer();

      pathBuffer.append(prefix).append("/").append(f.getName());

      TemplateKey key = new TemplateKey(blog.getIdentifier(),
                                        pathBuffer.toString());

      if (templates.containsKey(key))
      {
        templateList.add(templates.get(key));
      }
      else
      {
        Template template = reader.readTemplate(f, pathBuffer.toString());

        if (template != null)
        {
          templates.put(key, template);
          templateList.add(template);
        }
      }
    }
  }

  /**
   * Method description
   *
   */
  private void init()
  {
    if (templates == null)
    {
      BlogContext ctx = BlogContext.getInstance();

      templates = new HashMap<TemplateKey, Template>();
      templateDirectory =
        new File(ctx.getServletContext().getRealPath("/template/"));

      ResourceManager resManager = ctx.getResourceManager();

      customTemplateDirectory =
        resManager.getDirectory(Constants.RESOURCE_TEMPLATE);
      directoryFilter = new FileFilter()
      {
        public boolean accept(File file)
        {
          return file.isDirectory();
        }
      };
      reader = new TemplateReader();
    }
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version        Enter version here..., 09/05/04
   * @author         Enter your name here...
   */
  private static class TemplateKey
  {

    /**
     * Constructs ...
     *
     *
     *
     * @param blogIdentifier
     * @param name
     */
    public TemplateKey(String blogIdentifier, String name)
    {
      this.blogIdentifier = blogIdentifier;
      this.path = name;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param obj
     *
     * @return
     */
    @Override
    public boolean equals(Object obj)
    {
      if (obj == null)
      {
        return false;
      }

      if (getClass() != obj.getClass())
      {
        return false;
      }

      final TemplateKey other = (TemplateKey) obj;

      if ((this.blogIdentifier == null)
          ? (other.blogIdentifier != null)
          : !this.blogIdentifier.equals(other.blogIdentifier))
      {
        return false;
      }

      if ((this.path == null)
          ? (other.path != null)
          : !this.path.equals(other.path))
      {
        return false;
      }

      return true;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    @Override
    public int hashCode()
    {
      int hash = 5;

      hash = 79 * hash + ((this.blogIdentifier != null)
                          ? this.blogIdentifier.hashCode()
                          : 0);
      hash = 79 * hash + ((this.path != null)
                          ? this.path.hashCode()
                          : 0);

      return hash;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @return
     */
    public String getBlogIdentifier()
    {
      return blogIdentifier;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public String getPath()
    {
      return path;
    }

    //~--- fields -------------------------------------------------------------

    /** Field description */
    private String blogIdentifier;

    /** Field description */
    private String path;
  }


  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File customTemplateDirectory;

  /** Field description */
  private FileFilter directoryFilter;

  /** Field description */
  private TemplateReader reader;

  /** Field description */
  private File templateDirectory;

  /** Field description */
  private Map<TemplateKey, Template> templates;
}
