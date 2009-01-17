/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.devel;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.derby.drda.NetworkServerControl;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.MemberDAO;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.BlogMember;
import sonia.blog.entity.Category;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.io.PrintWriter;

import java.util.Date;
import java.util.Random;
import java.util.logging.Level;

/**
 *
 * @author sdorra
 */
public class DevelBean extends AbstractBean
{

  /**
   * Constructs ...
   *
   */
  public DevelBean()
  {
    super();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String createSampleData()
  {
    String result = SUCCESS;
    Encryption enc = getEncryption();
    String pwd = enc.encrypt("hallo123");
    DAOFactory daoFactory = BlogContext.getDAOFactory();
    UserDAO userDAO = daoFactory.getUserDAO();
    BlogDAO blogDAO = daoFactory.getBlogDAO();
    EntryDAO entryDAO = daoFactory.getEntryDAO();
    CommentDAO commentDAO = daoFactory.getCommentDAO();
    MemberDAO memberDAO = daoFactory.getMemberDAO();
    CategoryDAO categoryDAO = daoFactory.getCategoryDAO();

    for (int i = 0; i < blogs; i++)
    {
      User user = new User();

      user.setName("user" + i);
      user.setDisplayName("User " + i);
      user.setEmail("user" + i + "@jab.de");
      user.setPassword(pwd);
      user.setLastLogin(new Date());
      userDAO.add(user);

      for (int j = 0; j < blogs; j++)
      {
        Blog blog = new Blog();

        blog.setServername("blog" + j);
        blog.setTitle("Blog " + j);
        blog.setTemplate("jab");
        blog.setEmail("user" + j + "@jab.de");
        blog.setDescription("Description Blog " + j);
        blogDAO.add(blog);

        BlogMember member = new BlogMember(blog, user, Role.ADMIN);

        memberDAO.add(member);

        for (int k = 0; k < categories; k++)
        {
          Category category = new Category();

          category.setBlog(blog);
          category.setName("Category" + k);
          category.setDescription("Description Catgory " + k);
          categoryDAO.add(category);

          for (int l = 0; l < entries; l++)
          {
            Entry entry = new Entry();

            entry.setAuthor(user);
            entry.setCategory(category);
            entry.setPublished(true);
            entry.setTitle("Entry " + l);

            String content = createContent(l, 1000);

            entry.setContent(content);

            String teaser = createContent(l, 200);

            entry.setTeaser(teaser);
            entryDAO.add(entry);

            for (int m = 0; m < comments; m++)
            {
              Comment comment = new Comment();

              comment.setAuthorMail("user" + i + "@jab.de");
              comment.setAuthorAddress("127.0.0.1");
              comment.setAuthorName("User " + i);

              String commentContent = createContent(m, 100);

              comment.setContent(commentContent);
              comment.setEntry(entry);
              commentDAO.add(comment);
            }
          }
        }
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String startDBNetworkServer()
  {
    String result = SUCCESS;

    try
    {
      System.setProperty("derby.drda.startNetworkServer", "true");
      dbNetworkServer = new NetworkServerControl();
      dbNetworkServer.start(new PrintWriter(System.out));
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      result = FAILURE;
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String stopDBNetworkServer()
  {
    String result = SUCCESS;

    try
    {
      dbNetworkServer.shutdown();
      dbNetworkServer = null;
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      result = FAILURE;
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
  public int getBlogs()
  {
    return blogs;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getCategories()
  {
    return categories;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getComments()
  {
    return comments;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getEntries()
  {
    return entries;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getUsers()
  {
    return users;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isDBNetworkServerRunning()
  {
    return dbNetworkServer != null;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isDatabaseEmbedded()
  {
    return BlogContext.getInstance().getConfiguration().getBoolean(
        Constants.CONFIG_DB_EMBEDDED, Boolean.FALSE);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param blogs
   */
  public void setBlogs(int blogs)
  {
    this.blogs = blogs;
  }

  /**
   * Method description
   *
   *
   * @param categories
   */
  public void setCategories(int categories)
  {
    this.categories = categories;
  }

  /**
   * Method description
   *
   *
   * @param comments
   */
  public void setComments(int comments)
  {
    this.comments = comments;
  }

  /**
   * Method description
   *
   *
   * @param entries
   */
  public void setEntries(int entries)
  {
    this.entries = entries;
  }

  /**
   * Method description
   *
   *
   * @param users
   */
  public void setUsers(int users)
  {
    this.users = users;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param k
   * @param max
   *
   * @return
   */
  private String createContent(int k, int max)
  {
    String word = "content" + k + " ";
    StringBuffer buffer = new StringBuffer();
    int c = random.nextInt(max) + 1;

    for (int i = 0; i < c; i++)
    {
      buffer.append(word);
    }

    return buffer.toString();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private Encryption getEncryption()
  {
    ServiceRegistry registry = BlogContext.getInstance().getServiceRegistry();
    ServiceReference<Encryption> reference = registry.get(Encryption.class,
                                               Constants.SERVCIE_ENCRYPTION);

    return reference.get();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int blogs = 1;

  /** Field description */
  private int categories = 5;

  /** Field description */
  private int comments = 2;

  /** Field description */
  private NetworkServerControl dbNetworkServer;

  /** Field description */
  private int entries = 10;

  /** Field description */
  private int users = 20;

  /** Field description */
  private Random random = new Random();
}
