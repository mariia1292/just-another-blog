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
 * @author Sebastian Sdorra
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