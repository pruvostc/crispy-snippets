package com.crispysnippets.utils.saml;

import com.crispysnippets.utils.XpathUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;



public class Saml2Utils {

  private static final String ORGNAME_XPATH = 
      "//EntityDescriptor/Organization/OrganizationName/text()";
  private static final String SEP = "\n";
  
  /**
   * Returns the list of organisation names.
   * @param doc the Document with the Metadata
   * @return String the list of organisation names as a string
   */
  public static String getOrganisationName(Document doc) {
    StringBuilder orgname = new StringBuilder();
    try {
      NodeList nodes = XpathUtils.doXPathSearch(ORGNAME_XPATH, doc);
      for (int i = 0; i < nodes.getLength(); i++) {
        //System.out.println(nodes.item(i).getNodeValue());
        if (null != nodes.item(i)) {
          orgname.append(nodes.item(i).getNodeValue()).append(SEP);
        }
      }
    } catch (Exception ee) {
      // do something
    }
    
    return orgname.toString();
  }
  
}
