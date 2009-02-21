/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.wui;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.ResourceBundle;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author sdorra
 */
public class MemberBean extends AbstractBean
{

  /**
   * Method description
   *
   *
   *
   * @param event
   */
  public void roleChanged(ValueChangeEvent event)
  {
    BlogMember member = (BlogMember) members.getRowData();
    User user = member.getUser();
    Blog blog = member.getBlog();
    Role role = (Role) event.getNewValue();

    if (member != null)
    {
      try
      {
        UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();

        userDAO.setRole(blog, user, role);
        getMessageHandler().info("changeRoleSuccess");
      }
      catch (Exception ex)
      {
        getMessageHandler().error("changeRoleFailure");
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   * @return
   */
  public DataModel getMembers()
  {
    members = new ListDataModel();

    // TODO scrolling
    UserDAO userDAO = BlogContext.getDAOFactory().getUserDAO();
    List<BlogMember> memberList =
      userDAO.getMembers(getRequest().getCurrentBlog(), 0, 1000);

    if ((memberList != null) &&!memberList.isEmpty())
    {
      members.setWrappedData(memberList);
    }

    return members;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getRoleItems()
  {
    SelectItem[] items = new SelectItem[3];
    ResourceBundle bundle = getResourceBundle("label");

    items[0] = new SelectItem(Role.READER,
                              bundle.getString(Role.READER.name()));
    items[1] = new SelectItem(Role.AUTHOR,
                              bundle.getString(Role.AUTHOR.name()));
    items[2] = new SelectItem(Role.ADMIN, bundle.getString(Role.ADMIN.name()));

    return items;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DataModel members;
}
