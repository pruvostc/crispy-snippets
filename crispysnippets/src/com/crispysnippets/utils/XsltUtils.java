package com.crispysnippets.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XsltUtils {

  private static final Logger LOGGER = Logger.getLogger(XsltUtils.class.getName());
  
  /**
   * Performs the XSLT transformation using Files in input.
   * @param xslUrl The XSL File in input for the transformation
   * @param xmlUrl The XML File in input for the transformation
   * @return String the result of the transformation as a String
   */
  public static String xsltTransform(URL xslUrl, URL xmlUrl) {
    StreamSource xslsource = null;
    StreamSource xmlsource = null;
    try {
      xslsource = new StreamSource(xslUrl.openStream());
      xmlsource = new StreamSource(xmlUrl.openStream());
    } catch (IOException ioe) {
      LOGGER.log(Level.SEVERE, ioe.getMessage());
    } catch (Exception er) {
      LOGGER.log(Level.SEVERE,er.getMessage());
    }
    
    return XsltUtils.xsltTransform(xslsource, xmlsource);
  }
  
  /**
   * Performs the XSLT transformation using Files in input.
   * @param xslfile The XSL File in input for the transformation
   * @param xmlfile The XML File in input for the transformation
   * @return String the result of the transformation as a String
   */
  public static String xsltTransform(File xslfile, File xmlfile) {
    
    StreamSource xslsource = new StreamSource(xslfile);
    StreamSource xmlsource = new StreamSource(xmlfile);
    
    return XsltUtils.xsltTransform(xslsource, xmlsource);
  }

  /**
   * Performs the XSLT transformation using StreamSources in input.
   * @param xslStream The XSL stream in input for the transformation
   * @param xmlStream The XML stream in input for the transformation
   * @return String the result of the transformation as a String
   */
  public static String xsltTransform(StreamSource xslStream, StreamSource xmlStream) {
    
    StringWriter sw = new StringWriter();
    
    /* This is the preferred way to transform, but not working in the Google App Engine.
    try {
      // Load the XSL into the transformer for the transformation to proceed
      Transformer transformer = TransformerFactory.newInstance().newTransformer(xslStream);

      // Transform the document and store it in a String sw
      transformer.transform(xmlStream, new StreamResult(sw));

    } catch (TransformerException er) {
      LOGGER.log(Level.SEVERE,er.getMessage());
    }
     */
    
    try {
      /* Load the XSL into the transformer for the transformation to proceed
       * Note: I am using Xalan 2.7.1 (xalan.jar, XercesImpl.jar, xml-apis.jar, serializer.jar)
       * This is an unwanted fix for the Error thrown by the Google App Engine
       * java.lang.NoClassDefFoundError: 
       * com.sun.org.apache.xalan.internal.xsltc.dom.AbsoluteIterator 
       * is a restricted class. Please see the Google App Engine developer's guide for more details.
       * Ref. https://code.google.com/p/googleappengine/issues/detail?id=1452
       */
      javax.xml.transform.TransformerFactory xformFactory =
          javax.xml.transform.TransformerFactory.newInstance();
      //javax.xml.transform.TransformerFactory xformFactory =
      //    javax.xml.transform.TransformerFactory.newInstance(
      //        "org.apache.xalan.processor.TransformerFactoryImpl",
      //        Thread.currentThread().getContextClassLoader()); 
      
      Transformer transformer = xformFactory.newTransformer(xslStream);
      
      // Transform the document and store it in a String sw
      transformer.transform(xmlStream, new StreamResult(sw));
    } catch (Exception er) {
      LOGGER.log(Level.SEVERE,er.getMessage());
    }
    
    return sw.toString();
  }

}
