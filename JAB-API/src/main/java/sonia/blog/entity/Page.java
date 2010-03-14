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



package sonia.blog.entity;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.PageNavigation;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class Page
        implements Serializable, PermaObject, ContentObject, PageNavigation
{

  /** Field description */
  private static final long serialVersionUID = 7113784206225853548L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachment
   */
  public void addAttachment(Attachment attachment)
  {
    getAttachments().add(attachment);
    attachment.setPage(this);
  }

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

    final Page other = (Page) obj;

    if ((this.id != other.id)
        && ((this.id == null) ||!this.id.equals(other.id)))
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
    int hash = 7;

    hash = 79 * hash + ((this.id != null)
                        ? this.id.hashCode()
                        : 0);

    return hash;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean renderMacros()
  {
    return true;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public PageNavigation toPageNavigation()
  {
    return this;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String toString()
  {
    return "sonia.blog.entity.Page[id=" + id + "]";
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Attachment> getAttachments()
  {
    if (attachments == null)
    {
      attachments = new ArrayList<Attachment>();
    }

    return attachments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public User getAuthor()
  {
    return author;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorName()
  {
    return author.getDisplayName();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    return blog;
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
  public Date getCreationDate()
  {
    if ( creationDate == null )
    {
      creationDate = new Date();
    }
    return creationDate;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayContent()
  {
    return displayContent;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Long getId()
  {
    return id;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getLastUpdate()
  {
    return lastUpdate;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getNavigationPosition()
  {
    return navigationPosition;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getNavigationTitle()
  {
    return navigationTitle;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Page getParent()
  {
    return parent;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getPublishingDate()
  {
    return publishingDate;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTeaser()
  {
    return content;
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

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isPublished()
  {
    return published;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param attachments
   */
  public void setAttachments(List<Attachment> attachments)
  {
    this.attachments = attachments;
  }

  /**
   * Method description
   *
   *
   * @param author
   */
  public void setAuthor(User author)
  {
    this.author = author;
  }

  /**
   * Method description
   *
   *
   * @param blog
   */
  public void setBlog(Blog blog)
  {
    this.blog = blog;
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
   * @param displayContent
   */
  public void setDisplayContent(String displayContent)
  {
    this.displayContent = displayContent;
  }

  /**
   * Method description
   *
   *
   * @param id
   */
  public void setId(Long id)
  {
    this.id = id;
  }

  /**
   * Method description
   *
   *
   * @param navigationPosition
   */
  public void setNavigationPosition(int navigationPosition)
  {
    this.navigationPosition = navigationPosition;
  }

  /**
   * Method description
   *
   *
   * @param navigationTitle
   */
  public void setNavigationTitle(String navigationTitle)
  {
    this.navigationTitle = navigationTitle;
  }

  /**
   * Method description
   *
   *
   * @param parent
   */
  public void setParent(Page parent)
  {
    this.parent = parent;
  }

  /**
   * Method description
   *
   *
   * @param published
   */
  public void setPublished(boolean published)
  {
    this.published = published;
    this.publishingDate = new Date();
  }

  /**
   * Method description
   *
   *
   * @param publishingDate
   */
  public void setPublishingDate(Date publishingDate)
  {
    this.publishingDate = publishingDate;
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
  void prePersists()
  {
    creationDate = new Date();
  }

  /**
   * Method description
   *
   */
  void preUpdate()
  {
    lastUpdate = new Date();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Attachment> attachments;

  /** Field description */
  private User author;

  /** Field description */
  private Blog blog;

  /** Field description */
  private String content;

  /** Field description */
  private Date creationDate;

  /** Field description */
  private transient String displayContent;

  /** Field description */
  private Long id;

  /** Field description */
  private Date lastUpdate;

  /** Field description */
  private int navigationPosition;

  /** Field description */
  private String navigationTitle;

  /** Field description */
  private Page parent;

  /** Field description */
  private boolean published;

  /** Field description */
  private Date publishingDate;

  /** Field description */
  private String title;
}
