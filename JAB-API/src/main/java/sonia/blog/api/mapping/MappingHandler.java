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



package sonia.blog.api.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.Map;

import javax.servlet.ServletException;

/**
 *
 * @author Sebastian Sdorra
 */
public interface MappingHandler
{

  /**
   *  Method description
   *
   *
   *
   *  @param regex
   *  @param mapping
   */
  public void add(String regex, Class<? extends Mapping> mapping);

  /**
   * Method description
   *
   *
   * @param mapping
   */
  public void add(Class<? extends Mapping> mapping);

  /**
   * Method description
   *
   *
   *
   * @param regex
   *
   * @return
   */
  public boolean contains(String regex);

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param instructions
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public boolean handleMapping(BlogRequest request, BlogResponse response,
                               MappingInstructions instructions)
          throws IOException, ServletException;

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  public void load(InputStream in) throws IOException;

  /**
   * Method description
   *
   *
   * @param mapping
   */
  public void remove(Class<? extends Mapping> mapping);

  /**
   * Method description
   *
   *
   *
   * @param regex
   */
  public void remove(String regex);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param regex
   *
   * @return
   */
  public Class<? extends Mapping> get(String regex);

  /**
   * Method description
   *
   *
   * @return
   */
  public Map<String, Class<? extends Mapping>> getAll();

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  public MappingInstructions getMappingInstructions(BlogRequest request);
}
