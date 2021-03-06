package com.function;

import java.io.FileNotFoundException; 
import java.io.FileOutputStream; 
import java.io.IOException; 
import java.io.PrintWriter; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import javax.xml.transform.OutputKeys; 
import javax.xml.transform.Transformer; 
import javax.xml.transform.TransformerConfigurationException; 
import javax.xml.transform.TransformerException; 
import javax.xml.transform.TransformerFactory; 
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult; 
import org.w3c.dom.Document; 
import org.w3c.dom.Element; 
import org.w3c.dom.Node; 
import org.w3c.dom.NodeList; 
import org.xml.sax.SAXException; 
  
public class Xml{ 
    private Document document; 
  
    public void init() { 
        try { 
            DocumentBuilderFactory factory = DocumentBuilderFactory 
                    .newInstance(); 
            DocumentBuilder builder = factory.newDocumentBuilder(); 
            this.document = builder.newDocument(); 
        } catch (ParserConfigurationException e) { 
            System.out.println(e.getMessage()); 
        } 
    } 
  
    public void createXml(String fileName) { 
        Element root = this.document.createElement("params");  
        this.document.appendChild(root);  
        Element uuid = this.document.createElement("UUID");  
        Element uuid_read = this.document.createElement("UUID_read");  
        Element uuid_write = this.document.createElement("UUID_write");  
        
        uuid.appendChild(uuid_read);  
        uuid.appendChild(uuid_write);
        root.appendChild(uuid);
       
        TransformerFactory tf = TransformerFactory.newInstance(); 
        try { 
            Transformer transformer = tf.newTransformer(); 
            DOMSource source = new DOMSource(document); 
            transformer.setOutputProperty(OutputKeys.ENCODING, "gb2312"); 
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName)); 
            StreamResult result = new StreamResult(pw); 
            transformer.transform(source, result); 
            System.out.println("????XML????????!"); 
        } catch (TransformerConfigurationException e) { 
            System.out.println(e.getMessage()); 
        } catch (IllegalArgumentException e) { 
            System.out.println(e.getMessage()); 
        } catch (FileNotFoundException e) { 
            System.out.println(e.getMessage()); 
        } catch (TransformerException e) { 
            System.out.println(e.getMessage()); 
        } 
    } 
  
    public void parserXml(String fileName) { 
        try { 
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
            DocumentBuilder db = dbf.newDocumentBuilder(); 
            Document document = db.parse(fileName); 
              
            NodeList employees = document.getChildNodes(); 
            for (int i = 0; i < employees.getLength(); i++) { 
                Node employee = employees.item(i); 
                NodeList employeeInfo = employee.getChildNodes(); 
                for (int j = 0; j < employeeInfo.getLength(); j++) { 
                    Node node = employeeInfo.item(j); 
                    NodeList employeeMeta = node.getChildNodes(); 
                    for (int k = 0; k < employeeMeta.getLength(); k++) { 
                        System.out.println(employeeMeta.item(k).getNodeName() 
                                + ":" + employeeMeta.item(k).getTextContent()); 
                    } 
                } 
            } 
            System.out.println("????????"); 
        } catch (FileNotFoundException e) { 
            System.out.println(e.getMessage()); 
        } catch (ParserConfigurationException e) { 
            System.out.println(e.getMessage()); 
        } catch (SAXException e) { 
            System.out.println(e.getMessage()); 
        } catch (IOException e) { 
            System.out.println(e.getMessage()); 
        } 
    } 
} 

