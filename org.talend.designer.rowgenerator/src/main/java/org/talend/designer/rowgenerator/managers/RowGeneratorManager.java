// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006 Talend - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
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
package org.talend.designer.rowgenerator.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.CommandStack;
import org.talend.designer.rowgenerator.RowGeneratorComponent;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: RowGeneratorManager.java,v 1.8 2007/01/31 10:31:05 pub Exp $
 * 
 */
public class RowGeneratorManager {

	private static List<Map<String, Object>> orginEP = new ArrayList<Map<String, Object>>();

	private static String orginNumber = "10";

	private RowGeneratorComponent rowGeneratorComponent;

	private UIManager uiManager;

	private CommandStack commandStack;

	/**
	 * qzhang RowGeneratorManager constructor comment.
	 */
	public RowGeneratorManager(RowGeneratorComponent rowGeneratorComponent) {
		this.rowGeneratorComponent = rowGeneratorComponent;
		this.uiManager = new UIManager(this);
		orginEP.clear();
		initOrginEP();
	}

	/**
	 * qzhang Comment method "initOrginEP".
	 */
	@SuppressWarnings("unchecked")
	private void initOrginEP() {
		List<Map<String, Object>> lines = rowGeneratorComponent.getMapList();
		for (Map<String, Object> map : lines) {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.putAll(map);
			orginEP.add(newMap);
		}
		orginNumber = rowGeneratorComponent.getNumber();
	}

	public UIManager getUiManager() {
		return this.uiManager;
	}

	public void setUiManager(UIManager uiManager) {
		this.uiManager = uiManager;
	}

	public CommandStack getCommandStack() {
		return this.commandStack;
	}

	public void setCommandStack(CommandStack commandStack) {
		this.commandStack = commandStack;
	}

	public RowGeneratorComponent getRowGeneratorComponent() {
		return this.rowGeneratorComponent;
	}

	public void setRowGeneratorComponent(
			RowGeneratorComponent rowGeneratorComponent) {
		this.rowGeneratorComponent = rowGeneratorComponent;
	}

	public List<Map<String, Object>> getOrginEP() {
		return orginEP;
	}

	public String getOrginNumber() {
		return orginNumber;
	}

	public void setOrginNumber(String orginNumber) {
		RowGeneratorManager.orginNumber = orginNumber;
	}

}
