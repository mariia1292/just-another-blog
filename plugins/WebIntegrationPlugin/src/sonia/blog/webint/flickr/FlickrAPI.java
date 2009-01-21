/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webint.flickr;

/**
 *
 * @author sdorra
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
