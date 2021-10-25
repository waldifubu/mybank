package util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class XMLparser {

    private Document doc;
    private Element elem;
    private Element root = null;
    private Transformer tr;
    private String filename;

    private ArrayList<String> tags = new ArrayList<String>();

    private String[] spar;
    private String[] giro;
    private String[] fest;
    private String[] alle;
    private String[] zahlung;

    private Properties p = new Properties();

    public XMLparser(String dat) {
        filename = dat;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        XMLparser xml = new XMLparser("rules.xml");
        xml.addRoot("mybank");

        xml.create("alle");
        xml.addText("6");
        xml.append();

        xml.create("spar");
        xml.addAtt("para1", "1.21");
        xml.addText("1");
        xml.append();

        xml.create("giro");
        xml.addAtt("para1", "10");
        xml.addAtt("para2", "20");
        xml.addText("5");
        xml.append();

        xml.create("fest");
        xml.addAtt("para1", "11");
        xml.addAtt("para2", "21");
        xml.addText("1");
        xml.append();

        xml.create("zahlung");
        xml.addText("37000");
        xml.append();

        xml.build("rules.xml");
        xml.output();
        xml.lesen();

        Properties p = xml.getall();
        System.out.println(p.getProperty("zahlung"));
    }

    public String[] getArray(String typ) {
        if (typ.equals("zahlung")) return zahlung;
        if (typ.equals("spar")) return spar;
        if (typ.equals("giro")) return giro;
        if (typ.equals("fest")) return fest;
        if (typ.equals("alle")) return alle;
        return null;
    }


    public void lesen() {
        try {

            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            // Wurzelelement
            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = null;
            String[] typen = {"spar", "giro", "fest", "zahlung", "alle"};

            for (int temp = 0; temp < typen.length; temp++) {
                nList = doc.getElementsByTagName(typen[temp]);

                //System.out.println(nList.getLength());
                //if(nList.getLength()>=)

                Node nNode = nList.item(0);

//				System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) { //Node
                    Element eElement = (Element) nNode;
//					System.out.println(eElement.getNodeName());
                    String para1 = eElement.getAttribute("para1");
                    String para2 = eElement.getAttribute("para2");
//					System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());

                    switch (typen[temp]) {
                        case "alle":
                            String[] al = {eElement.getTextContent()};
                            alle = al;
                            break;
                        case "spar":
                            String[] sp = {eElement.getTextContent(), para1};
                            spar = sp;
                            break;
                        case "giro":
                            String[] gi = {eElement.getTextContent(), para1, para2};
                            giro = gi;
                            break;
                        case "fest":
                            String[] fe = {eElement.getTextContent(), para1, para2};
                            fest = fe;
                            break;
                        case "zahlung":
                            String[] z = {eElement.getTextContent()};
                            zahlung = z;
                            break;
                        default:
                    } // Switch

                } //Node
            } //for-ende
        } //try-ende
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRoot(String str) {
        try {
            root = doc.createElement(str);
            doc.appendChild(root);
        } catch (org.w3c.dom.DOMException ex) {
            System.err.println("No more ROOT element allowed! Remove '" + str + "'");
            ex.getLocalizedMessage();
            System.exit(0);
        }
    }

    public void output() {
        File file = new File(filename);
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            int oneByte;
            while ((oneByte = fis.read()) != -1) {
                System.out.write(oneByte);
                // System.out.print((char)oneByte); // could also do this
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.flush();
    }


    public void build() {

        try {
            tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            //tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
            tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }

    }

    public void build(String str) {
        filename = str;
        build();
        save(str);
    }

    public void save(String str) {
        DOMSource source = new DOMSource(doc);
        PrintStream ps;
        try {
            ps = new PrintStream(str);
            StreamResult result = new StreamResult(ps);
            tr.transform(source, result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void addAtt(String str, String str2) {
        elem.setAttribute(str, str2);
    }

    public boolean append() {
        root.appendChild(elem);
        return true;
    }

    private boolean contains(String str) throws Exception {
        if (tags.isEmpty()) return false;
        for (String s : tags) {
            if (s.equals(str)) {
                throw new Exception();
            }
        }
        return false;
    }


    public boolean create(String str) {
        try {
            if (!contains(str)) tags.add(str);
        } catch (Exception e) {
            System.err.println("Already used tag '" + str + "'");
            e.printStackTrace();
            System.exit(0);
        }

        elem = doc.createElement(str);
        return true;
    }

    public void addText(String str) {
        elem.setTextContent(str);
    }

    public Properties getall() {
        //List<String> wordList = Arrays.asList(getArray("spar"));
        String[] b;

        b = getArray("alle");
        p.setProperty("alle", b[0]);

        b = getArray("spar");
        p.setProperty("spar", b[0]);
        p.setProperty("sparpro", b[1]);

        b = getArray("fest");
        p.setProperty("fest", b[0]);
        p.setProperty("festpro", b[1]);
        p.setProperty("festpro2", b[2]);

        b = getArray("giro");
        p.setProperty("giro", b[0]);
        p.setProperty("giropro", b[1]);
        p.setProperty("giropro2", b[2]);

        b = getArray("zahlung");
        p.setProperty("zahlung", b[0]);
        return p;
    }


}
