/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.template;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class TemplateManager
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(TemplateManager.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public TemplateManager()
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
  }

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
    List<Template> templateList = new ArrayList<Template>();

    addDirectory(templateList, templateDirectory, blog, "/template");

    if (customTemplateDirectory.exists())
    {
      File f = new File(customTemplateDirectory, "__all");

      if (f.exists())
      {
        addDirectory(templateList, f, blog, "/custom-template/__all");
      }

      f = new File(customTemplateDirectory, blog.getIdentifier());

      if (f.exists())
      {
        StringBuffer path = new StringBuffer();

        path.append("/custom-template/").append(blog.getIdentifier());
        addDirectory(templateList, f, blog, path.toString());
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
        Template template = buildTemplate(f, pathBuffer.toString());

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
   *
   * @param file
   * @param path
   *
   * @return
   */
  private Template buildTemplate(File file, String path)
  {
    Template template = null;

    if (file.exists() && file.isDirectory())
    {
      File informationFile = new File(file, "information.properties");

      if (informationFile.exists() && informationFile.isFile())
      {
        try
        {
          template = new Template();
          template.setPath(path);

          Properties information = new Properties();

          information.load(new FileInputStream(informationFile));
          template.setAuthor(information.getProperty("author"));
          template.setName(information.getProperty("name"));
          template.setUrl(information.getProperty("url"));
          template.setEmail(information.getProperty("email"));
          template.setDescription(information.getProperty("description"));
          template.setVersion(information.getProperty("version"));
          template.setContentCSS(information.getProperty("content_css"));
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }

    return template;
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
  private File templateDirectory;

  /** Field description */
  private Map<TemplateKey, Template> templates;
}
