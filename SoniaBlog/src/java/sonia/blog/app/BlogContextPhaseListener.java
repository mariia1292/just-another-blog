/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.Constants;
import sonia.blog.api.mapping.MappingHandler;
import sonia.blog.entity.Blog;
import sonia.blog.wui.BlogBean;

import sonia.plugin.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author sdorra
 */
public class BlogContextPhaseListener implements PhaseListener
{

  /** Field description */
  private static final long serialVersionUID = 1195642361741262521L;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(BlogContextPhaseListener.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param event
   */
  public void afterPhase(PhaseEvent event) {}

  /**
   * Method description
   *
   *
   * @param event
   */
  public void beforePhase(PhaseEvent event)
  {
    if (event.getPhaseId() == PhaseId.RESTORE_VIEW)
    {
      FacesContext context = event.getFacesContext();

      applyMessages(context);

      if (BlogContext.getInstance().isInstalled())
      {
        Object req = context.getExternalContext().getRequest();
        BlogRequest request = null;

        if (req instanceof BlogRequest)
        {
          request = (BlogRequest) req;
        }
        else
        {
          request = new BlogRequest((HttpServletRequest) req);
        }

        BlogBean blogBean =
          (BlogBean) request.getSession().getAttribute("BlogBean");

        if (blogBean != null)
        {
          blogBean.setNextUri(null);
          blogBean.setPrevUri(null);
        }

        Blog blog = request.getCurrentBlog();
        String uri = request.getRequestURI();

        uri = uri.substring(request.getContextPath().length());
        uri = uri.substring(5);

        String mapping = null;

        if (uri.length() > 1)
        {
          uri = uri.substring(1);

          try
          {
            uri = URLDecoder.decode(uri, "UTF-8");
          }
          catch (UnsupportedEncodingException ex)
          {
            logger.log(Level.SEVERE, null, ex);
          }

          String[] parts = uri.split("/");

          if (parts.length > 0)
          {
            String mappingName = parts[0];

            if (!(mappingName.equals("personal")
                  || mappingName.equals("install")))
            {
              List<MappingHandler> mappingHandlers = getMappingHandlers();

              if (mappingHandlers != null)
              {
                for (MappingHandler handler : mappingHandlers)
                {
                  if (mappingName.equals(handler.getMappingName()))
                  {
                    String[] args = new String[parts.length - 1];

                    System.arraycopy(parts, 1, args, 0, parts.length - 1);
                    mapping = handler.handleMapping(context, blog, args);

                    break;
                  }
                }
              }
            }
          }
        }
        else
        {
          MappingHandler handler = getDefaultMappingHander();

          if (handler != null)
          {
            mapping = handler.handleMapping(context, blog, new String[0]);
          }
          else
          {
            mapping = "list.xhtml";
          }
        }

        if (mapping != null)
        {
          request.setViewId("/template/" + blog.getTemplate() + "/" + mapping);
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingHandler getDefaultMappingHander()
  {
    MappingHandler handler = null;

    if (defaultMappingReference == null)
    {
      defaultMappingReference =
        BlogContext.getInstance().getServiceRegistry().getServiceReference(
          Constants.SERVICE_DEFAULTMAPPING);
    }

    handler = (MappingHandler) defaultMappingReference.getImplementation();

    return handler;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MappingHandler> getMappingHandlers()
  {
    List<MappingHandler> mappingHandlers = null;

    if (mappingHandlerReference == null)
    {
      mappingHandlerReference =
        BlogContext.getInstance().getServiceRegistry().getServiceReference(
          Constants.SERVICE_MAPPING);
    }

    mappingHandlers = mappingHandlerReference.getImplementations();

    return mappingHandlers;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public PhaseId getPhaseId()
  {
    return PhaseId.RESTORE_VIEW;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param context
   */
  @SuppressWarnings("unchecked")
  private void applyMessages(FacesContext context)
  {
    Iterator<FacesMessage> messages =
      (Iterator<FacesMessage>) context.getExternalContext().getSessionMap().get(
          "sonia.blog.messages");

    if (messages != null)
    {
      while (messages.hasNext())
      {
        FacesMessage msg = messages.next();

        context.addMessage(null, msg);
      }
    }
    else
    {
      context.getExternalContext().getSessionMap().remove(
          "sonia.blog.messages");
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference defaultMappingReference;

  /** Field description */
  private ServiceReference mappingHandlerReference;
}
