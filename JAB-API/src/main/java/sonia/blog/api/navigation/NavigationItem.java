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



package sonia.blog.api.navigation;

/**
 *
 * @author Sebastian Sdorra
 */
public class NavigationItem
{

  /**
   * Constructs ...
   *
   */
  public NavigationItem() {}

  /**
   * Constructs ...
   *
   *
   * @param label
   * @param action
   */
  public NavigationItem(String label, String action)
  {
    this.label = label;
    this.action = action;
  }

  /**
   * Constructs ...
   *
   *
   * @param label
   * @param href
   * @param target
   */
  public NavigationItem(String label, String href, String target)
  {
    this.label = label;
    this.href = href;
    this.target = target;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAction()
  {
    return action;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getHref()
  {
    return href;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTarget()
  {
    return target;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param action
   */
  public void setAction(String action)
  {
    this.action = action;
  }

  /**
   * Method description
   *
   *
   * @param href
   */
  public void setHref(String href)
  {
    this.href = href;
  }

  /**
   * Method description
   *
   *
   * @param label
   */
  public void setLabel(String label)
  {
    this.label = label;
  }

  /**
   * Method description
   *
   *
   * @param target
   */
  public void setTarget(String target)
  {
    this.target = target;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String action;

  /** Field description */
  private String href;

  /** Field description */
  private String label;

  /** Field description */
  private String target;
}
