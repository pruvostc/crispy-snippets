package com.crispysnippets.utils;

//import java.util.logging.Logger;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* see also https://docs.oracle.com/javase/7/docs/api/javax/xml/xpath/package-summary.html
*
* Summary:
* Location Path - Description
* 
* /foo/bar/@id - Selects the attribute id of the <bar> element
* /foo/bar/text() - Selects the text nodes of the <bar> element. 
*                   No distinction is made between escaped and non-escaped character data.
* /foo/bar/comment() - Selects all comment nodes contained in the <bar> element.
* /foo/bar/processing-instruction() - Selects all processing-instruction nodes 
*                                     contained in the <bar> element.
*/

public class XpathUtils {

  //  private static final Logger LOGGER = Logger.getLogger(XpathUtils.class.getName());
  
  /** Performs the actual implementation of the XPath Search.
   * @param xpathExpression the path to search with
   * @param doc the document to search
   * @return the matching node list
   * @throws XPathExpressionException Xpath Expression Exception.
   */
  public static NodeList doXPathSearch(String xpathExpression, Document doc)
      throws XPathExpressionException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    XPathExpression expr = xpath.compile(xpathExpression);
    NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    return nodes;
  }

  /**
   * Performs XPath Search to return a single node.
   * @param xpathExpression the path to search with
   * @param doc the document to search
   * @return the matching node
   * @throws XPathExpressionException XPath Expression Exception.
   */
  public static Node doSingleXPathSearch(String xpathExpression, Document doc)
      throws XPathExpressionException {
    XPath xpath = XPathFactory.newInstance().newXPath();
    XPathExpression expr = xpath.compile(xpathExpression);
    Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
    return node;
  }
  
  /**
   * Method doSingleXPathSearchValue, A wrapper class to select a piece of 
   * xml specified by the path.
   * @param doc the document to select the information from
   * @param xpathExpression the Xpath to select information
   * @return the selected text from the xml
   * @throws XPathExpressionException Transformer Exception. 
   */
  public static String doSingleXPathSearchValue(String xpathExpression, Document doc)
      throws XPathExpressionException {
    
    Node node = XpathUtils.doSingleXPathSearch(xpathExpression, doc);
    return (node != null) ? node.getNodeValue() : "";

  }
}

/**
 * 
 * 
try {
      //response = FileUtils.fileToString("demoFile.xml");

      
      Document responseDocument = DocumentUtils.createDocument(response);

      NodeList nodes = XpathUtils.doXPathSearch(
          "//EntityDescriptor/Organization/OrganizationName/text()", responseDocument);
      NodeList nodes2 = XpathUtils.doXPathSearch("//EntityDescriptor/@ID", responseDocument);
      
      for (int i = 0; i < nodes.getLength(); i++) {
        if (null != nodes2.item(i)) {
          resp.getWriter().println(nodes2.item(i).getNodeValue() + "-> " 
              + nodes.item(i).getNodeValue());
        } else {
          resp.getWriter().println(nodes.item(i).getNodeValue());
        }
      }
      System.out.println("\nDOM object parsed properly...");
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, ex.getMessage());
    }
*/
