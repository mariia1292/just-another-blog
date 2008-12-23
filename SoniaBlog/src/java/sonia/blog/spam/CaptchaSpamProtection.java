/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.MappingEntry;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.api.spam.SpamInputProtection;
import sonia.blog.entity.PermaObject;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.OutputStream;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.ResponseWriter;

import javax.imageio.ImageIO;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class CaptchaSpamProtection implements SpamInputProtection
{

  /** Field description */
  public static final String LABEL = "Captcha";

  /** Field description */
  public static final String SESSIONVAR = "sonia.blog.spam.captcha";

  /** Field description */
  private static final char[] CHARARRAY =
  {
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'i', 'J', 'K', 'L', 'M', 'N', 'o',
    'P', 'Q', 'R', 'S', 'T', 'u', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5',
    '6', '7', '8', '9'
  };

  /** Field description */
  private static final int HEIGHT = 44;

  /** Field description */
  private static final int SPACE = 16;

  /** Field description */
  private static final int WIDTH = 134;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(CaptchaSpamProtection.class.getName());

  /** Field description */
  private static final Random RANDOM = new Random();

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public CaptchaSpamProtection()
  {
    MappingHandler handler = BlogContext.getInstance().getMappingHandler();

    if (!handler.containsPath("/captcha.jab"))
    {
      handler.addMappging("/captcha.jab", new CaptchaMappingHandler());
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param writer
   *
   * @return
   *
   * @throws IOException
   */
  public String renderInput(BlogRequest request, ResponseWriter writer)
          throws IOException
  {
    LinkBuilder linkBuilder = BlogContext.getInstance().getLinkBuilder();
    char[] buffer = new char[6];

    for (int i = 0; i < 6; i++)
    {
      buffer[i] = CHARARRAY[RANDOM.nextInt(CHARARRAY.length)];
    }

    String text = new String(buffer);
    String link = linkBuilder.buildLink(request, "/captcha.jab");

    request.getSession().setAttribute(SESSIONVAR, text);
    writer.startElement("img", null);
    writer.writeAttribute("src", link, null);
    writer.writeAttribute("style", "width: " + WIDTH + ";height: " + HEIGHT,
                          null);
    writer.endElement("img");

    return text;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getLabel()
  {
    return LABEL;
  }

  //~--- inner classes --------------------------------------------------------

  /**
   *
   * @author     sdorra
   */
  private class CaptchaMappingHandler implements MappingEntry
  {

    /**
     * Method description
     *
     *
     * @param request
     * @param response
     * @param param
     *
     * @return
     */
    public boolean handleMapping(BlogRequest request, BlogResponse response,
                                 String[] param)
    {
      OutputStream out = null;

      try
      {
        String text = (String) request.getSession().getAttribute(SESSIONVAR);

        response.setContentType("image/pjpeg");
        out = response.getOutputStream();

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
                                BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.green);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        graphics.setColor(Color.blue);
        graphics.setFont(new Font("Monospaced", Font.BOLD + Font.ITALIC, 32));
        graphics.drawString(text, 8, 32);
        graphics.setColor(Color.white);

        for (int i = -HEIGHT; i < WIDTH; i += SPACE)
        {
          graphics.drawLine(i, 0, i + HEIGHT, HEIGHT);
          graphics.drawLine(i, HEIGHT, i + HEIGHT, 0);
        }

        ImageIO.write(image, "jpeg", out);
        out.close();
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);

        try
        {
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        catch (IOException iex)
        {
          logger.log(Level.SEVERE, null, iex);
        }
      }
      finally
      {
        try
        {
          if (out != null)
          {
            out.close();
          }
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
        }
      }

      return false;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Method description
     *
     *
     * @param request
     * @param linkBuilder
     * @param object
     *
     * @return
     */
    public String getUri(BlogRequest request, LinkBuilder linkBuilder,
                         PermaObject object)
    {
      return null;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public boolean isNavigationRendered()
    {
      return false;
    }
  }
}
