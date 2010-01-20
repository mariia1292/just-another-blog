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



package sonia.util;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Sebastian Sdorra
 */
public class XmlUtil
{

  /** Field description */
  private static final String JAXP_SCHEMA_LANGUAGE =
    "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

  /** Field description */
  private static final String JAXP_SCHEMA_SOURCE =
    "http://java.sun.com/xml/jaxp/properties/schemaSource";

  /** Field description */
  private static final String W3C_SCHEMA_SOURCE =
    "http://www.w3.org/2001/XMLSchema";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   *
   * @return
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public static Document buildDocument(InputStream in)
          throws IOException, SAXException, ParserConfigurationException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    factory.setIgnoringComments(true);
    factory.setIgnoringElementContentWhitespace(true);

    DocumentBuilder builder = factory.newDocumentBuilder();

    return builder.parse(in);
  }

  /**
   * Method description
   *
   *
   * @param file
   *
   * @return
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public static Document buildDocument(File file)
          throws IOException, SAXException, ParserConfigurationException
  {
    Document doc = null;
    FileInputStream fis = null;

    try
    {
      fis = new FileInputStream(file);
      doc = buildDocument(fis);
    }
    finally
    {
      if (fis != null)
      {
        fis.close();
      }
    }

    return doc;
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param schema
   *
   * @return
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public static Document buildDocument(InputStream in, InputStream schema)
          throws IOException, SAXException, ParserConfigurationException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    factory.setNamespaceAware(true);
    factory.setValidating(true);
    factory.setIgnoringComments(true);
    factory.setIgnoringElementContentWhitespace(true);
    factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_SCHEMA_SOURCE);
    factory.setAttribute(JAXP_SCHEMA_SOURCE, schema);

    DocumentBuilder builder = factory.newDocumentBuilder();

    builder.setErrorHandler(new XmlErrorHandler());

    return builder.parse(in);
  }

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws ParserConfigurationException
   */
  public static Document createDocument() throws ParserConfigurationException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();

    return builder.newDocument();
  }

  /**
   * Method description
   *
   *
   * @param node
   * @param out
   *
   * @throws TransformerConfigurationException
   * @throws TransformerException
   */
  public static void writeDocument(Node node, OutputStream out)
          throws TransformerConfigurationException, TransformerException
  {
    writeDocument(node, out, false);
  }

  /**
   * Method description
   *
   *
   * @param node
   * @param out
   * @param prettyOutput
   *
   * @throws TransformerConfigurationException
   * @throws TransformerException
   */
  public static void writeDocument(Node node, OutputStream out,
                                   boolean prettyOutput)
          throws TransformerConfigurationException, TransformerException
  {
    TransformerFactory factory = TransformerFactory.newInstance();
    Transformer transformer = factory.newTransformer();

    if (prettyOutput)
    {
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    }

    transformer.transform(new DOMSource(node), new StreamResult(out));
  }

  /**
   * Method description
   *
   *
   * @param node
   * @param file
   *
   * @throws FileNotFoundException
   * @throws TransformerConfigurationException
   * @throws TransformerException
   */
  public static void writeDocument(Node node, File file)
          throws FileNotFoundException, TransformerConfigurationException,
                 TransformerException
  {
    writeDocument(node, new FileOutputStream(file));
  }

  /**
   * Method description
   *
   *
   * @param node
   * @param path
   *
   * @throws FileNotFoundException
   * @throws TransformerConfigurationException
   * @throws TransformerException
   */
  public static void writeDocument(Node node, String path)
          throws FileNotFoundException, TransformerConfigurationException,
                 TransformerException
  {
    writeDocument(node, new FileOutputStream(path));
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param node
   * @param name
   *
   * @return
   */
  public static String getAttributeValue(Node node, String name)
  {
    return getAttributeValue(node.getAttributes(), name);
  }

  /**
   * Method description
   *
   *
   * @param attributes
   * @param name
   *
   * @return
   */
  public static String getAttributeValue(NamedNodeMap attributes, String name)
  {
    String result = null;

    if (attributes != null)
    {
      Node node = attributes.getNamedItem(name);

      if (node != null)
      {
        result = node.getTextContent();
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param list
   *
   * @return
   */
  public static boolean hasContent(NodeList list)
  {
    return (list != null) && (list.getLength() > 0);
  }

  //~--- inner classes --------------------------------------------------------

  /**
   * Class description
   *
   *
   * @version    Enter version here..., 08/09/07
   * @author     Enter your name here...
   */
  private static class XmlErrorHandler implements ErrorHandler
  {

    /**
     * Method description
     *
     *
     * @param ex
     *
     * @throws SAXException
     */
    public void error(SAXParseException ex) throws SAXException
    {
      throw new SAXException(ex);
    }

    /**
     * Method description
     *
     *
     * @param ex
     *
     * @throws SAXException
     */
    public void fatalError(SAXParseException ex) throws SAXException
    {
      throw new SAXException(ex);
    }

    /**
     * Method description
     *
     *
     * @param ex
     *
     * @throws SAXException
     */
    public void warning(SAXParseException ex) throws SAXException
    {
      throw new SAXException(ex);
    }
  }
}
