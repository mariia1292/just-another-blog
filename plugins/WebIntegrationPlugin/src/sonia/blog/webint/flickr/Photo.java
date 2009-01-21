/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.webint.flickr;

//~--- non-JDK imports --------------------------------------------------------

import org.xml.sax.Attributes;

//~--- JDK imports ------------------------------------------------------------

import java.text.MessageFormat;

/**
 *
 * @author sdorra
 */
public class Photo
{

  /**
   * Method description
   *
   *
   *
   * @param attributes
   *
   * @return
   */
  public static Photo fromXml(Attributes attributes)
  {
    Photo photo = new Photo();
    String idString = attributes.getValue("id");

    photo.setId(Long.parseLong(idString));

    String owner = attributes.getValue("owner");

    photo.setOwner(owner);

    String secret = attributes.getValue("secret");

    photo.setSecret(secret);

    String serverString = attributes.getValue("server");

    photo.setServer(Long.parseLong(serverString));

    String farmString = attributes.getValue("farm");

    photo.setFarm(Long.parseLong(farmString));

    String title = attributes.getValue("title");

    photo.setTitle(title);

    return photo;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  public String toString()
  {
    return getUrl() + " : " + title;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public long getFarm()
  {
    return farm;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getId()
  {
    return id;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getOwner()
  {
    return owner;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getSecret()
  {
    return secret;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getServer()
  {
    return server;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getThumbnailUrl()
  {
    return MessageFormat.format(FlickrAPI.PHOTO_URL, String.valueOf(farm),
                                String.valueOf(server), String.valueOf(id),
                                secret, FlickrAPI.SIZE_SMALL);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getUrl()
  {
    return MessageFormat.format(FlickrAPI.PHOTO_URL, String.valueOf(farm),
                                String.valueOf(server), String.valueOf(id),
                                secret, FlickrAPI.SIZE_ORGINAL);
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param farm
   */
  public void setFarm(long farm)
  {
    this.farm = farm;
  }

  /**
   * Method description
   *
   *
   * @param id
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Method description
   *
   *
   * @param owner
   */
  public void setOwner(String owner)
  {
    this.owner = owner;
  }

  /**
   * Method description
   *
   *
   * @param secret
   */
  public void setSecret(String secret)
  {
    this.secret = secret;
  }

  /**
   * Method description
   *
   *
   * @param server
   */
  public void setServer(long server)
  {
    this.server = server;
  }

  /**
   * Method description
   *
   *
   * @param title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private long farm;

  /** Field description */
  private long id;

  /** Field description */
  private String owner;

  /** Field description */
  private String secret;

  /** Field description */
  private long server;

  /** Field description */
  private String title;
}
