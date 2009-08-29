/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.script;

/**
 *
 * @author Sebastian Sdorra
 */
public interface ScriptConstants
{

  /** Field description */
  public static final String ACTION_EXCECUTE = "excecute";

  /** Field description */
  public static final String ACTION_LIST = "list";

  /** Field description */
  public static final String ACTION_REMOVE = "remove";

  /** Field description */
  public static final String ACTION_STORE = "store";

  /** Field description */
  public static final String ACTION_VIEW = "view";

  /** Field description */
  public static final String DIRECTORY = "scripts";

  /** Field description */
  public static final String METAPROPERTIES_AUTHOR = "author";

  /** Field description */
  public static final String METAPROPERTIES_CONTENT = "content";

  /** Field description */
  public static final String METAPROPERTIES_DESCRIPTION = "description";

  /** Field description */
  public static final String METAPROPERTIES_SCRIPT = "script";

  /** Field description */
  public static final String METAPROPERTIES_TITLE = "title";

  /** Field description */
  public static final String PARAMETER_ACTION = "action";

  /** Field description */
  public static final String PARAMETER_SCRIPT = "script";

  /** Field description */
  public static final String[] SAMPLES = { "/sonia/blog/script/samples/list-blogs.xml",
          "/sonia/blog/script/samples/spam-report.xml" };
}
