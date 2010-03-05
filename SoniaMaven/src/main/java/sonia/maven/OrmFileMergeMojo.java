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



package sonia.maven;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @goal mergeorm
 * @author Sebastian Sdorra
 */
public class OrmFileMergeMojo extends AbstractMojo
{

  /**
   * Method description
   *
   *
   * @throws MojoExecutionException
   * @throws MojoFailureException
   */
  public void execute() throws MojoExecutionException, MojoFailureException
  {
    List<File> files = MavenUtil.getFiles(ormBasePath, ormIncludes, null);

    if ((files != null) &&!files.isEmpty())
    {
      Document doc = null;

      for (File file : files)
      {
        if (doc == null)
        {
          doc = createBaseDocument(file);
        }
        else
        {
          appendFile(doc, file);
        }
      }

      writeDocument(doc);
    }
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param ormBasePath
   */
  public void setOrmBasePath(File ormBasePath)
  {
    this.ormBasePath = ormBasePath;
  }

  /**
   * Method description
   *
   *
   * @param ormIncludes
   */
  public void setOrmIncludes(String[] ormIncludes)
  {
    this.ormIncludes = ormIncludes;
  }

  /**
   * Method description
   *
   *
   * @param ormOutput
   */
  public void setOrmOutput(File ormOutput)
  {
    this.ormOutput = ormOutput;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param doc
   * @param file
   *
   * @throws MojoFailureException
   */
  private void appendFile(Document doc, File file) throws MojoFailureException
  {
    try
    {
      Document ormDoc = getDocumentBuilder().parse(file);
      NodeList list = ormDoc.getElementsByTagName("entity");

      for (int i = 0; i < list.getLength(); i++)
      {
        Node node = doc.importNode(list.item(i), true);

        doc.getDocumentElement().appendChild(node);
      }
    }
    catch (Exception ex)
    {
      getLog().error(ex);

      throw new MojoFailureException(ex.getMessage());
    }
  }

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   *
   * @throws MojoFailureException
   */
  private Document createBaseDocument(File file) throws MojoFailureException
  {
    Document document = null;

    try
    {
      document = getDocumentBuilder().parse(file);
    }
    catch (Exception ex)
    {
      getLog().error(ex);

      throw new MojoFailureException(ex.getMessage());
    }

    return document;
  }

  /**
   * Method description
   *
   *
   * @param doc
   *
   * @throws MojoFailureException
   */
  private void writeDocument(Node doc) throws MojoFailureException
  {
    File parent = ormOutput.getParentFile();

    if (!parent.exists())
    {
      if (!parent.mkdirs())
      {
        throw new MojoFailureException("could not create parent directory");
      }
    }

    try
    {
      Transformer transformer =
        TransformerFactory.newInstance().newTransformer();

      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.transform(new DOMSource(doc), new StreamResult(ormOutput));
    }
    catch (Exception ex)
    {
      getLog().error(ex);

      throw new MojoFailureException(ex.getMessage());
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws MojoFailureException
   */
  private DocumentBuilder getDocumentBuilder() throws MojoFailureException
  {
    if (documentBuilder == null)
    {
      try
      {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        documentBuilder = factory.newDocumentBuilder();
      }
      catch (Exception ex)
      {
        getLog().error(ex);

        throw new MojoFailureException(ex.getMessage());
      }
    }

    return documentBuilder;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private DocumentBuilder documentBuilder;

  /**
   *
   * @parameter
   */
  private File ormBasePath;

  /**
   *
   * @parameter
   */
  private String[] ormIncludes;

  /**
   *
   * @parameter
   */
  private File ormOutput;
}
