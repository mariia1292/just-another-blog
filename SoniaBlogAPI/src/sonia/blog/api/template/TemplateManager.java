/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.template;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
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
    templateDirectory = new File(
        BlogContext.getInstance().getServletContext().getRealPath(
          "/template/"));
    templates = new HashMap<String, Template>();
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
   * @param name
   *
   * @return
   */
  public Template getTemplate(String name)
  {
    Template template = templates.get(name);

    if (template == null)
    {
      template = buildTemplate(new File(templateDirectory, name));

      if (template != null)
      {
        templates.put(name, template);
      }
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
    return getTemplate(blog.getTemplate());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Template> getTemplates()
  {
    for (File f : templateDirectory.listFiles(directoryFilter))
    {
      String id = f.getName();

      if (!templates.containsKey(id))
      {
        Template template = buildTemplate(f);

        templates.put(id, template);
      }
    }

    return new ArrayList<Template>(templates.values());
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   */
  private Template buildTemplate(File file)
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
          template.setPath(file.getName());

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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private FileFilter directoryFilter;

  /** Field description */
  private File templateDirectory;

  /** Field description */
  private Map<String, Template> templates;
}
