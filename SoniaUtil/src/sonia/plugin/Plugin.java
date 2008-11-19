/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.plugin;

/**
 *
 * @author sdorra
 */
public class Plugin
{

  /** Field description */
  public static final int STATE_REGISTERED = 0;

  /** Field description */
  public static final int STATE_STARTED = 1;

  /** Field description */
  public static final int STATE_STOPED = 2;

  /** Field description */
  public static final int STATE_UNREGISTERED = 3;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public Plugin()
  {
    this.state = STATE_UNREGISTERED;
    this.autostart = true;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Activator getActivator()
  {
    return activator;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayName()
  {
    return displayName;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getPath()
  {
    return path;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getState()
  {
    return state;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getVendor()
  {
    return vendor;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getVersion()
  {
    return version;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isAutostart()
  {
    return autostart;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param activator
   */
  public void setActivator(Activator activator)
  {
    this.activator = activator;
  }

  /**
   * Method description
   *
   *
   * @param autostart
   */
  public void setAutostart(boolean autostart)
  {
    this.autostart = autostart;
  }

  /**
   * Method description
   *
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Method description
   *
   *
   * @param displayName
   */
  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Method description
   *
   *
   * @param path
   */
  public void setPath(String path)
  {
    this.path = path;
  }

  /**
   * Method description
   *
   *
   * @param state
   */
  public void setState(int state)
  {
    this.state = state;
  }

  /**
   * Method description
   *
   *
   * @param vendor
   */
  public void setVendor(String vendor)
  {
    this.vendor = vendor;
  }

  /**
   * Method description
   *
   *
   * @param version
   */
  public void setVersion(String version)
  {
    this.version = version;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Activator activator;

  /** Field description */
  private boolean autostart;

  /** Field description */
  private String description;

  /** Field description */
  private String displayName;

  /** Field description */
  private String name;

  /** Field description */
  private String path;

  /** Field description */
  private int state;

  /** Field description */
  private String vendor;

  /** Field description */
  private String version;
}
