package com.crispysnippets;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crispysnippets.security.HashGenerator;
import com.crispysnippets.utils.DateTime;
import com.crispysnippets.utils.HttpConnector;



/** This servlet is used to download data feed from given source
 * and is protected by a digital signature so that only trusted parties
 * can use it.
 * Servlet implementation class DataFeedServlet
 */
public class DataFeedServlet extends HttpServlet {
  
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(DataFeedServlet.class.getName());
  private static final String DEFAULT_ENCODING = "UTF-8";
  private static final String DEFAULT_SIGALGO = "SHA-256";
  private static final String ACCESSREFUSED = 
            "<html><body>Access to this feature is restricted, sorry...</body></html>";
  
  /** Constructor.
   * @see HttpServlet#HttpServlet()
   */
  public DataFeedServlet() {
    super();
    // Auto-generated constructor stub
  }
     
  /** doGet Method to support HTTP GET Requests.
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws ServletException, IOException {
    String src = request.getParameter("src"); // target URL
    System.out.println("src = " + src);
    String sig = request.getParameter("sig"); // hash provided
    System.out.println("sig = " + sig);
    String secretToHide = "30268606F8B95F76B300D27630AFAC4E";
    String resp = ""; // response returned.
    boolean valid = true;
    
    // Extract creationTime and convert it to a long for comparison with now
    String stamp = request.getParameter("s");
    System.out.println("received timestamp: " + stamp);
    long creationTime = 0;
    try {
      creationTime = Long.parseLong(stamp);
    } catch (NumberFormatException nfe) {
      LOGGER.log(Level.WARNING,"NumberFormatException: " + nfe.getMessage());
      valid = false;
    }
    // compare the time of creation with now - invalidate if expired
    long now = DateTime.getUtcTimeMilliseconds();
    System.out.println("UTC Stamp now: " + now);
    if ((now - creationTime) > 300000) {
      // invalidate the request
      valid = false;
    }
    
    // check the validity of the signature - invalidate if incorrect
    if (valid) {
      String msg = "src=" + URLEncoder.encode(src, StandardCharsets.UTF_8.toString()) + "&s=" + stamp + secretToHide;
    
      // regenerate MessageDigest to check validity
      try {
        String calculatedHash = HashGenerator.hashString(msg, DEFAULT_SIGALGO);
        LOGGER.log(Level.INFO,"original:" + msg);
        LOGGER.log(Level.INFO,"digested(hex):" + calculatedHash);
        LOGGER.log(Level.INFO,"provided(hex):" + sig);
        if (!sig.equals(calculatedHash)) {
          valid = false; // invalidate request if signature is different
        }
      } catch (Exception ex) {
        LOGGER.log(Level.WARNING,"Failed to generate hash and compare with original: " 
            + ex.getMessage());
        valid = false;
      }
    }
    
    // if all is good, go an fetch the content
    if (valid) {
      
      // the submitted param is used when <Carriage Return> was used to submit the form
      if ((src != null) && !("").equals(src)) {
        URLDecoder.decode(src, DEFAULT_ENCODING);
        resp = HttpConnector.httpGet(src);
      }

      // Auto-generated method stub
      response.getWriter().append(resp);
      
    // Response is HTTP 403 when the request isn't valid
    } else {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.setContentType("text/html; charset=" + DEFAULT_ENCODING);
      response.setCharacterEncoding(DEFAULT_ENCODING);
      response.getWriter().append(ACCESSREFUSED);
    }
    
  }

  /** doGet Method to support HTTP GET Requests.
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Auto-generated method stub
    doGet(request, response);
  }

}
