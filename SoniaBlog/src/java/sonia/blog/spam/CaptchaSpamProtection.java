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



package sonia.blog.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.link.LinkBuilder;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.api.spam.SpamInputProtection;

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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
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

  /** Field description */
  private static final long serialVersionUID = -7729920336258957980L;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public CaptchaSpamProtection()
  {
    MappingHandler handler = BlogContext.getInstance().getMappingHandler();

    if (!handler.contains("^/captcha.jab$"))
    {
      handler.add("^/captcha.jab$", CaptchaMapping.class);
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
  private static class CaptchaMapping extends FinalMapping
  {

    /**
     * Method description
     *
     *
     * @param request
     * @param response
     * @param param
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void handleFinalMapping(BlogRequest request,
                                      BlogResponse response, String[] param)
            throws IOException, ServletException
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
    }
  }
}
