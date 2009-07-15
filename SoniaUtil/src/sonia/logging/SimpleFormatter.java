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


package sonia.logging;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author Sebastian Sdorra
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

    if (dateFormat != null)
    {
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