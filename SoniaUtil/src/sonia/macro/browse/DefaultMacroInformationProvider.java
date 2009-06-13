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
public class DefaultMacroInformationProvider extends MacroInformationProvider
{

  /**
   * Constructs ...
   *
   */
  protected DefaultMacroInformationProvider() {}

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
  public MacroInformation getInformation(Class<? extends Macro> macroClass,
          Locale locale)
  {
    MacroInformation information = null;
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
  private MacroInformation getMainInformation(ResourceBundle bundle,
          MacroInfo macro)
  {
    String name = macro.name();
    String displayName = getString(bundle, macro.displayName());
    String description = getString(bundle, macro.description());
    String icon = getString(bundle, macro.icon());
    MacroInformation info = new MacroInformation(name, displayName,
                              description, icon);
    Class<? extends MacroWidget> widget = macro.bodyWidget();

    if (widget != null)
    {
      info.setBodyWidget(widget);

      String param = macro.widgetParam();

      if (Util.hasContent(param))
      {
        info.setWidgetParam(param);
      }
    }

    return info;
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
  private List<MacroInformationParameter> getParameters(
          Class<? extends Macro> macroClass, ResourceBundle bundle)
  {
    List<MacroInformationParameter> parameters =
      new ArrayList<MacroInformationParameter>();
    Method[] methods = macroClass.getDeclaredMethods();

    for (Method method : methods)
    {
      MacroInfoParameter param = method.getAnnotation(MacroInfoParameter.class);

      if (param != null)
      {
        String name = getParamName(method.getName());
        String label = getString(bundle, param.displayName());
        String description = getString(bundle, param.description());
        MacroInformationParameter infoParam =
          new MacroInformationParameter(name, label, description);
        Class<? extends MacroWidget> widget = param.widget();

        if (widget != null)
        {
          infoParam.setWidget(widget);

          String widgetParam = param.widgetParam();

          if (Util.hasContent(widgetParam))
          {
            infoParam.setWidgetParam(widgetParam);
          }
        }

        parameters.add(infoParam);
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
