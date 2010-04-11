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



package sonia.blog.api.template;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class Style
{

  /**
   * Method description
   *
   *
   * @param name
   * @param value
   */
  public void addAttribute(String name, String value)
  {
    if (attributes == null)
    {
      attributes = new ArrayList<StyleAttribute>();
    }

    attributes.add(new StyleAttribute(name, value));
  }

  /**
   * Method description
   *
   *
   * @param clazz
   */
  public void addClass(String clazz)
  {
    if (classes == null)
    {
      classes = new ArrayList<String>();
    }

    classes.add(clazz);
  }

  /**
   * Method description
   *
   *
   * @param selector
   */
  public void addSelector(String selector)
  {
    if (selectors == null)
    {
      selectors = new ArrayList<String>();
    }

    selectors.add(selector);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<StyleAttribute> getAttributes()
  {
    return attributes;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getBlock()
  {
    return block;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<String> getClasses()
  {
    return classes;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getInline()
  {
    return inline;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<String> getSelectors()
  {
    return selectors;
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
   * @param block
   */
  public void setBlock(String block)
  {
    this.block = block;
  }

  /**
   * Method description
   *
   *
   * @param inline
   */
  public void setInline(String inline)
  {
    this.inline = inline;
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private List<StyleAttribute> attributes;

  /** Field description */
  private String block;

  /** Field description */
  private List<String> classes;

  /** Field description */
  private String inline;

  /** Field description */
  private List<String> selectors;

  /** Field description */
  private String title;
}
