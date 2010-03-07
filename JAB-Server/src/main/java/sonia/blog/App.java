package sonia.blog;

//~--- non-JDK imports --------------------------------------------------------

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import sonia.cli.Argument;
import sonia.cli.CliParser;
import sonia.cli.DefaultCliHelpBuilder;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App
{

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
    final App app = new App();
    StringBuffer resourceBuffer = new StringBuffer();

    resourceBuffer.append(System.getProperty("user.home"));
    resourceBuffer.append(File.separator).append(".jab");
    app.resourcePath = resourceBuffer.toString();

    CliParser parser = new CliParser();

    parser.parse(app, args);

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
          new ConfigFrame(app).setVisible(true);
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
   * @throws Exception
   */
  public void start() throws Exception
  {
    printOptions();
    server = new Server();
    addShutdownHook();

    Connector connector = new SelectChannelConnector();

    connector.setPort(port);
    server.addConnector(connector);

    WebAppContext wac = new WebAppContext();

    wac.setContextPath(contextPath);

    File resourceDir = new File(resourcePath, "webapp");

    if (!resourceDir.exists() &&!resourceDir.mkdirs())
    {
      throw new IOException("could not create directory");
    }

    File tempDirectory = new File(resourceDir, "temp");

    if (!tempDirectory.exists() &&!tempDirectory.mkdirs())
    {
      throw new IOException("could not create directory");
    }

    File warFile = new File(resourceDir, "jab.war");

    if (warFile.exists())
    {
      if (isNew(warFile))
      {
        updateWebApp(warFile);
      }
    }
    else
    {
      copyWebApp(warFile);
    }

    wac.setWar(warFile.getAbsolutePath());
    wac.setExtractWAR(true);
    wac.setTempDirectory(tempDirectory);
    server.setHandler(wac);
    server.setStopAtShutdown(true);
    server.start();
    server.join();
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
  public Server getServer()
  {
    return server;
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
   * @param server
   */
  public void setServer(Server server)
  {
    this.server = server;
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
          catch (Exception ex)
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
   * @param warFile
   *
   * @throws IOException
   */
  private void copyWebApp(File warFile) throws IOException
  {
    InputStream in = null;
    FileOutputStream out = null;

    try
    {
      in = getWebApp();
      out = new FileOutputStream(warFile);
      Util.copy(in, out);
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }

      if (out != null)
      {
        out.close();
      }
    }
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
   *
   * @param warFile
   *
   * @throws IOException
   */
  private void updateWebApp(File warFile) throws IOException
  {
    System.out.println("Update WebApp");

    if (!warFile.delete())
    {
      throw new IOException("could not delete war-file");
    }

    copyWebApp(warFile);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private InputStream getWebApp()
  {
    return App.class.getResourceAsStream("/webapp/jab.war");
  }

  /**
   * Method description
   *
   *
   * @param warFile
   *
   * @return
   *
   * @throws IOException
   */
  private boolean isNew(File warFile) throws IOException
  {
    String checksum = ChecksumUtil.createChecksum(warFile);
    String newChecksum = ChecksumUtil.createChecksum(getWebApp());

    return !checksum.equals(newChecksum);
  }

  //~--- fields ---------------------------------------------------------------

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
  private Server server;

  /** Field description */
  @Argument(
    value = "h",
    longName = "help",
    description = "Shows this help"
  )
  private Boolean showHelp = Boolean.FALSE;
}
