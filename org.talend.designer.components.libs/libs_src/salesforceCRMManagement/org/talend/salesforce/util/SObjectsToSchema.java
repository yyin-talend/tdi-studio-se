package org.talend.salesforce.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.rpc.ServiceException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.sforce16.soap.partner.DescribeGlobalResult;
import com.sforce16.soap.partner.DescribeSObjectResult;
import com.sforce16.soap.partner.Field;
import com.sforce16.soap.partner.FieldType;
import com.sforce16.soap.partner.LoginResult;
import com.sforce16.soap.partner.SessionHeader;
import com.sforce16.soap.partner.SforceServiceLocator;
import com.sforce16.soap.partner.SoapBindingStub;
import com.sforce16.soap.partner.fault.InvalidIdFault;
import com.sforce16.soap.partner.fault.LoginFault;
import com.sforce16.soap.partner.fault.UnexpectedErrorFault;

public class SObjectsToSchema {

    /**
     * Update modules and schemas in source XML file with Salesforce user's SObject.
     * 
     * For example: SObjectsToSchema.update("tSalesforce_java.xml", "tSalesforce_java_output.xml",url,username,password)
     * can generate a new XML file that replace old modules with the latest modules.
     * 
     * Custom module are not included, it is still can be set manually.
     * 
     * dom4j-1.6.1.jar seems doesn't support write textual attribute value, my test result shows that setEscapeText()
     * only useful to text except attribute value, so I just replace the flag to work around this.
     * 
     * @param sourceFile old XML file
     * @param destinationFile genrated XML file
     * @param url
     * @param username
     * @param password
     */
    public static void update(String sourceFile, String destinationFile, String url, String username, String password) {
        try {
            // load template XML file
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new File(sourceFile));
            // get module list node
            List<Element> list = document.selectNodes("/COMPONENT/PARAMETERS/PARAMETER/ITEMS");
            Element itemsElement = null;
            for (Element e : list) {
                if (e.getParent().attribute("NAME") != null) {
                    if (e.getParent().attribute("NAME").getValue().equals("MODULENAME")) {
                        itemsElement = e;
                        itemsElement.clearContent();
                    }
                }
            }
            // get schema node
            list = document.selectNodes("/COMPONENT/PARAMETERS/PARAMETER");
            Element parameterElement = null;
            for (Element e : list) {
                if (e.attribute("NAME") != null) {
                    if (e.attribute("NAME").getValue().equals("SCHEMA")) {
                        parameterElement = e;
                        parameterElement.clearContent();
                    }
                }
            }
            // SF login
            SoapBindingStub binding = (SoapBindingStub) new SforceServiceLocator().getSoap(new URL(url));
            binding.setTimeout(60000);
            LoginResult loginResult = binding.login(username, password);
            binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY, loginResult.getServerUrl());
            SessionHeader sh = new SessionHeader();
            sh.setSessionId(loginResult.getSessionId());
            binding.setHeader(new SforceServiceLocator().getServiceName().getNamespaceURI(), "SessionHeader", sh);
            // get all SObjects
            DescribeGlobalResult describeGlobalResult = binding.describeGlobal();
            if (!(describeGlobalResult == null)) {
                String[] types = describeGlobalResult.getTypes();
                if (!(types == null)) {
                    // loop SObject fields
                    for (int i = 0; i < types.length; i++) {
                        String module = types[i];
                        System.out.println("module: " + module);
                        // add module list, ignore custom module
                        if (!module.contains("__c")) {
                            Element itemElement = itemsElement.addElement("ITEM");
                            itemElement.addAttribute("NAME", module);
                            itemElement.addAttribute("VALUE", module);
                            Element tableElement = parameterElement.addElement("TABLE");
                            tableElement.addAttribute("IF", "MODULENAME=='" + module + "'");
                            DescribeSObjectResult describeSObjectResult = binding.describeSObject(module);
                            if (!(describeSObjectResult == null)) {
                                Field[] fields = describeSObjectResult.getFields();
                                if (!(fields == null)) {
                                    for (int j = 0; j < fields.length; j++) {
                                        Field field = fields[j];
                                        boolean isKey = field.getName().equals("Id");
                                        int length = field.getLength();
                                        String fieldName = field.getName();
                                        FieldType fieldType = field.getType();
                                        System.out.println(isKey ? "true" : "false" + "|" + length + "|" + fieldName + "|"
                                                + ConvertSFTypeToTOSType.getTOSValue(fieldType.getValue()));
                                        // add schemam, ignore custom module
                                        if (!fieldName.contains("__c")) {
                                            Element columnElement = tableElement.addElement("COLUMN");
                                            columnElement.addAttribute("KEY", isKey ? "true" : "false");
                                            columnElement.addAttribute("LENGTH", String.valueOf(length));
                                            columnElement.addAttribute("NAME", fieldName);
                                            columnElement.addAttribute("TYPE", ConvertSFTypeToTOSType.getTOSValue(fieldType
                                                    .getValue()));
                                            if ("id_Date".equals(ConvertSFTypeToTOSType.getTOSValue(fieldType.getValue()))) {
                                                // dom4j can't disable escape text in attribute value, so add a flag to
                                                // be replaced later.
                                                columnElement.addAttribute("PATTERN", "!!!PATTERN!!!");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // add custom module
                    Element itemElement = itemsElement.addElement("ITEM");
                    itemElement.addAttribute("NAME", "CustomModule");
                    itemElement.addAttribute("VALUE", "CustomModule");
                }
            }
            // write XML
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(new FileWriter(new File(destinationFile + ".temp")), format);
            // writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            // replace !!!PATTERN!!!
            String b = null;
            BufferedReader br = new BufferedReader(new FileReader(destinationFile + ".temp"));
            BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFile));
            while ((b = br.readLine()) != null) {
                if (b.contains("!!!PATTERN!!!")) {
                    bw.write(b.replace("!!!PATTERN!!!", "&quot;yyyy-MM-dd&apos;T&apos;HH:mm:ss&apos;.000Z&apos;&quot;"));
                } else {
                    bw.write(b);
                }
                bw.newLine();
                bw.flush();
            }
            br.close();
            bw.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnexpectedErrorFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidIdFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LoginFault e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            File tempFile = new File(destinationFile + ".temp");
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
        System.out.println("============finish============");
    }
}
