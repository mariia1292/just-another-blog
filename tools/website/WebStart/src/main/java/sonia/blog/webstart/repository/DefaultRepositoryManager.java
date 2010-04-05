/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webstart.repository;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultRepositoryManager implements RepositoryManager
{

  /** Field description */
  private static String separator = File.separator;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param repository
   */
  public DefaultRepositoryManager(File repository)
  {
    this.repository = repository;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   */
  public FileObject getFileObject(String path)
  {
    FileObject fileObject = null;

    path = fixPath(path);

    File file = new File(repository, path);

    if ((file != null) && file.exists())
    {
      fileObject = new DefaultFileObject(file);
    }

    return fileObject;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param path
   *
   * @return
   */
  private String fixPath(String path)
  {
    if (!separator.equals("/"))
    {
      path = path.replaceAll("/", separator);
    }

    return path;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private File repository;
}
