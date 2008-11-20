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

    result.append(dateFormat.format(new Date(record.getMillis())));
    result.append(" ");
    result.append(record.getLevel().getName());
    result.append(" ");
    result.append("[" + record.getLoggerName() + "]");
    result.append(" ");

    String message = record.getMessage();
    Throwable thrown = record.getThrown();

    if ((message != null) && (thrown == null))
    {
      result.append(message);
    }
    else if ((message != null) && (thrown != null))
    {
      result.append(message + "\n");
      buildThrown(result, record, thrown);
    }
    else if ((message == null) && (thrown != null))
    {
      result.append(thrown.getMessage() + "\n");
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

    result.append("Exception in thread " + record.getThreadID() + " "
                  + thrown.getMessage() + "\n");

    for (StackTraceElement element : thrown.getStackTrace())
    {
      result.append("\tat " + element.getClassName() + "."
                    + element.getMethodName() + "(" + element.getFileName()
                    + ":" + element.getLineNumber() + ")\n");
    }

    return result.toString();
  }
}
