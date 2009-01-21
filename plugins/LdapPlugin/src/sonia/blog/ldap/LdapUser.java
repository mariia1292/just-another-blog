/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.ldap;

/**
 *
 * @author sdorra
 */
public class LdapUser
{

  /**
   * Constructs ...
   *
   *
   * @param dn
   * @param name
   * @param displayName
   * @param mail
   */
  public LdapUser(String dn, String name, String displayName, String mail)
  {
    this.dn = dn;
    this.name = name;
    this.displayName = displayName;
    this.mail = mail;
  }

  //~--- get methods ----------------------------------------------------------

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
  public String getDn()
  {
    return dn;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getMail()
  {
    return mail;
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

  //~--- set methods ----------------------------------------------------------

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
   * @param dn
   */
  public void setDn(String dn)
  {
    this.dn = dn;
  }

  /**
   * Method description
   *
   *
   * @param mail
   */
  public void setMail(String mail)
  {
    this.mail = mail;
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String displayName;

  /** Field description */
  private String dn;

  /** Field description */
  private String mail;

  /** Field description */
  private String name;
}
