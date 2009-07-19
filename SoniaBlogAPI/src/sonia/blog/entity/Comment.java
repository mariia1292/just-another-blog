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

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;

/**
 *
 * @author Sebastian Sdorra
 */
public class Comment implements Serializable, PermaObject
{

  /** Field description */
  private static final long serialVersionUID = 4507923974114945766L;

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
    if (!(object instanceof Comment))
    {
      return false;
    }

    Comment other = (Comment) object;

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
    return "sonia.blop.entity.Comment[id=" + id + "]";
  }

  //~--- get methods ----------------------------------------------------------

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
  public String getAuthorAddress()
  {
    return authorAddress;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorMail()
  {
    String result = null;

    if (author != null)
    {
      result = author.getEmail();
    }
    else
    {
      result = authorMail;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorName()
  {
    String result = null;

    if (author != null)
    {
      result = author.getDisplayName();
    }
    else
    {
      result = authorName;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAuthorURL()
  {
    String result = null;

    if (author != null)
    {
      result = author.getHomePage();
    }
    else
    {
      result = authorURL;
    }

    return result;
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
    return creationDate;
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
  public boolean isSpam()
  {
    return spam;
  }

  //~--- set methods ----------------------------------------------------------

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
   *
   * @param authorAddress
   */
  public void setAuthorAddress(String authorAddress)
  {
    this.authorAddress = authorAddress;
  }

  /**
   * Method description
   *
   *
   * @param authorMail
   */
  public void setAuthorMail(String authorMail)
  {
    this.authorMail = authorMail;
  }

  /**
   * Method description
   *
   *
   * @param authorName
   */
  public void setAuthorName(String authorName)
  {
    this.authorName = authorName;
  }

  /**
   * Method description
   *
   *
   * @param authorURL
   */
  public void setAuthorURL(String authorURL)
  {
    this.authorURL = authorURL;
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
   * @param spam
   */
  public void setSpam(boolean spam)
  {
    this.spam = spam;
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
  private User author;

  /** Field description */
  private String authorAddress;

  /** Field description */
  private String authorMail;

  /** Field description */
  private String authorName;

  /** Field description */
  private String authorURL;

  /** Field description */
  private String content;

  /** Field description */
  private Date creationDate;

  /** Field description */
  private Entry entry;

  /** Field description */
  private Long id;

  /** Field description */
  private boolean spam = false;
}
