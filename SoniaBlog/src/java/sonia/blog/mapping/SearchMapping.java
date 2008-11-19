/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import javax.faces.context.FacesContext;

/**
 *
 * @author sdorra
 */
public class SearchMapping extends AbstractMappingHandler
{

  /**
   * Method description
   *
   *
   * @param context
   * @param blog
   * @param args
   *
   * @return
   */
  public String handleMapping(FacesContext context, Blog blog, String[] args)
  {
    String result = "result.xhtml";

    if ((args != null) && (args.length > 0))
    {
      String searchString = "";

      for (int i = 0; i < args.length; i++)
      {
        searchString += args[i];

        if (i + 1 < args.length)
        {
          searchString += "/";
        }
      }

      getBlogBean(context).setSearchString(searchString);
    }

    return result;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMappingName()
  {
    return "search";
  }
}
