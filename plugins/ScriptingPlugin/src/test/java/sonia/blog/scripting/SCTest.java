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



package sonia.blog.scripting;

//~--- non-JDK imports --------------------------------------------------------

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.script.ScriptEngine;
import org.junit.Test;

//~--- JDK imports ------------------------------------------------------------



import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Sebastian Sdorra
 */
public class SCTest
{

  /**
   * Method description
   *
   *
   * @throws ScriptException
   */
  @Test
  public void scriptTest() throws ScriptException
  {
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("Groovy");

    Map<String,String> parameter = new HashMap<String, String>();
    parameter.put("name", "What");

    FreemarkerScriptTemplate template = new FreemarkerScriptTemplate( "<h3>say ${name}</h3>" );

    engine.put("scTemplate", template);
    engine.put("parameter", parameter);

    StringBuffer script = new StringBuffer();
    script.append("def name = parameter.get('name');\n");
    script.append("if ( name == null ){\n");
    script.append("  name = 'Wooooo';\n");
    script.append("}\n");
    script.append("scTemplate.put('name', name);\n");
    script.append("println scTemplate.render();");

    System.out.println(script.toString());


    StringWriter writer = new StringWriter();
    engine.getContext().setWriter(writer);
    engine.eval( script.toString() );
    System.out.println(writer);
    
  }
}
