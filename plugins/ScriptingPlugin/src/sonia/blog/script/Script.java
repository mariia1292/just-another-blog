/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.script;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.exception.BlogException;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 *
 * @author Sebastian Sdorra
 */
public class Script
{

  /**
   * Constructs ...
   *
   *
   *
   *
   * @param file
   */
  Script(File file)
  {
    this.file = file;
    load();
    validate();
  }

  /**
   * Constructs ...
   *
   *
   * @param title
   * @param author
   * @param description
   * @param content
   */
  public Script(String title, String author, String description, String content)
  {
    this.title = title;
    this.author = author;
    this.description = description;
    this.content = content;
    validate();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param writer
   *
   *
   * @throws IOException
   */
  public void invoke(BlogRequest request, Writer writer) throws IOException
  {
    ScriptManager.getInstance().invoke(request, writer, this);
  }

  /**
   * Method description
   *
   */
  public void load()
  {
    try
    {
      Document document = ScriptManager.getInstance().getDocument(file);
      Element root = document.getDocumentElement();
      NodeList childNodes = root.getChildNodes();

      for (int i = 0; i < childNodes.getLength(); i++)
      {
        Node childNode = childNodes.item(i);
        String childName = childNode.getNodeName();
        String childValue = childNode.getTextContent();

        if (childName.equals(ScriptConstants.METAPROPERTIES_TITLE))
        {
          title = childValue;
        }
        else if (childName.equals(ScriptConstants.METAPROPERTIES_AUTHOR))
        {
          author = childValue;
        }
        else if (childName.equals(ScriptConstants.METAPROPERTIES_DESCRIPTION))
        {
          description = childValue;
        }
        else if (childName.equals(ScriptConstants.METAPROPERTIES_CONTENT))
        {
          content = childValue;
        }
      }
    }
    catch (Exception ex)
    {
      throw new BlogException(ex);
    }
  }

  /**
   * Method description
   *
   */
  public void remove()
  {
    if (file != null)
    {
      file.delete();
    }

    ScriptManager.getInstance().getScripts().remove(this);
  }

  /**
   * Method description
   *
   */
  public void store()
  {
    ScriptManager manager = ScriptManager.getInstance();

    if (file == null)
    {
      file = manager.createScriptFile();
    }

    Document document = manager.getDocument();
    Element scriptEl =
      document.createElement(ScriptConstants.METAPROPERTIES_SCRIPT);
    Element titleEl =
      document.createElement(ScriptConstants.METAPROPERTIES_TITLE);

    titleEl.setTextContent(title);
    scriptEl.appendChild(titleEl);

    if (Util.hasContent(description))
    {
      Element descriptionEl =
        document.createElement(ScriptConstants.METAPROPERTIES_DESCRIPTION);

      descriptionEl.setTextContent(description);
      scriptEl.appendChild(descriptionEl);
    }

    if (Util.hasContent(author))
    {
      Element authorEl =
        document.createElement(ScriptConstants.METAPROPERTIES_AUTHOR);

      authorEl.setTextContent(author);
      scriptEl.appendChild(authorEl);
    }

    if (Util.hasContent(content))
    {
      Element contentEl =
        document.createElement(ScriptConstants.METAPROPERTIES_CONTENT);

      contentEl.setTextContent(content);
      scriptEl.appendChild(contentEl);
    }

    document.appendChild(scriptEl);

    try
    {
      manager.store(document, file);

      if (!manager.getScripts().contains(this))
      {
        manager.getScripts().add(this);
      }
    }
    catch (Exception ex)
    {
      throw new BlogException(ex);
    }
  }

  //~--- get methods ----------------------------------------------------------

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
  public String getContent()
  {
    return content;
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
  public String getName()
  {
    if (file == null)
    {
      file = ScriptManager.getInstance().createScriptFile();
    }

    return file.getName();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTitle()
  {
    return title;
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
   * @param content
   */
  public void setContent(String content)
  {
    this.content = content;
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
   * @param title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void validate()
  {
    if (Util.isBlank(title))
    {
      throw new BlogException("title is empty");
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String author;

  /** Field description */
  private String content;

  /** Field description */
  private String description;

  /** Field description */
  private File file;

  /** Field description */
  private String title;
}
