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

package org.talend.repository.editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;
import org.talend.core.model.properties.Item;

/**
 * DOC mhelleboid class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class RepositoryEditorInput extends FileEditorInput {

    private Item item;

    public RepositoryEditorInput(IFile file, Item item) {
        super(file);
        this.item = item;
    }

    public String getName() {
        // PTODO MHE use RepositoryLabelProvider when ready
        return "Model " + item.getProperty().getLabel();
    }

    public Item getItem() {
        return this.item;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RepositoryEditorInput other = (RepositoryEditorInput) obj;
        if (this.item == null) {
            if (other.item != null) {
                return false;
            }
        } else if (!this.item.equals(other.item)) {
            return false;
        }
        return true;
    }
}
