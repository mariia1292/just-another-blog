/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.listener;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;

import sonia.plugin.ServiceReference;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

/**
 *
 * @author sdorra
 */
public class AttachmentListener extends EntityListener
{

  /**
   * Method description
   *
   *
   * @param object
   */
  @Override @PostLoad
  public void postLoad(Object object)
  {
    for (EntityListener listener : getListeners())
    {
      listener.postLoad(object);
    }
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  @Override @PostPersist
  public void postPersists(Object object)
  {
    for (EntityListener listener : getListeners())
    {
      listener.postPersists(object);
    }
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  @Override @PostRemove
  public void postRemove(Object object)
  {
    for (EntityListener listener : getListeners())
    {
      listener.postRemove(object);
    }
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  @Override @PostUpdate
  public void postUpdate(Object object)
  {
    for (EntityListener listener : getListeners())
    {
      listener.postUpdate(object);
    }
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  @Override @PrePersist
  public void prePersists(Object object)
  {
    for (EntityListener listener : getListeners())
    {
      listener.prePersists(object);
    }
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  @Override @PreRemove
  public void preRemove(Object object)
  {
    for (EntityListener listener : getListeners())
    {
      listener.preRemove(object);
    }
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  @Override @PreUpdate
  public void preUpdate(Object object)
  {
    for (EntityListener listener : getListeners())
    {
      listener.preUpdate(object);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @SuppressWarnings("unchecked")
  private List<EntityListener> getListeners()
  {
    if (reference == null)
    {
      reference =
        BlogContext.getInstance().getServiceRegistry().getServiceReference(
          Constants.SERVICE_BLOG_LISTENER);
    }

    return reference.getImplementations();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ServiceReference reference;
}
