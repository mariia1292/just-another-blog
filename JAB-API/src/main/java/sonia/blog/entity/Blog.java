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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Sebastian Sdorra
 */
public class Blog implements Serializable, PermaObject, Comparable<Blog>
{

  /** Field description */
  private static final long serialVersionUID = 2936020289259434092L;

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param o
   *
   * @return
   */
  public int compareTo(Blog o)
  {
    int result = -1;

    if (o != null)
    {
      result = o.title.compareTo(title);

      if (result == 0)
      {
        result = creationDate.compareTo(o.creationDate);
      }
    }

    return result;
  }

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
    if (!(object instanceof Blog))
    {
      return false;
    }

    Blog other = (Blog) object;

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
    return title;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Category> getCategories()
  {
    if (categories == null)
    {
      categories = new ArrayList<Category>();
    }

    return categories;
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
  public String getDateFormat()
  {
    return dateFormat;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public DateFormat getDateFormatter()
  {
    Locale l = getLocale();

    if (l == null)
    {
      l = Locale.getDefault();
    }

    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, l);

    sdf.setTimeZone(getTimeZone());

    return sdf;
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
  public List<Entry> getEntries()
  {
    return entries;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getEntriesPerPage()
  {
    return entriesPerPage;
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
  public String getIdentifier()
  {
    return identifier;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getImageHeight()
  {
    return imageHeight;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getImageWidth()
  {
    return imageWidth;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Locale getLocale()
  {
    Locale result = null;

    if (!Util.isBlank(locale))
    {
      result = new Locale(locale);
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<BlogMember> getMembers()
  {
    if (members == null)
    {
      members = new ArrayList<BlogMember>();
    }

    return members;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Page> getPages()
  {
    return pages;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<BlogParameter> getParameters()
  {
    if (parameters == null)
    {
      parameters = new ArrayList<BlogParameter>();
    }

    return parameters;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getStartPage()
  {
    return startPage;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTemplate()
  {
    return template;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getThumbnailHeight()
  {
    return thumbnailHeight;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getThumbnailWidth()
  {
    return thumbnailWidth;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public TimeZone getTimeZone()
  {
    TimeZone result = null;

    if (timeZone != null)
    {
      result = TimeZone.getTimeZone(timeZone);
    }
    else
    {
      result = TimeZone.getDefault();
    }

    return result;
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
  public boolean isActive()
  {
    return active;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAllowCaching()
  {
    return allowCaching;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAllowComments()
  {
    return allowComments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAllowHtmlComments()
  {
    return allowHtmlComments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAllowMacros()
  {
    return allowMacros;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAllowTrackbacks()
  {
    return allowTrackbacks;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isSendAutoPing()
  {
    return sendAutoPing;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param active
   */
  public void setActive(boolean active)
  {
    this.active = active;
  }

  /**
   * Method description
   *
   *
   * @param allowCaching
   */
  public void setAllowCaching(boolean allowCaching)
  {
    this.allowCaching = allowCaching;
  }

  /**
   * Method description
   *
   *
   * @param allowComments
   */
  public void setAllowComments(boolean allowComments)
  {
    this.allowComments = allowComments;
  }

  /**
   * Method description
   *
   *
   * @param allowHtmlComments
   */
  public void setAllowHtmlComments(boolean allowHtmlComments)
  {
    this.allowHtmlComments = allowHtmlComments;
  }

  /**
   * Method description
   *
   *
   * @param allowMacros
   */
  public void setAllowMacros(boolean allowMacros)
  {
    this.allowMacros = allowMacros;
  }

  /**
   * Method description
   *
   *
   *
   * @param allowTrackbacks
   */
  public void setAllowTrackbacks(boolean allowTrackbacks)
  {
    this.allowTrackbacks = allowTrackbacks;
  }

  /**
   * Method description
   *
   *
   *
   * @param categories
   */
  public void setCategories(List<Category> categories)
  {
    this.categories = categories;
  }

  /**
   * Method description
   *
   *
   * @param dateFormat
   */
  public void setDateFormat(String dateFormat)
  {
    this.dateFormat = dateFormat;
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
   * @param entries
   */
  public void setEntries(List<Entry> entries)
  {
    this.entries = entries;
  }

  /**
   * Method description
   *
   *
   * @param entriesPerPage
   */
  public void setEntriesPerPage(int entriesPerPage)
  {
    this.entriesPerPage = entriesPerPage;
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
   * @param identifier
   */
  public void setIdentifier(String identifier)
  {
    this.identifier = identifier;
  }

  /**
   * Method description
   *
   *
   * @param imageHeight
   */
  public void setImageHeight(int imageHeight)
  {
    this.imageHeight = imageHeight;
  }

  /**
   * Method description
   *
   *
   * @param imageWidth
   */
  public void setImageWidth(int imageWidth)
  {
    this.imageWidth = imageWidth;
  }

  /**
   * Method description
   *
   *
   * @param locale
   */
  public void setLocale(Locale locale)
  {
    if (locale != null)
    {
      this.locale = locale.toString();
    }
    else
    {
      this.locale = null;
    }
  }

  /**
   * Method description
   *
   *
   * @param members
   */
  public void setMembers(List<BlogMember> members)
  {
    this.members = members;
  }

  /**
   * Method description
   *
   *
   * @param pages
   */
  public void setPages(List<Page> pages)
  {
    this.pages = pages;
  }

  /**
   * Method description
   *
   *
   * @param parameters
   */
  public void setParameters(List<BlogParameter> parameters)
  {
    this.parameters = parameters;
  }

  /**
   * Method description
   *
   *
   * @param sendAutoPing
   */
  public void setSendAutoPing(boolean sendAutoPing)
  {
    this.sendAutoPing = sendAutoPing;
  }

  /**
   * Method description
   *
   *
   * @param startPage
   */
  public void setStartPage(String startPage)
  {
    this.startPage = startPage;
  }

  /**
   * Method description
   *
   *
   * @param template
   */
  public void setTemplate(String template)
  {
    this.template = template;
  }

  /**
   * Method description
   *
   *
   * @param thumbnailHeight
   */
  public void setThumbnailHeight(int thumbnailHeight)
  {
    this.thumbnailHeight = thumbnailHeight;
  }

  /**
   * Method description
   *
   *
   * @param thumbnailWidth
   */
  public void setThumbnailWidth(int thumbnailWidth)
  {
    this.thumbnailWidth = thumbnailWidth;
  }

  /**
   * Method description
   *
   *
   * @param timeZone
   */
  public void setTimeZone(TimeZone timeZone)
  {
    this.timeZone = timeZone.getID();
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

    if (timeZone == null)
    {
      timeZone = TimeZone.getDefault().getID();
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<Category> categories;

  /** Field description */
  private Date creationDate;

  /** Field description */
  private String dateFormat = "yyyy-MM-dd HH:mm";

  /** Field description */
  private String description;

  /** Field description */
  private String email;

  /** Field description */
  private List<Entry> entries;

  /** Field description */
  private int entriesPerPage = 20;

  /** Field description */
  private Long id;

  /** Field description */
  private String identifier;

  /** Field description */
  private int imageHeight = 0;

  /** Field description */
  private int imageWidth = 640;

  /** Field description */
  private String locale;

  /** Field description */
  private List<BlogMember> members;

  /** Field description */
  private List<Page> pages;

  /** Field description */
  private List<BlogParameter> parameters;

  /** Field description */
  private boolean sendAutoPing = false;

  /** Field description */
  private boolean allowTrackbacks = true;

  /** Field description */
  private boolean allowMacros = true;

  /** Field description */
  private boolean allowHtmlComments = false;

  /** Field description */
  private boolean allowComments = true;

  /** Field description */
  private boolean allowCaching = true;

  /** Field description */
  private boolean active = true;

  /** Field description */
  private String startPage;

  /** Field description */
  private String template;

  /** Field description */
  private int thumbnailHeight = 0;

  /** Field description */
  private int thumbnailWidth = 200;

  /** Field description */
  private String timeZone;

  /** Field description */
  private String title;
}
