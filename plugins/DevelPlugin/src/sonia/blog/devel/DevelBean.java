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



package sonia.blog.devel;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.derby.drda.NetworkServerControl;

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequestListener;
import sonia.blog.api.app.BlogSession;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.api.dao.BlogDAO;
import sonia.blog.api.dao.CategoryDAO;
import sonia.blog.api.dao.CommentDAO;
import sonia.blog.api.dao.DAOFactory;
import sonia.blog.api.dao.EntryDAO;
import sonia.blog.api.dao.UserDAO;
import sonia.blog.api.util.AbstractBean;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Category;
import sonia.blog.entity.Comment;
import sonia.blog.entity.Entry;
import sonia.blog.entity.Role;
import sonia.blog.entity.User;

import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

import sonia.security.encryption.Encryption;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author Sebastian Sdorra
 */
public class DevelBean extends AbstractBean
{

  /** Field description */
  private static Logger logger = Logger.getLogger(DevelBean.class.getName());

  /** Field description */
  private static SimpleDateFormat sdf =
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public DevelBean()
  {
    super();
    perfRequestListener = new PerfRequestListener();
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
    CategoryDAO categoryDAO = daoFactory.getCategoryDAO();
    BlogSession session = getRequest().getBlogSession();

    for (int i = 0; i < blogs; i++)
    {
      User user = new User();

      user.setName("user" + i);
      user.setDisplayName("User " + i);
      user.setEmail("user" + i + "@jab.de");
      user.setPassword(pwd);
      user.setLastLogin(new Date());
      userDAO.add(session, user);

      for (int j = 0; j < blogs; j++)
      {
        Blog blog = new Blog();

        blog.setIdentifier("blog" + j);
        blog.setTitle("Blog " + j);
        blog.setTemplate("jab");
        blog.setEmail("user" + j + "@jab.de");
        blog.setDescription("Description Blog " + j);
        blogDAO.add(session, blog);
        userDAO.setRole(blog, user, Role.ADMIN);

        for (int k = 0; k < categories; k++)
        {
          Category category = new Category();

          category.setBlog(blog);
          category.setName("Category" + k);
          category.setDescription("Description Catgory " + k);
          categoryDAO.add(session, category);

          for (int l = 0; l < entries; l++)
          {
            Entry entry = new Entry();

            entry.setAuthor(user);
            entry.addCateogory(category);
            entry.setPublished(true);
            entry.setTitle("Entry " + l);

            String content = createContent(l, 1000);

            entry.setContent(content);

            String teaser = createContent(l, 200);

            entry.setTeaser(teaser);
            entryDAO.add(session, entry);

            for (int m = 0; m < comments; m++)
            {
              Comment comment = new Comment();

              comment.setAuthorMail("user" + i + "@jab.de");
              comment.setAuthorAddress("127.0.0.1");
              comment.setAuthorName("User " + i);

              String commentContent = createContent(m, 100);

              comment.setContent(commentContent);
              comment.setEntry(entry);
              commentDAO.add(session, comment);
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

  /**
   * Method description
   *
   *
   * @param event
   */
  public void toggleRequestListener(ValueChangeEvent event)
  {
    if (Boolean.TRUE.equals(event.getNewValue()))
    {
      if (!listenerReference.getAll().contains(perfRequestListener))
      {
        listenerReference.add(perfRequestListener);
      }
    }
    else
    {
      if (listenerReference.getAll().contains(perfRequestListener))
      {
        listenerReference.remove(perfRequestListener);
      }

      try
      {
        perfRequestListener.store();
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);
        getMessageHandler().error(getRequest(),ex.getMessage());
      }
    }
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
  public File getStatisticFile()
  {
    return statisticFile;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public SelectItem[] getStatisticFiles()
  {
    SelectItem[] items = null;
    ResourceManager resManager = BlogContext.getInstance().getResourceManager();
    File directory = resManager.getDirectory("plugin" + File.separator
                       + "devel");

    if (directory.exists())
    {
      File[] files = directory.listFiles(new FilenameFilter()
      {
        public boolean accept(File dir, String name)
        {
          return name.endsWith(".xml");
        }
      });
      int size = files.length;

      items = new SelectItem[size];

      for (int i = 0; i < size; i++)
      {
        try
        {
          String name = files[i].getName();

          if (name.length() > 4)
          {
            name = name.substring(0, name.length() - 4);

            long time = Long.parseLong(name);

            name = sdf.format(new Date(time));
            items[i] = new SelectItem(files[i], name);
          }
        }
        catch (NumberFormatException ex)
        {
          logger.log(Level.WARNING, null, ex);
        }
      }
    }

    return items;
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

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isRequestListener()
  {
    return requestListener;
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
   * @param requestListener
   */
  public void setRequestListener(boolean requestListener)
  {
    this.requestListener = requestListener;
  }

  /**
   * Method description
   *
   *
   *
   * @param statisticFile
   */
  public void setStatisticFile(File statisticFile)
  {
    this.statisticFile = statisticFile;
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
  @Service(Constants.SERVICE_REQUESTLISTENER)
  private ServiceReference<BlogRequestListener> listenerReference;

  /** Field description */
  private PerfRequestListener perfRequestListener;

  /** Field description */
  private boolean requestListener = false;

  /** Field description */
  private File statisticFile;

  /** Field description */
  private int users = 20;

  /** Field description */
  private Random random = new Random();
}
