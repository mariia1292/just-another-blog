/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.logging;

//~--- JDK imports ------------------------------------------------------------

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 *
 * @author sdorra
 */
public class FileHandler extends Handler implements PropertyChangeListener
{

  /** Field description */
  private static SimpleDateFormat dateFormat =
    new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public FileHandler()
  {
    LogManager.getLogManager().addPropertyChangeListener(this);
    init();
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws SecurityException
   */
  @Override
  public void close() throws SecurityException
  {
    writer.close();
  }

  /**
   * Method description
   *
   */
  @Override
  public void flush()
  {
    writer.flush();
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void propertyChange(PropertyChangeEvent event)
  {
    if (event.getPropertyName().startsWith("sonia.logging.FileHandler"))
    {
      init();
    }
  }

  /**
   * Method description
   *
   *
   * @param record
   */
  @Override
  public void publish(LogRecord record)
  {
    if (file.length() >= (maxSize * 1024l * 1024l))
    {
      moveFile();
    }

    writer.println(formatter.format(record));
  }

  /**
   * Method description
   *
   *
   * @param manager
   */
  protected void initDirectory(LogManager manager)
  {
    String directoryProperty =
      manager.getProperty("sonia.logging.FileHandler.directory");

    if (directoryProperty != null)
    {
      directory = new File(directoryProperty);

      if (!directory.exists() ||!directory.isDirectory())
      {
        directory = null;
      }
    }

    if (directory == null)
    {
      directory = new File(System.getProperty("java.io.tmpdir"));
    }
  }

  /**
   * Method description
   *
   *
   * @param manager
   */
  protected void initFilename(LogManager manager)
  {
    String filenameProperty =
      manager.getProperty("sonia.logging.FileHandler.filename");

    if (filenameProperty != null)
    {
      filename = filenameProperty;
    }

    if (filename == null)
    {
      filename = "sonia.log";
    }
  }

  /**
   * Method description
   *
   *
   * @param manager
   */
  protected void initFormatter(LogManager manager)
  {
    String formatterProperty =
      manager.getProperty("sonia.logging.FileHandler.formatter");

    if (formatterProperty != null)
    {
      try
      {
        formatter = (Formatter) Class.forName(formatterProperty).newInstance();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }

    if (formatter == null)
    {
      formatter = new SimpleFormatter();
    }
  }

  /**
   * Method description
   *
   *
   * @param manager
   */
  protected void initMaxSize(LogManager manager)
  {
    String maxSizeProperty =
      manager.getProperty("sonia.logging.FileHandler.maxSize");

    if (maxSizeProperty != null)
    {
      try
      {
        maxSize = Integer.parseInt(maxSizeProperty);
      }
      catch (NumberFormatException ex)
      {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Method description
   *
   */
  private synchronized void init()
  {
    LogManager manager = LogManager.getLogManager();

    // init formatter
    initFormatter(manager);

    // init directory
    initDirectory(manager);

    // init filename
    initFilename(manager);

    // init maxSize
    initMaxSize(manager);
    file = new File(directory, filename);

    try
    {
      writer = new PrintWriter(file);
    }
    catch (FileNotFoundException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Method description
   *
   */
  private void moveFile()
  {
    writer.println("rotate log");
    writer.close();

    File moveFile = new File(directory,
                             filename + "-" + dateFormat.format(new Date()));

    if (!file.renameTo(moveFile))
    {
      throw new RuntimeException("could not rename the file");
    }

    file = new File(directory, filename);

    try
    {
      writer = new PrintWriter(file);
    }
    catch (FileNotFoundException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected File directory;

  /** Field description */
  protected File file;

  /** Field description */
  protected String filename;

  /** Field description */
  protected Formatter formatter;

  /** Field description */
  protected long maxSize = 100;

  /** Field description */
  protected PrintWriter writer;
}
