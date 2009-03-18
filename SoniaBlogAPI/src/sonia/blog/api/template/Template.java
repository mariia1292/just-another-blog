/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.template;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author sdorra
 */
public class Template
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthor()
  {
    return author;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getContentCSS()
  {
    String result = null;

    if (contentCSS != null)
    {
      result = "/template/" + path + "/" + contentCSS;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getEmail()
  {
    return email;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
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

  /**
   * Method description
   *
   *
   * @return
   */
  public String getScreenshot()
  {
    return "/template/" + path + "/screenshot.jpg";
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getUrl()
  {
    return url;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getVersion()
  {
    return version;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isScreenshotAvailable()
  {
    String filePath =
      BlogContext.getInstance().getServletContext().getRealPath("/template/"
        + path + "/screenshot.jpg");

    return new File(filePath).exists();
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param author
   */
  public void setAuthor(String author)
  {
    this.author = author;
  }

  /**
   * Method description
   *
   *
   * @param contentCSS
   */
  public void setContentCSS(String contentCSS)
  {
    this.contentCSS = contentCSS;
  }

  /**
   * Method description
   *
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Method description
   *
   *
   * @param email
   */
  public void setEmail(String email)
  {
    this.email = email;
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Method description
   *
   *
   * @param path
   */
  public void setPath(String path)
  {
    this.path = path;
  }

  /**
   * Method description
   *
   *
   * @param url
   */
  public void setUrl(String url)
  {
    this.url = url;
  }

  /**
   * Method description
   *
   *
   * @param version
   */
  public void setVersion(String version)
  {
    this.version = version;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String author;

  /** Field description */
  private String contentCSS;

  /** Field description */
  private String description;

  /** Field description */
  private String email;

  /** Field description */
  private String name;

  /** Field description */
  private String path;

  /** Field description */
  private String url;

  /** Field description */
  private String version;
}
