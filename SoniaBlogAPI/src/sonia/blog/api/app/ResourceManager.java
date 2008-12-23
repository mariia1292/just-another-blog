/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;
import sonia.blog.entity.FileObject;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author sdorra
 */
public class ResourceManager
{

  /**
   * Constructs ...
   *
   *
   * @param resourceDirectory
   */
  public ResourceManager(File resourceDirectory)
  {
    this.resourceDirectory = resourceDirectory;
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
  public File getDirectory(String name)
  {
    return getDirectory(name, null, true);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param blog
   *
   * @return
   */
  public File getDirectory(String name, Blog blog)
  {
    return getDirectory(name, blog, true);
  }

  /**
   * Method description
   *
   *
   * @param name
   * @param blog
   * @param create
   *
   * @return
   */
  public File getDirectory(String name, Blog blog, boolean create)
  {
    String path = name;

    if (blog != null)
    {
      path += File.separator + blog.getId();
    }

    File file = new File(resourceDirectory, path);

    if (!file.exists() && create)
    {
      if (!file.mkdirs())
      {
        throw new RuntimeException("resource directory is read only");
      }
    }

    return file;
  }

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  public File getFile(FileObject object)
  {
    return new File(resourceDirectory, object.getFilePath());
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public File getResourceDirectory()
  {
    return resourceDirectory;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File resourceDirectory;
}
