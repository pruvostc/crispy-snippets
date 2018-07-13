package com.crispysnippets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class X509DecoderServlet extends HttpServlet {
  
  private static final Logger LOGGER = Logger.getLogger(X509DecoderServlet.class.getName());
  
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
  public void doGet(HttpServletRequest req, HttpServletResponse resp) {
    
    long startTime = System.currentTimeMillis(); // get the current time
    
    // Get the certificate value;
    String cryptedCert = req.getParameter("cert");
    LOGGER.log(Level.INFO, "Duration " + (System.currentTimeMillis() - startTime));
  }
}
