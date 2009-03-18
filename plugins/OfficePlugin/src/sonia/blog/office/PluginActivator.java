/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.editor.AttachmentHandler;
import sonia.blog.api.mapping.MappingHandler;

import sonia.macro.MacroParser;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;
import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;

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
    mappingHandler.add(PdfViewerMapping.REGEX, PdfViewerMapping.class);
    parser = MacroParser.getInstance();
    parser.putMacro(PdfViewerMacro.NAME, PdfViewerMacro.class);
    parser.putMacro(CodeMacro.NAME, CodeMacro.class);

    if (handler == null)
    {
      handler = new PdfHandler();
    }

    if ((handlerReference != null) &&!handlerReference.contains(handler))
    {
      handlerReference.add(handler);
    }
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

    if ((handler != null) && (handlerReference != null))
    {
      handlerReference.add(handler);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private PdfHandler handler;

  /** Field description */
  @Service(Constants.SERVICE_ATTACHMENTHANDLER)
  private ServiceReference<AttachmentHandler> handlerReference;

  /** Field description */
  private MappingHandler mappingHandler;

  /** Field description */
  private MacroParser parser;
}
