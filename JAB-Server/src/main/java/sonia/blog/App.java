package sonia.blog;

//~--- non-JDK imports --------------------------------------------------------

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import sonia.cli.Argument;
import sonia.cli.CliParser;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

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
    CliParser parser = new CliParser();

    parser.parse(app, args);

    if (app.showHelp)
    {
      System.out.println(parser.createHelp(app));
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
    connector.setHost("127.0.0.1");
    server.addConnector(connector);

    WebAppContext wac = new WebAppContext();

    wac.setContextPath(contextPath);
    wac.setBaseResource(new ResourceCollection(new String[] {
      "/tmp/jetty/webapps" }));
    wac.setResourceAlias("/WEB-INF/classes/", "/classes/");
    wac.setTempDirectory(new File("/tmp/jetty/temp"));
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
  private Server server;

  /** Field description */
  @Argument(
    value = "t",
    longName = "temp",
    description = "Path to the webapp temp directory"
  )
  private String temp = "/tmp/jab";

  /** Field description */
  @Argument(
    value = "h",
    longName = "help",
    description = "Shows this help"
  )
  private Boolean showHelp = Boolean.FALSE;
}
