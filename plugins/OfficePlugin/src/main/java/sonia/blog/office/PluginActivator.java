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



package sonia.blog.office;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.editor.AttachmentHandler;
import sonia.blog.api.mapping.MappingHandler;

import sonia.macro.MacroParser;

import sonia.plugin.Activator;
import sonia.plugin.PluginContext;
import sonia.plugin.service.Service;
import sonia.plugin.service.ServiceReference;
import sonia.plugin.service.ServiceRegistry;

/**
 *
 * @author Sebastian Sdorra
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
    mappingHandler.add(PdfViewerMapping.class);
    parser = MacroParser.getInstance();
    parser.putMacro(PdfViewerMacro.NAME, PdfViewerMacro.class);

    if (handler == null)
    {
      handler = new PdfHandler();
    }

    if ((handlerReference != null) &&!handlerReference.contains(handler))
    {
      handlerReference.add(handler);
    }

    if (renderer == null)
    {
      renderer = new DefaultPDFRenderer();
    }

    getRendererReference().add(renderer);
  }

  /**
   * Method description
   *
   *
   * @param context
   */
  public void stop(PluginContext context)
  {
    parser.removeMacroFactory(PdfViewerMacro.NAME);
    mappingHandler.remove(PdfViewerMapping.class);

    if ((handler != null) && (handlerReference != null))
    {
      handlerReference.remove(handler);
    }

    if ((renderer != null) && (rendererReference != null))
    {
      rendererReference.remove(renderer);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  private ServiceReference<PDFRenderer> getRendererReference()
  {
    if (rendererReference == null)
    {
      rendererReference = registry.register(PDFRenderer.class,
              PDFRenderer.SERVICE);
    }

    return rendererReference;
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

  /** Field description */
  @Context
  private ServiceRegistry registry;

  /** Field description */
  private PDFRenderer renderer;

  /** Field description */
  private ServiceReference<PDFRenderer> rendererReference;
}
