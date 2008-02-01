// ============================================================================
//
// Copyright (C) 2006-2007 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.componentdesigner.model.componentpref;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.talend.componentdesigner.PluginConstant;
import org.talend.componentdesigner.model.ILibEntry;
import org.talend.componentdesigner.model.enumtype.JetFileStamp;
import org.talend.componentdesigner.model.enumtype.LanguageType;
import org.talend.componentdesigner.model.enumtype.ResourceLanguageType;
import org.talend.componentdesigner.model.libentry.JarLibEntry;
import org.talend.componentdesigner.model.libentry.PmLibEntry;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author rli
 * 
 */
public class ComponentPref {

    private static final String FIELDS_SEPARATOR = "#"; //$NON-NLS-1$

    private static final String EQUEL_FIELDS_SEPARATOR = "="; //$NON-NLS-1$

    private static final String INTERNAL_FIELDS_SEPARATOR = ";"; //$NON-NLS-1$

    private String name;

    private String longName;

    private String family;

    private LanguageType languageType;

    private List<ResourceLanguageType> resourceLanguageTypes;

    private List<JetFileStamp> jetFileStamps;

    private String imageURL;

    private ILibEntry[] libEntries;

    private String xmlFileName;

    private Document xmlDocument;

    private Node[] jarImportNodes;

    private Node[] perImportNodes;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for longName.
     * 
     * @return the longName
     */
    public String getLongName() {
        return longName;
    }

    /**
     * Sets the longName.
     * 
     * @param longName the longName to set
     */
    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * Getter for family.
     * 
     * @return the family
     */
    public String getFamily() {
        return family;
    }

    /**
     * Sets the family.
     * 
     * @param family the family to set
     */
    public void setFamily(String family) {
        this.family = family;
    }

    /**
     * @return the languageType
     */
    public LanguageType getLanguageType() {
        return languageType;
    }

    /**
     * @param languageType the languageType to set
     */
    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    /**
     * @return the resourceLanguageTypes
     */
    public List<ResourceLanguageType> getResourceLanguageTypes() {
        return resourceLanguageTypes;
    }

    /**
     * @param resourceLanguageTypes the resourceLanguageTypes to set
     */
    public void setResourceLanguageTypes(List<ResourceLanguageType> resourceLanguageTypes) {
        this.resourceLanguageTypes = resourceLanguageTypes;
    }

    /**
     * @return the jetFileTypes
     */
    public List<JetFileStamp> getJetFileStamps() {
        return jetFileStamps;
    }

    /**
     * @param jetFileStamps the jetFileTypes to set
     */
    public void setJetFileStamps(List<JetFileStamp> jetFileStamps) {
        this.jetFileStamps = jetFileStamps;
    }

    /**
     * @return the imageURL
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * @param imageURL the srcImageName to set
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * @return the libFileURL
     */
    public ILibEntry[] getLibEntries() {
        return libEntries;
    }

    /**
     * @param libFileURL the libFileName to set
     */
    public void setLibEntries(ILibEntry[] libEntries) {
        this.libEntries = libEntries;
    }

    public static ComponentPref writeFromString(String s) {
        ComponentPref toReturn = new ComponentPref();
        try {
            String[] st = s.split(FIELDS_SEPARATOR, -1);
            int i = 0;
            toReturn.setName(st[i++]);
            toReturn.setLanguageType(LanguageType.find(st[i++]));
            String fieldString;
            String singleString;
            String[] internalFields;

            // 1. parse to jetFileTypes
            fieldString = st[i++];
            internalFields = fieldString.split(INTERNAL_FIELDS_SEPARATOR, 0);
            List<JetFileStamp> stampList = new ArrayList<JetFileStamp>();
            for (int j = 0; j < internalFields.length; j++) {
                singleString = internalFields[j];
                stampList.add(JetFileStamp.findFileStamp(singleString));
            }
            toReturn.setJetFileStamps(stampList);

            // 2. parse to resourceFileTypes
            fieldString = st[i++];
            internalFields = fieldString.split(INTERNAL_FIELDS_SEPARATOR, 0);
            List<ResourceLanguageType> resourceLangList = new ArrayList<ResourceLanguageType>();
            for (int j = 0; j < internalFields.length; j++) {
                singleString = internalFields[j];
                resourceLangList.add(ResourceLanguageType.find(singleString));
            }
            toReturn.setResourceLanguageTypes(resourceLangList);

            // 3. parse to resourceFileTypes
            fieldString = st[i++];
            if (!fieldString.equals(PluginConstant.EMPTY_STRING)) {
                toReturn.setImageURL(fieldString);
            }

            // 4. parse to library entries
            fieldString = st[i++];
            if (fieldString.equals(PluginConstant.EMPTY_STRING)) {
                return toReturn;
            }
            internalFields = fieldString.split(INTERNAL_FIELDS_SEPARATOR, 0);
            handleLibEntries(internalFields, toReturn);

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private static void handleLibEntries(String[] internalFields, ComponentPref componentBean) {
        ILibEntry[] libArrays = new ILibEntry[internalFields.length];
        for (int j = 0; j < internalFields.length; j++) {
            String[] entryStrings = internalFields[j].split(EQUEL_FIELDS_SEPARATOR);
            int k = 0;
            String libName = entryStrings[k++];
            String isExternal = entryStrings[k++];
            String location = entryStrings[k++];
            Object obj = null;
            if (Boolean.valueOf(isExternal)) {
                obj = new Path(location);
            } else {
                obj = ResourcesPlugin.getWorkspace().getRoot().getProject(PluginConstant.PROJECTNAME_DEFAULT)
                        .findMember(location);
            }
            if (libName.matches("(?i).*\\.(jar)\\b")) {
                libArrays[j] = new JarLibEntry(obj);
            } else if (libName.matches("(?i).*\\.(pm)\\b")) {
                libArrays[j] = new PmLibEntry(obj);
            } else {
                // Nothing
            }

        }
        componentBean.setLibEntries(libArrays);
    }

    /**
     * Parse fields of current class to string.
     * 
     * @return
     */
    public String readToString() {
        StringBuffer fields = new StringBuffer(getName() + FIELDS_SEPARATOR + getLanguageType().getNameSuffix()
                + FIELDS_SEPARATOR);
        // 1. append jetFileTypes
        if (this.getJetFileStamps().size() > 0) {
            for (JetFileStamp fileStamp : getJetFileStamps()) {
                fields.append(fileStamp.getFileStampName());
                fields.append(INTERNAL_FIELDS_SEPARATOR);
            }
        }
        fields.append(FIELDS_SEPARATOR);

        // 2. append resourceFileTypes.
        if (this.resourceLanguageTypes.size() > 0) {
            for (ResourceLanguageType resourceType : resourceLanguageTypes) {
                fields.append(resourceType.getLang());
                fields.append(INTERNAL_FIELDS_SEPARATOR);
            }
        }
        fields.append(FIELDS_SEPARATOR);

        // 3. append imageUrl String.
        if (this.imageURL == null) {
            fields.append(PluginConstant.EMPTY_STRING);
        } else {
            fields.append(imageURL);
        }
        fields.append(FIELDS_SEPARATOR);

        // 4. append library entries string.
        if (libEntries != null && this.libEntries.length > 0) {
            for (ILibEntry entry : this.libEntries) {
                fields.append(entry.getName());
                fields.append(EQUEL_FIELDS_SEPARATOR);
                fields.append(entry.isExternal());
                fields.append(EQUEL_FIELDS_SEPARATOR);
                fields.append(entry.getLocation());
                fields.append(INTERNAL_FIELDS_SEPARATOR);
            }
        }
        return fields.toString();
    }

    /**
     * Getter for xmlFileName.
     * 
     * @return the xmlFileName
     */
    public String getXmlFileName() {
        return xmlFileName;
    }

    /**
     * Sets the xmlFileName.
     * 
     * @param xmlFileName the xmlFileName to set
     */
    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    /**
     * Getter for xmlDocument.
     * 
     * @return the xmlDocument
     */
    public Document getXmlDocument() {
        return xmlDocument;
    }

    /**
     * Sets the xmlDocument.
     * 
     * @param xmlDocument the xmlDocument to set
     */
    public void setXmlDocument(Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    /**
     * Getter for jarImportNodes.
     * 
     * @return the jarImportNodes
     */
    public Node[] getJarImportNodes() {
        return this.jarImportNodes;
    }

    /**
     * Sets the jarImportNodes.
     * 
     * @param jarImportNodes the jarImportNodes to set
     */
    public void setJarImportNodes(Node[] jarImportNodes) {
        this.jarImportNodes = jarImportNodes;
    }

    /**
     * Getter for perImportNodes.
     * 
     * @return the perImportNodes
     */
    public Node[] getPerImportNodes() {
        return this.perImportNodes;
    }

    /**
     * Sets the perImportNodes.
     * 
     * @param perImportNodes the perImportNodes to set
     */
    public void setPerImportNodes(Node[] perImportNodes) {
        this.perImportNodes = perImportNodes;
    }

    public Document getJavaXmlDocument() {
        return removeUnusedNodes(this.getPerImportNodes());
    }

    public Document getPerlXmlDocument() {
        return removeUnusedNodes(getJarImportNodes());
    }

    public Document getCurrentTypeDocument(String xmlType) {
        if (xmlType.equals(PluginConstant.JAVA_XML_SUFFIX)) {
            return this.getJavaXmlDocument();
        } else {
            return this.getPerlXmlDocument();
        }
    }

    private Document removeUnusedNodes(Node[] importNodes) {
        Document documentCopy = null;

        if (importNodes != null && importNodes.length != 0) {
            Node importsNode = importNodes[0].getParentNode();
            for (Node node : importNodes) {
                importsNode.removeChild(node);
            }

            documentCopy = (Document) this.xmlDocument.cloneNode(true);

            for (Node node : importNodes) {
                importsNode.appendChild(node);
            }
        } else {
            documentCopy = this.xmlDocument;
        }
        return documentCopy;
    }
}
