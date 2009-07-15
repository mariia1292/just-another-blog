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


package sonia.image;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Dimension;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Sebastian Sdorra
 */
public interface ImageFileHandler
{

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param x
   * @param y
   * @param width
   * @param height
   *
   *
   * @throws IOException
   */
  public void cropImage(File in, File out, String format, int x, int y,
                        int width, int height)
          throws IOException;

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param background
   * @param maxWidth
   * @param maxHeight
   *
   *
   * @throws IOException
   */
  public void scaleImage(File in, File out, String format, Color background,
                         int maxWidth, int maxHeight)
          throws IOException;

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param maxWidth
   * @param maxHeight
   *
   *
   * @throws IOException
   */
  public void scaleImage(File in, File out, String format, int maxWidth,
                         int maxHeight)
          throws IOException;

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param width
   * @param height
   *
   *
   * @throws IOException
   */
  public void scaleImageFix(File in, File out, String format, int width,
                            int height)
          throws IOException;

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param maxHeight
   *
   *
   * @throws IOException
   */
  public void scaleImageMaxHeight(File in, File out, String format,
                                  int maxHeight)
          throws IOException;

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param maxWidth
   *
   *
   * @throws IOException
   */
  public void scaleImageMaxWidth(File in, File out, String format, int maxWidth)
          throws IOException;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   *
   * @return
   *
   * @throws IOException
   */
  public Dimension getDimension(File in) throws IOException;
}