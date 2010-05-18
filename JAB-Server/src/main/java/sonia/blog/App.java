package sonia.blog;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.server.BlogServer;
import sonia.blog.server.BlogServerConfig;
import sonia.blog.server.BlogServerException;
import sonia.blog.server.BlogServerFactory;

import sonia.cli.Argument;
import sonia.cli.CliParser;
import sonia.cli.DefaultCliHelpBuilder;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App
{

  /** Field description */
  public static final String DEFAULT_CONFIG = "/jab-server.conf";

  /** Field description */
  public static final String PROPERTY_CONTEXTPATH = "context-path";

  /** Field description */
  public static final String PROPERTY_PORT = "port";

  /** Field description */
  public static final String PROPERTY_RESOURCEDIRECTORY = "resource-dir";

  /** Field description */
  private static Logger logger = Logger.getLogger(App.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param args
   *
   * @throws Exception
   */
  public static void main(String[] args) throws Exception
  {

    // disable security manager
    System.setSecurityManager(null);

    final App app = new App();
    StringBuffer resourceBuffer = new StringBuffer();

    resourceBuffer.append(System.getProperty("user.home"));
    resourceBuffer.append(File.separator).append(".jab");
    app.resourcePath = resourceBuffer.toString();

    CliParser parser = new CliParser();

    parser.parse(app, args);

    if (Util.isNotEmpty(app.configFile))
    {
      app.readConfigFromFile();
    }
    else
    {
      app.readConfigFromClasspath();
    }

    if (app.showHelp)
    {
      app.printHelp(parser);
    }
    else if (app.gui)
    {
      java.awt.EventQueue.invokeLater(new Runnable()
      {
        @Override
        public void run()
        {
          ConfigFrame frame = new ConfigFrame(app.resourcePath);

          frame.restoreState();
          frame.setVisible(true);
        }
      });
    }
    else
    {
      app.start();
    }
  }

  /**
   * Method description
   *
   *
   * @param in
   *
   * @throws IOException
   */
  public void readConfig(InputStream in) throws IOException
  {
    Properties properties = new Properties();

    properties.load(in);
    readProperties(properties);
  }

  /**
   * Method description
   *
   */
  public void readConfigFromFile()
  {
    File file = new File(configFile);

    if (file.exists())
    {
      InputStream in = null;

      try
      {
        in = new FileInputStream(file);
        readConfig(in);
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
      finally
      {
        try
        {
          in.close();
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
    else
    {
      logger.info("config file not found");
    }
  }

  /**
   * Method description
   *
   *
   * @throws Exception
   */
  public void start() throws Exception
  {
    printOptions();
    addShutdownHook();

    BlogServerConfig config = new BlogServerConfig(new File(resourcePath),
                                port, contextPath);

    server = BlogServerFactory.newServer(config);
    server.start();
  }

  /**
   * Method description
   *
   */
  public void stop()
  {
    if (server != null)
    {
      try
      {
        server.stop();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
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
  public String getContextPath()
  {
    return contextPath;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Boolean getGui()
  {
    return gui;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Integer getPort()
  {
    return port;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getResourcePath()
  {
    return resourcePath;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Boolean getShowHelp()
  {
    return showHelp;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param contextPath
   */
  public void setContextPath(String contextPath)
  {
    this.contextPath = contextPath;
  }

  /**
   * Method description
   *
   *
   * @param gui
   */
  public void setGui(Boolean gui)
  {
    this.gui = gui;
  }

  /**
   * Method description
   *
   *
   * @param port
   */
  public void setPort(Integer port)
  {
    this.port = port;
  }

  /**
   * Method description
   *
   *
   * @param resourcePath
   */
  public void setResourcePath(String resourcePath)
  {
    this.resourcePath = resourcePath;
  }

  /**
   * Method description
   *
   *
   * @param showHelp
   */
  public void setShowHelp(Boolean showHelp)
  {
    this.showHelp = showHelp;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void addShutdownHook()
  {
    Runtime.getRuntime().addShutdownHook(new Thread()
    {
      @Override
      public void run()
      {
        if (server != null)
        {
          try
          {
            server.stop();
          }
          catch (BlogServerException ex)
          {
            ex.printStackTrace();
          }
        }
      }
    });
  }

  /**
   * Method description
   *
   *
   * @param parser
   */
  private void printHelp(CliParser parser)
  {
    String s = System.getProperty("line.separator");
    StringBuffer prefix = new StringBuffer("Just-Another-Blog Server");

    prefix.append(s).append("usage: java -jar JAB-Server.jar [OPTIONS]");
    prefix.append(s);

    DefaultCliHelpBuilder helpBuilder =
      new DefaultCliHelpBuilder(prefix.toString(), null);

    System.out.println(parser.createHelp(helpBuilder, this));
  }

  /**
   * Method description
   *
   */
  private void printOptions()
  {
    System.out.println("start JAB-Server");
    System.out.append("  port:          ").println(port);
    System.out.append("  context-path:  ").println(contextPath);
    System.out.append("  resource-path: ").println(resourcePath);
    System.out.println();
  }

  /**
   * Method description
   *
   */
  private void readConfigFromClasspath()
  {
    InputStream in = Util.findResource(DEFAULT_CONFIG);

    if (in != null)
    {
      try
      {
        readConfig(in);
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
      finally
      {
        try
        {
          in.close();
        }
        catch (IOException ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param properties
   */
  private void readProperties(Properties properties)
  {
    contextPath = properties.getProperty(PROPERTY_CONTEXTPATH, contextPath);
    resourcePath = properties.getProperty(PROPERTY_RESOURCEDIRECTORY,
            resourcePath);

    try
    {
      port = Integer.parseInt(properties.getProperty(PROPERTY_PORT,
              String.valueOf(port)));
    }
    catch (NumberFormatException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Argument(
    value = "f",
    longName = "config-file",
    description = "Path to a jab-server config file"
  )
  private String configFile;

  /** Field description */
  @Argument(
    value = "c",
    longName = "context-path",
    description = "The Context-Path of the Jab-WebApp"
  )
  private String contextPath = "/";

  /** Field description */
  @Argument(
    value = "g",
    longName = "gui",
    description = "starts with the GUI"
  )
  private Boolean gui = Boolean.FALSE;

  /** Field description */
  @Argument(
    value = "p",
    longName = "port",
    description = "The port for the listener"
  )
  private Integer port = Integer.valueOf(8080);

  /** Field description */
  @Argument(
    value = "r",
    longName = "resource-path",
    description = "Path to the jab resource directory"
  )
  private String resourcePath;

  /** Field description */
  private BlogServer server;

  /** Field description */
  @Argument(
    value = "h",
    longName = "help",
    description = "Shows this help"
  )
  private Boolean showHelp = Boolean.FALSE;
}
