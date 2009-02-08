/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.mapping.MappingHandler;

import sonia.macro.MacroParser;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;

/**
 *
 * @author sdorra
 */
public class PluginActivator implements Activator
{

  /**
   * Method description
   *
   *
   * @param context
   */
  public void start(PluginContext context)
  {
    BlogContext ctx = BlogContext.getInstance();

    mappingHandler = ctx.getMappingHandler();
    mappingHandler.add(PdfViewerMapping.REGEX,
                               PdfViewerMapping.class);
    parser = MacroParser.getInstance();
    parser.putMacro(PdfViewerMacro.NAME, new PdfViewerMacro());
    parser.putMacro(CodeMacro.NAME, new CodeMacro());
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void stop(PluginContext context)
  {
    parser.removeMacro(CodeMacro.NAME);
    parser.removeMacro(PdfViewerMacro.NAME);
    mappingHandler.remove(PdfViewerMapping.REGEX);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private MappingHandler mappingHandler;

  /** Field description */
  private MacroParser parser;
}
