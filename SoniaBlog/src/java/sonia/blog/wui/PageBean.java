/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.blog.wui;

import java.util.logging.Logger;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Page;

/**
 *
 * @author sdorra
 */
public class PageBean extends AbstractBean
{

  public static final String NAME = "PageBean";

  private static Logger logger = Logger.getLogger( PageBean.class.getName() );

  public PageBean()
  {
    super();
  }

  public Page getPage()
  {
    return page;
  }

  public void setPage(Page page)
  {
    this.page = page;
  }



  private Page page;

}
