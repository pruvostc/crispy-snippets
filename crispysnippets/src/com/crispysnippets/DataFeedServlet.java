package com.crispysnippets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.crispysnippets.error.DigestProcessingException;
import com.crispysnippets.utils.HttpConnector;

/**
 * Servlet implementation class DataFeedServlet
 */
public class DataFeedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataFeedServlet() {
        super();
        // Auto-generated constructor stub
    }
     
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String src = request.getParameter("src");
		String sig = request.getParameter("sig");
		String stamp = request.getParameter("_s");
		String msg = "src=" + src + "&_s=" + stamp;
		
		// regenerate MessageDigest to check validity
		try {
			
		
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(msg.getBytes());
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}
			System.out.println("original:" + msg);
			System.out.println("digested(hex):" + sb.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		String resp = "";
	    
	    // the submitted param is used when <Carriage Return> was used to submit the form
	    if ((src != null) && !("").equals(src)) {
	    	URLDecoder.decode(src, "UTF-8");
			resp = HttpConnector.httpGet(src);
	    }
	    
	    // Auto-generated method stub
	 	response.getWriter().append(resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Auto-generated method stub
		doGet(request, response);
	}

}
