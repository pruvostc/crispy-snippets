//import com.crispysnippets.utils.XsltUtils;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;



/**
 * A simple Tester Class to save some time.
 * @author PruvostC
 * 
 */
public class Tester {
  
  /**
   * tester only.
   * @param args arguments
   * @throws IOException Input/Output Exception
   * @throws URISyntaxException URI Syntax Exception
   * @throws TransformerException XSLT Transformer Exception
   */
  public static void main(final String[] args) 
      throws IOException, URISyntaxException, TransformerException {
    final TransformerFactory factory = TransformerFactory.newInstance();
    
    final Source xslt = new StreamSource(new File("war/xsltest.xsl"));

    final Transformer transformer = factory.newTransformer(xslt);

    final Source text = new StreamSource(new File("war/ukfederation-metadata-saved.xml"));
    
    transformer.transform(text, new StreamResult(new File("war/output.html")));
    
  }
}

