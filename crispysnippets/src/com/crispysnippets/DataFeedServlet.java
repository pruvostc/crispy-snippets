package com.crispysnippets;

import com.crispysnippets.security.HashGenerator;
import com.crispysnippets.utils.DateTime;
import com.crispysnippets.utils.HttpConnector;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/** This servlet is used to download data feed from given source
 * and is protected by a digital signature so that only trusted parties
 * can use it.
 * Servlet implementation class DataFeedServlet
 */
public class DataFeedServlet extends HttpServlet {
  
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(DataFeedServlet.class.getName());  
  
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
    String sig = request.getParameter("sig"); // hash provided
    String secretToHide = "30268606F8B95F76B300D27630AFAC4E";
    String resp = ""; // response returned.
    boolean valid = true;
    
    // Extract creationTime and convert it to a long for comparison with now
    String stamp = request.getParameter("s");
    long creationTime = 0;
    try {
      creationTime = Long.parseLong(stamp);
    } catch (NumberFormatException nfe) {
      LOGGER.log(Level.SEVERE,"NumberFormatException: " + nfe.getMessage());
    }
    // compare the time of creation with now
    long now = DateTime.getUtcTimeMilliseconds();
    if ((now - creationTime) > 300000) {
      // invalidate the request
      valid = false;
    }
    
    if (valid) {
      String msg = "src=" + src + "&s=" + stamp + secretToHide;
    
      // regenerate MessageDigest to check validity
      try {
        String calculatedHash = HashGenerator.hashString(msg, "SHA-256");
        LOGGER.log(Level.INFO,"original:" + msg);
        LOGGER.log(Level.INFO,"digested(hex):" + calculatedHash);
        LOGGER.log(Level.INFO,"provided(hex):" + sig);
        if (!sig.equals(calculatedHash)) {
          valid = false; // invalidate request if signature is different
        }
      } catch (Exception ex) {
        LOGGER.log(Level.SEVERE,"Failed to generate hash and compare with original: " 
            + ex.getMessage());
      }
    }
    
    
    if (valid) {
      
      // the submitted param is used when <Carriage Return> was used to submit the form
      if ((src != null) && !("").equals(src)) {
        URLDecoder.decode(src, "UTF-8");
        resp = HttpConnector.httpGet(src);
      }

      // Auto-generated method stub
      response.getWriter().append(resp);
      
    } else {
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().append("Access to this feature is restricted, Access Refused!");
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
