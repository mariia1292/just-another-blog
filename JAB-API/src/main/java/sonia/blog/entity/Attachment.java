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

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;

/**
 *
 * @author Sebastian Sdorra
 */
public class Attachment implements Serializable, PermaObject, FileObject
{

  /** Field description */
  private static final long serialVersionUID = 6433042381384640712L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param object
   *
   * @return
   */
  @Override
  public boolean equals(Object object)
  {

    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Attachment))
    {
      return false;
    }

    Attachment other = (Attachment) object;

    if (((this.id == null) && (other.id != null))
        || ((this.id != null) &&!this.id.equals(other.id)))
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
    int hash = 0;

    hash += ((id != null)
             ? id.hashCode()
             : 0);

    return hash;
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
    return "sonia.blop.entity.Attachment[id=" + id + "]";
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getCreationDate()
  {
    return creationDate;
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
  public Entry getEntry()
  {
    return entry;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getExtension()
  {
    String extension = Util.getExtension(name);

    if (extension != null)
    {
      extension = extension.toLowerCase();
    }

    return extension;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getFilePath()
  {
    return filePath;
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
  public String getMimeType()
  {
    return mimeType;
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
  public Page getPage()
  {
    return page;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getSize()
  {
    return fileSize;
  }

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public boolean isBlog(Blog blog)
  {
    return ((entry != null) && entry.getBlog().equals(blog))
           || ((page != null) && page.getBlog().equals(blog));
  }

  //~--- set methods ----------------------------------------------------------

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
   * @param entry
   */
  public void setEntry(Entry entry)
  {
    this.entry = entry;
  }

  /**
   * Method description
   *
   *
   * @param filePath
   */
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
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
   * @param mimeType
   */
  public void setMimeType(String mimeType)
  {
    this.mimeType = mimeType;
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
   * @param page
   */
  public void setPage(Page page)
  {
    this.page = page;
  }

  /**
   * Method description
   *
   *
   * @param size
   */
  public void setSize(long size)
  {
    this.fileSize = size;
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Date creationDate;

  /** Field description */
  private String description;

  /** Field description */
  private Entry entry;

  /** Field description */
  private String filePath;

  /** Field description */
  private long fileSize;

  /** Field description */
  private Long id;

  /** Field description */
  private String mimeType;

  /** Field description */
  private String name;

  /** Field description */
  private Page page;
}
