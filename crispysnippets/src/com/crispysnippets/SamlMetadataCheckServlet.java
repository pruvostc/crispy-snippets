package com.crispysnippets;

import com.crispysnippets.utils.XsltUtils;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
/***
 * 
 * @author PruvostC
 *
 */
public class SamlMetadataCheckServlet extends HttpServlet {
  
  private static final Logger LOGGER = Logger.getLogger(SamlMetadataCheckServlet.class.getName());

  //private static final String metadataXml = "http://www.rediris.es/sir/shib1metadata.xml";
  //private static final String metadataXml = "http://metadata.ukfederation.org.uk/ukfederation-metadata.xml";
  //private static String metadataXml = "http://localhost:8888/ukfederation-metadata-saved.xml";
  private static String metadataXml = "http://localhost:8888/ukfederation-metadata-short.xml";
  
  //defines the type of output that we want to generate
  private String viewType = "basic"; // ('basic' = default viewType)
  private String styleSheet = ""; // defined later depending on the viewType
  
  /**
   * Processes the POST requests received via this Servlet.
   */
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    doGet(req, resp);
  }
  
  /**
   * Processes the GET requests received via this Servlet.
   */
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    
    String ctxUrl = req.getRequestURL().toString();
    String ctxUri = req.getRequestURI().toString();
    String baseUrl = ctxUrl.substring(0, ctxUrl.indexOf(ctxUri));
    
    LOGGER.log(Level.INFO,"Server Context (Base URL): " + baseUrl);
    
    String metadata = req.getParameter("metadata");
    String view = req.getParameter("view");
    
    // the submitted param is used when <Carriage Return> was used to submit the form
    if ((metadata != null) && !("").equals(metadata)) {
      URLDecoder.decode(metadata, "UTF-8");
      metadataXml = metadata;
      //LOGGER.log(Level.INFO,"Incoming: " + metadataXml);
      if ((metadataXml.indexOf("ukfederation") > 0) && (baseUrl.indexOf("localhost") > 0)) {
        metadataXml = baseUrl + "/ukfederation-metadata-short.xml";
        //metadataXml = baseUrl + "/ukfederation-metadata-saved.xml";
        //LOGGER.log(Level.INFO,"Using: " + metadataXml);
      }
    }
    if ((view != null) && !("").equals(view)) {
      viewType = view;
    }
    
    resp.setContentType("text/html; charset=utf-8");

    
    final long startTime = System.currentTimeMillis(); // get the current time
    
    //String response = HttpConnector.httpGet(metadataXml);
    //String xmlns = "urn:oasis:names:tc:SAML:2.0:metadata";
        
    // select the type of output ('basic' or 'stats')
    if (viewType.equals("stats")) {
      styleSheet = "extensionsStats.xsl"; // 'stats' view
    } else {
      viewType = "basic";
      styleSheet = "xsltest.xsl"; // 'basic' view (default)
    }
    
    String result = "";
    try {
      result = XsltUtils.xsltTransform(new URL(baseUrl + "/" + styleSheet), new URL(metadataXml));
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, "Failed to transform" + metadataXml + " with "
          + baseUrl + "/" + styleSheet + "\n" + ex.getMessage());
    }
    resp.getWriter().println(result);
    long timer1 = System.currentTimeMillis(); // get the time again
    LOGGER.log(Level.INFO,"Took this much time to run:" + (timer1 - startTime));
  }
}
