package com.crispysnippets.utils;

import com.crispysnippets.error.ParserErrorHandler;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class DocumentUtils {

  /**
   * Returns a DOM Object from parsing the XML in input.
   * @param isource The InputSource Handler to the XML data
   * @param nameSpaceAware Specify if the parser should be aware of Name spaces
   * @param validating Should we validate the XML while parsing
   * @param eh Error Handler
   * @return Document object
   * @throws ParserConfigurationException Parsing Error Exception
   * @throws IOException IO Error
   * @throws SAXException SAX Exception
   */
  public static Document createDocument(
      InputSource isource, boolean nameSpaceAware, boolean validating, ErrorHandler eh)
          throws ParserConfigurationException, IOException, SAXException {
    
    //Get Document Builder Factory
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    
    // ignore white spaces
    docFactory.setIgnoringElementContentWhitespace(true);
    
    // Turn on validation
    docFactory.setValidating(validating);
    
    // Turn on namespaces
    //docFactory.setNamespaceAware(nameSpaceAware); // Means xmlns="..." value will be required
    
    // Create document builder
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

    docBuilder.setErrorHandler(eh);

    // Parse the passed in XML  
    return docBuilder.parse(isource);
  }
  
  /** 
   * This method creates a Document based on a string, and
   * allows the parsing to be Name space aware or not, as well
   * as imposing validation or not.
   * @param xmlString the XML as a string
   * @param nameSpaceAware true or false
   * @param validating true or false
   * @return the created document
   * @throws ParserConfigurationException Parser Configuration Exception.
   * @throws IOException IO Exception.
   * @throws SAXException SAX Exception.
   */
  public static Document createDocument(
      String xmlString, boolean nameSpaceAware, boolean validating)
          throws ParserConfigurationException, IOException, SAXException {
    
    return createDocument(new InputSource(new StringReader(xmlString)), 
      nameSpaceAware, validating, new ParserErrorHandler(System.out));
  }
  
  /** 
   * This method creates a Document based on a string.
   * @param xmlString the xml as a string.
   * @return the created document
   * @throws ParserConfigurationException Parser Configuration Exception.
   * @throws IOException IO Exception.
   * @throws SAXException SAX Exception.
   */
  public static Document createDocument(String xmlString)
      throws ParserConfigurationException, IOException, SAXException {
    
    return createDocument(xmlString, true, false);
  }
}
