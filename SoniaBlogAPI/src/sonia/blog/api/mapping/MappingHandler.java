/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  public boolean handleMapping(BlogRequest request, BlogResponse response)
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
}
