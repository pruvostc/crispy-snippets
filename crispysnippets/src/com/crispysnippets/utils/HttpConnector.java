package com.crispysnippets.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import java.util.zip.GZIPInputStream;

public class HttpConnector {

  private static final Logger LOGGER = Logger.getLogger(HttpConnector.class.getName());
  private static final String USER_AGENT = "CripySnippets Agent";
  private static final String DEFAULT_ENCODING = "UTF-8"; //consider using StandardCharsets.UTF_8 at some point
  
  /**
   * HTTP GET Method.
   * @param urlString URL to access via the GET Method
   * @return ByteArrayOutputStream (which you can easily convert to a String with the appropriate encoding later)
   * @author cpruvost
   */
  public static ByteArrayOutputStream httpGetByteArrayOutputStream(String urlString) {
	  ByteArrayOutputStream baos = new ByteArrayOutputStream();
	  InputStream input = null;
	  HttpURLConnection con = null;

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
	      con.setRequestProperty("Accept-Encoding", "gzip, deflate");
	      //con.setDoInput(false);
	      //con.setUseCaches(true);
	      
	      // open the connection and read the response from here onward
	      con.connect();
	      
	      int responseCode = con.getResponseCode();
	      LOGGER.log(Level.INFO, "GET request did not worked-> responseCode: " + responseCode);
	      LOGGER.log(Level.INFO, ">>>>>>>>> Content-Type: " + con.getContentType());
	      String contentEncoding = con.getContentEncoding();
	      LOGGER.log(Level.INFO, ">>>>>>>>> contentEncoding: " + contentEncoding);
	      if (responseCode == HttpURLConnection.HTTP_OK) { // success and read it all
	        LOGGER.log(Level.INFO, "content-Type: " + con.getContentType());
	        
	        // ensure to deflate the content if it is zipped
	        if ("gzip".equals(con.getContentEncoding())) {
	            input = new GZIPInputStream(con.getInputStream());
	        } else {
	            input = con.getInputStream();
	        }
	         
	        byte[] byteChunk = new byte[4096]; // The size is what you want to read in at a time.
		    int n;
		    
		    while ( (n = input.read(byteChunk)) > 0 ) { // read the response by byteChunk
			   baos.write(byteChunk, 0, n);
			}

	        // print result
	        LOGGER.log(Level.FINE,baos.toString());
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

	  return baos;
  }
  
  /**
   * HTTP GET Method.
   * @param urlString URL to access via the GET Method
   * @return String
   */
  public static String httpGet(String urlString) {
	  StringBuilder response = new StringBuilder();
	  response = httpGetStringBuilder(urlString);
	  String content= null;
	  try {
		  byte b[] = String.valueOf(response).getBytes();
		  content = new String(b, DEFAULT_ENCODING);
	  } catch (Exception e) {
		  LOGGER.log(Level.SEVERE, "EncodingException..." + e.getLocalizedMessage());
		  content = response.toString();
	  } 
	  return content;
  }
  /**
   * HTTP GET Method.
   * @param urlString URL to access via the GET Method
   * @return StringBuilder
   */
  public static StringBuilder httpGetStringBuilder(String urlString) {

    InputStream input = null;
    HttpURLConnection con = null;
    StringBuilder response = new StringBuilder();
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
        LOGGER.log(Level.INFO, "content-Type: " + con.getContentType());
        
        input = con.getInputStream();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(input, DEFAULT_ENCODING));
        
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();

        // print result
        LOGGER.log(Level.FINE,response.toString());
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
    	PrintStream out = new PrintStream(System.out, true, DEFAULT_ENCODING); // here "UTF-8"
    	out.println(response.toString());
    } catch (Exception e) {
    	LOGGER.log(Level.SEVERE, "EncodingException..." + e.getLocalizedMessage());
    } **/
    
    return response;
  }
}
