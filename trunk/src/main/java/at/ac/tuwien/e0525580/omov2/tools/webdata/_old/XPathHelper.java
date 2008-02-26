package at.ac.tuwien.e0525580.omov2.tools.webdata._old;



public class XPathHelper {
    /*
    
    public XPathHelper() {
        
       
    }
    
    public static void test(String relativeFileName) throws Exception { // http://xml.apache.org/xalan-j/xpath_apis.html

//      1. Instantiate an XPathFactory.
       XPathFactory factory = XPathFactory.newInstance();
       
       // 2. Use the XPathFactory to create a new XPath object
       XPath xpath = factory.newXPath();
       
       // 3. Compile an XPath string into an XPathExpression
       String str = "/html/body" +
            "/div[0]" + // id="wrapper
            "/div[1]" + // 0=banner, 1=root
            "/table[0]/tbody" + // table "outerbody"
            "/tr[0]/td[2]" + // middle column
            "/table[0]/tbody[0]" + // 
            "/tr[0]/td[0]" +
            "/table[0]/tbody" +
            "/tr[1]" + // second row
            "/td[0]"; // finally got to the content
       
       str += "/p[1]/table/tbody/tr/td[0]/a/@onclick";
       
       // /html/frameset/frame[1]/@src
       
       XPathExpression expression = xpath.compile(str);
       
       // 4. Evaluate the XPath expression on an input document
      String result = expression.evaluate(new InputSource(relativeFileName));
      System.out.println("result: " + result);
    }
    
//    private XMLParserLiaison xpathSupport;
//    private XPathProcessor xpathParser;
//    private PrefixResolver prefixResolver;
//    
//
//    public XPathHelper() { // http://www.javaworld.com/javaworld/jw-09-2000/jw-0908-xpath.html
//       xpathSupport = new XMLParserLiaisonDefault();
//       xpathParser = new XPathProcessorImpl(xpathSupport);
//    }
//    
//
//    public NodeList processXPath(String xpath, Node target) throws SAXException {
//       prefixResolver = new PrefixResolverDefault(target);
//       // create the XPath and initialize it
//       XPath xp = new XPath();
//       xpathParser.initXPath(xp, xpath, prefixResolver);
//       // now execute the XPath select statement
//       XObject list = xp.execute(xpathSupport, target, prefixResolver);
//       // return the resulting node
//       return list.nodeset();
//    }
    
    
    
    

//    public Node findAddress(String name, Document source) throws Exception {
//        XPathHelper xpathHelper = new XPathHelper();
//        
//        final String xpath = "//address[child::addressee[text() = '"+name+"']]";
//        NodeList nl = xpathHelper.processXPath(xpath, source.getDocumentElement());
//        return nl.item(0);
//     }
*/
}
