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



package sonia.blog.webint.flickr;

/**
 *
 * @author Sebastian Sdorra
 */
public interface FlickrAPI
{

  /** Field description */
  public static final String API_URL = "http://api.flickr.com/services/rest/";

  /** Field description */
  public static final String KEY = "cb415f129c7fd3ee11898afdaeeb97d3";

  /** Field description */
  public static final String METHOD_SEARCH = "flickr.photos.search";

  /** Field description */
  public static final String PARAM_APIKEY = "api_key";

  /** Field description */
  public static final String PARAM_METHOD = "method";

  /** Field description */
  public static final String PARAM_PERPAGE = "per_page";

  /** Field description */
  public static final String PARAM_TAGS = "tags";

  /*
   * 0 - Farm Id
   * 1 - Server Id
   * 2 - Photo Id
   * 3 - Secret
   * 4 - size (s,t,m,-,b,o)
   */

  /** Field description */
  public static final String PHOTO_URL =
    "http://farm{0}.static.flickr.com/{1}/{2}_{3}_{4}.jpg";

  /** Field description */
  public static final char SIZE_BIG = 'b';

  /** Field description */
  public static final char SIZE_MIDDLE = '-';

  /** Field description */
  public static final char SIZE_ORGINAL = 'o';

  /** Field description */
  public static final char SIZE_SMALL = 'm';

  /** Field description */
  public static final char SIZE_SMALLEST = 's';

  /** Field description */
  public static final char SIZE_THUMBNAIL = 't';
}
