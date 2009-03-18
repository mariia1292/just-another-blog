/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.logging;

//~--- JDK imports ------------------------------------------------------------

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 *
 * @author sdorra
 */
public class ConsoleHandler extends Handler implements PropertyChangeListener
{

  /**
   * Constructs ...
   *
   */
  public ConsoleHandler()
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

    // do nothing
  }

  /**
   * Method description
   *
   */
  @Override
  public void flush()
  {
    System.out.flush();
  }

  /**
   * Method description
   *
   *
   * @param event
   */
  public void propertyChange(PropertyChangeEvent event)
  {
    if (event.getPropertyName().equals(
            "sonia.logging.ConsoleHandler.formatter"))
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
    System.out.println(formatter.format(record));
  }

  /**
   * Method description
   *
   */
  protected synchronized void init()
  {
    String formatterProperty = LogManager.getLogManager().getProperty(
                                   "sonia.logging.ConsoleHandler.formatter");

    try
    {
      formatter = (Formatter) Class.forName(formatterProperty).newInstance();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    if (formatter == null)
    {
      formatter = new SimpleFormatter();
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Formatter formatter;
}
