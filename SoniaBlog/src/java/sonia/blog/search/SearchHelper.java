/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.search;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import sonia.blog.entity.Entry;

//~--- JDK imports ------------------------------------------------------------

import java.io.Reader;
import java.io.StringReader;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author sdorra
 */
public class SearchHelper
{

  /** Field description */
  private static Logger logger = Logger.getLogger(SearchHelper.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param entry
   *
   * @return
   */
  public static Document buildDocument(Entry entry)
  {
    Document doc = new Document();

    doc.add(new Field("tid", Entry.class.getName() + "-" + entry.getId(),
                      Field.Store.YES, Field.Index.NOT_ANALYZED));
    doc.add(new Field("type", Entry.class.getName(), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field("id", entry.getId().toString(), Field.Store.YES,
                      Field.Index.NOT_ANALYZED));
    doc.add(new Field("author", entry.getAuthor().getDisplayName(),
                      Field.Store.YES, Field.Index.NOT_ANALYZED));
    doc.add(new Field("creationDate",
                      DateTools.timeToString(entry.getCreationDate().getTime(),
                        DateTools.Resolution.SECOND), Field.Store.YES,
                          Field.Index.NOT_ANALYZED));

    if (entry.getLastUpdate() != null)
    {
      doc.add(new Field("lastUpdate",
                        DateTools.timeToString(entry.getLastUpdate().getTime(),
                          DateTools.Resolution.SECOND), Field.Store.YES,
                            Field.Index.NOT_ANALYZED));
    }

    doc.add(new Field("title", entry.getTitle(), Field.Store.YES,
                      Field.Index.ANALYZED));
    doc.add(new Field("content", extractText(entry.getContent()),
                      Field.Store.YES, Field.Index.ANALYZED));

    return doc;
  }

  /**
   * Method description
   *
   *
   * @param content
   *
   * @return
   */
  public static String extractText(String content)
  {
    try
    {
      EditorKit kit = new HTMLEditorKit();
      javax.swing.text.Document doc = kit.createDefaultDocument();

      doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

      Reader reader = new StringReader(content);

      kit.read(reader, doc, 0);
      content = doc.getText(0, doc.getLength());
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }

    return content;
  }
}
