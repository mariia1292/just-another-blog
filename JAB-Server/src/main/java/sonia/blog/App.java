package sonia.blog;

//~--- non-JDK imports --------------------------------------------------------

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import sonia.cli.Argument;
import sonia.cli.CliHelpBuilder;
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
    App app = new App();
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
    System.out.println("copy war-file");

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
