package com.crispysnippets.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpConnector {

  private static final Logger LOGGER = Logger.getLogger(HttpConnector.class.getName());
  private static final String USER_AGENT = "CripySnippets Agent";
  private static final String DEFAULT_ENCODING = "UTF-8";

  /**
   * HTTP GET Method.
   * @param urlString URL to access via the GET Method
   * @return String
   */
  public static String httpGet(String urlString) {

    InputStream input = null;
    HttpURLConnection con = null;
    StringBuffer response = new StringBuffer("");
    try {
      URL url = new URL(urlString);
      con = (HttpURLConnection) url.openConnection();
      con.setReadTimeout(150000); //150s max
      
      // By default it is GET request
      con.setRequestMethod("GET");
      // add request headers
      //con.setRequestProperty("Connection", "keep-alive");
      con.setRequestProperty("Charset", DEFAULT_ENCODING);
      con.setRequestProperty("Connection", "close");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
      //con.setDoInput(false);
      //con.setUseCaches(true);
      
      con.connect();
      //System.out.print(">>>>>>>>> Content-Type: " + con.getContentType());
      //String contentEncoding = con.getContentEncoding();
      //System.out.print(">>>>>>>>> contentEncoding: " + contentEncoding);
      int responseCode = con.getResponseCode();
      //System.out.println("GET request did not worked-> responseCode: " + responseCode);
      if (responseCode == HttpURLConnection.HTTP_OK) { // success and read it all
        LOGGER.log(Level.WARNING, "content-Type: " + con.getContentType());
        
        input = con.getInputStream();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(input, DEFAULT_ENCODING));
        
        
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();

        // print result
        // LOGGER.log(Level.FINE,response.toString());
      } else {
        LOGGER.log(Level.WARNING,"GET request did not worked-> responseCode: " + responseCode);
      }
    } catch (MalformedURLException mlExcept) {
      LOGGER.log(Level.SEVERE, "MalformedURLException..." + mlExcept.getMessage());
    } catch (UnknownHostException uhExcept) {
      LOGGER.log(Level.SEVERE, "UnknownHostException..." + uhExcept.getMessage());
    } catch (IOException io) {
      LOGGER.log(Level.SEVERE, "IOException..." + io.getLocalizedMessage());
    } catch (Exception er) {
      LOGGER.log(Level.SEVERE, "Exception..." + er.getLocalizedMessage());
    } finally {
      if (input != null) {
        try {
          input.close();
          con.disconnect();
          LOGGER.log(Level.FINE, "Closing socket..."); 
        } catch (Exception fetchException) {
          LOGGER.log(Level.SEVERE, fetchException.getMessage());
        } finally {
          //long timer2 = System.currentTimeMillis();
          //g3.setCurrentValue(timer2 - timer1);
        }
      }
    }
    
    // ensure to output with the correct encoding (not system default like the one on windows)
    /** try {
    	PrintStream out = new PrintStream(System.out, true, "UTF-8");
    	out.println(response.toString());
    } catch (Exception e) {
    	LOGGER.log(Level.SEVERE, "EncodingException..." + e.getLocalizedMessage());
    } **/
    
    String content= null;
    try {
	    byte b[] = String.valueOf(response).getBytes();
	    content = new String(b, "UTF-8");
    } catch (Exception e) {
    	LOGGER.log(Level.SEVERE, "EncodingException..." + e.getLocalizedMessage());
    } finally {
    	content = response.toString();
    }
    return content;
  }
}
