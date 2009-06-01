/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author sdorra
 */
public class BlogResponse extends HttpServletResponseWrapper
{

  /**
   * Constructs ...
   *
   *
   * @param response
   */
  public BlogResponse(HttpServletResponse response)
  {
    super(response);
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws IOException
   */
  public void finish() throws IOException
  {
    if (cacheResonse && (writer != null) && (baos != null))
    {
      writer.flush();

      PrintWriter orgWriter = super.getWriter();

      orgWriter.write(baos.toString());
      orgWriter.close();
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public byte[] getContent()
  {
    byte[] content = null;

    if ((writer != null) && (baos != null))
    {
      writer.flush();
      content = baos.toByteArray();
    }

    return content;
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws IOException
   */
  @Override
  public PrintWriter getWriter() throws IOException
  {
    writer = null;

    if (cacheResonse)
    {
      baos = new ByteArrayOutputStream();
      writer = new PrintWriter(baos);
    }
    else
    {
      writer = super.getWriter();
    }

    return writer;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isCacheResonse()
  {
    return cacheResonse;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param cacheResonse
   */
  public void setCacheResonse(boolean cacheResonse)
  {
    this.cacheResonse = cacheResonse;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ByteArrayOutputStream baos;

  /** Field description */
  private boolean cacheResonse = false;

  /** Field description */
  private PrintWriter writer;
}
