/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.logging;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author sdorra
 */
public class SimpleFormatter extends Formatter
{

  /** Field description */
  private static SimpleDateFormat dateFormat =
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param record
   *
   * @return
   */
  @Override
  public String format(LogRecord record)
  {
    StringBuffer result = new StringBuffer();
    Date date = new Date(record.getMillis());

    result.append(dateFormat.format(date));
    result.append(" ").append(record.getLevel().getName()).append(" ");
    result.append("[").append(record.getLoggerName()).append("]");
    result.append(" ");

    String message = record.getMessage();
    Throwable thrown = record.getThrown();

    if ((message != null) && (thrown == null))
    {
      result.append(message);
    }
    else if ((message != null) && (thrown != null))
    {
      result.append(message).append("\n");
      buildThrown(result, record, thrown);
    }
    else if ((message == null) && (thrown != null))
    {
      result.append(thrown.getMessage()).append("\n");
      buildThrown(result, record, thrown);
    }

    return result.toString();
  }

  /**
   * Method description
   *
   *
   *
   * @param result
   * @param record
   * @param thrown
   *
   * @return
   */
  private String buildThrown(StringBuffer result, LogRecord record,
                             Throwable thrown)
  {
    if (thrown.getCause() != null)
    {
      buildThrown(result, record, thrown.getCause());
    }

    result.append(thrown.getClass().getName());
    result.append(": ").append(thrown.getMessage()).append("\n");

    for (StackTraceElement element : thrown.getStackTrace())
    {
      result.append("  at ").append(element.getClassName()).append(".");
      result.append(element.getMethodName()).append("(");
      result.append(element.getFileName()).append(":");
      result.append(element.getLineNumber()).append("\n");
    }

    return result.toString();
  }
}
