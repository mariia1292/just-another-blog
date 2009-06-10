/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.macro.Macro;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author sdorra
 */
public class DefaultMetaInformationProvider extends MetaInformationProvider
{

  /**
   * Constructs ...
   *
   */
  protected DefaultMetaInformationProvider() {}

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param macroClass
   * @param locale
   *
   * @return
   */
  @Override
  public MetaInformation getInformation(Class<? extends Macro> macroClass,
          Locale locale)
  {
    MetaInformation information = null;
    MacroInfo macro = macroClass.getAnnotation(MacroInfo.class);

    if (macro != null)
    {
      ResourceBundle bundle = null;
      String bundlePath = macro.resourceBundle();

      if (Util.hasContent(bundlePath))
      {
        bundle = ResourceBundle.getBundle(bundlePath, locale,
                                          macroClass.getClassLoader());
      }

      information = getMainInformation(bundle, macro);
      information.setParameter(getParameters(macroClass, bundle));
    }

    return information;
  }

  /**
   * Method description
   *
   *
   * @param bundle
   * @param macro
   *
   * @return
   */
  private MetaInformation getMainInformation(ResourceBundle bundle,
          MacroInfo macro)
  {
    String name = getString(bundle, macro.value());
    String description = getString(bundle, macro.description());
    String icon = getString(bundle, macro.icon());

    return new MetaInformation(name, description, icon);
  }

  /**
   * Method description
   *
   *
   * @param methodName
   *
   * @return
   */
  private String getParamName(String methodName)
  {
    String name = null;

    if ((methodName.length() > 3) && methodName.startsWith("set"))
    {
      char c = methodName.charAt(3);

      name = Character.toLowerCase(c) + methodName.substring(4);
    }

    return name;
  }

  /**
   * Method description
   *
   *
   * @param macroClass
   * @param bundle
   *
   * @return
   */
  private List<MetaInformationParameter> getParameters(
          Class<? extends Macro> macroClass, ResourceBundle bundle)
  {
    List<MetaInformationParameter> parameters =
      new ArrayList<MetaInformationParameter>();
    Method[] methods = macroClass.getDeclaredMethods();

    for (Method method : methods)
    {
      MacroInfoParameter param = method.getAnnotation(MacroInfoParameter.class);

      if (param != null)
      {
        String name = getParamName(method.getName());
        String label = getString(bundle, param.value());
        String description = getString(bundle, param.description());

        parameters.add(new MetaInformationParameter(name, label, description));
      }
    }

    return parameters;
  }

  /**
   * Method description
   *
   *
   * @param bundle
   * @param value
   *
   * @return
   */
  private String getString(ResourceBundle bundle, String value)
  {
    String result = null;

    if (Util.hasContent(value))
    {
      if (bundle != null)
      {
        result = bundle.getString(value);
      }
      else
      {
        result = value;
      }
    }

    return result;
  }
}
