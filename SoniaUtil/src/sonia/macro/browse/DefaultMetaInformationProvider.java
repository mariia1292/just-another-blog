/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sonia.macro.browse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import sonia.macro.Macro;
import sonia.util.Util;

/**
 *
 * @author sdorra
 */
public class DefaultMetaInformationProvider extends MetaInformationProvider {

  protected DefaultMetaInformationProvider()
  {
  }





  @Override
  public MetaInformation getInformation(Class<? extends Macro> macroClass, Locale locale)
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
    Field[] fields = macroClass.getDeclaredFields();

    for (Field field : fields)
    {
      MacroInfoParameter param = field.getAnnotation(MacroInfoParameter.class);

      if (param != null)
      {
        String name = field.getName();
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
