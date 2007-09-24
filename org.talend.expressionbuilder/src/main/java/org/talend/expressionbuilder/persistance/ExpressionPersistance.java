// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2007 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.expressionbuilder.persistance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.talend.commons.exception.RuntimeExceptionHandler;
import org.talend.core.model.expression.EMFExpression;
import org.talend.core.model.expression.EMFVariable;
import org.talend.core.model.expression.ExpressionFactory;
import org.talend.core.model.expression.ExpressionPackage;
import org.talend.expressionbuilder.test.shadow.Expression;
import org.talend.expressionbuilder.test.shadow.Variable;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExpressionPersistance.java 下午01:26:21 2007-9-24 +0000 (2007-9-24) yzhang $
 * 
 */
public class ExpressionPersistance {

    private String ownerId;

    private static ExpressionPersistance expressionPersistance;

    private String path;

    private final ResourceSet resourceSet;

    /**
     * yzhang ExpressionPersistance constructor comment.
     */
    private ExpressionPersistance() {
        resourceSet = new ResourceSetImpl();

        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
                Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

        resourceSet.getPackageRegistry().put(ExpressionPackage.eNS_URI, ExpressionPackage.eINSTANCE);
    }

    /**
     * yzhang Comment method "getInstance".
     * 
     * @return
     */
    public static ExpressionPersistance getInstance() {
        if (expressionPersistance == null) {
            expressionPersistance = new ExpressionPersistance();
        }
        return expressionPersistance;
    }

    /**
     * Getter for ownerId.
     * 
     * @return the ownerId
     */
    public String getOwnerId() {
        return this.ownerId;
    }

    /**
     * Sets the ownerId.
     * 
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Sets the path.
     * 
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * yzhang Comment method "convert".
     * 
     * @param expression
     * @return
     */
    private EMFExpression convert(Expression expression) {

        EMFExpression emfExpression = ExpressionFactory.eINSTANCE.createEMFExpression();
        emfExpression.setId(this.ownerId);
        emfExpression.setExpression(expression.getExpression());
        for (Variable variable : expression.getVariables()) {
            EMFVariable emfVariable = ExpressionFactory.eINSTANCE.createEMFVariable();
            emfVariable.setName(variable.getName());
            emfVariable.setValue(variable.getValue());
            emfExpression.getVariables().add(emfVariable);
        }

        return emfExpression;
    }

    /**
     * yzhang Comment method "convert".
     * 
     * @param emfExpression
     * @return
     */
    private Expression convert(EMFExpression emfExpression) {

        List<Variable> vars = new ArrayList<Variable>();

        for (EMFVariable emfVar : emfExpression.getVariables()) {
            vars.add(new Variable(emfVar.getName(), emfVar.getValue()));
        }

        return new Expression(emfExpression.getExpression(), vars);
    }

    /**
     * yzhang Comment method "saveExpression".
     * 
     * @param expression
     */
    public void saveExpression(Expression expression) {
        EMFExpression emfExpression = convert(expression);
        Resource resource = resourceSet.createResource(URI.createURI("http:///My.expression"));

        File file = new File(path);

        resource.getContents().add(emfExpression);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            resource.save(new FileOutputStream(file), null);
        } catch (IOException e) {
            RuntimeExceptionHandler.process(e);
        }

    }

    /**
     * yzhang Comment method "loadExpression".
     * 
     * @return
     */
    public List<Expression> loadExpression() {
        List<Expression> expressionList = new ArrayList<Expression>();
        File file = new File(path);
        if (!file.exists()) {
            return expressionList;
        }
        URI uri = URI.createFileURI(file.getAbsolutePath());
        Resource resource = resourceSet.getResource(uri, true);
        for (EObject eObject : resource.getContents()) {
            if (eObject instanceof EMFExpression) {
                expressionList.add(convert((EMFExpression) eObject));
            }
        }
        return expressionList;
    }

}
