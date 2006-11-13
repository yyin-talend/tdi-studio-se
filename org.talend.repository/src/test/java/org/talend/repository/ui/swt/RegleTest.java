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
package org.talend.repository.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * RegleTest
 * 
 * $Id$
 * 
 */

public class RegleTest extends Composite {

    private static RegleTest composite;

    private Canvas canvas;

    public RegleTest(Composite parent, int style) {
        super(parent, style);

        composite = this;
        canvas = new Canvas(parent, SWT.NONE);
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                GC gc = e.gc;

                gc.drawLine(0, 20, 500, 20);

                int unite = 0;
                int uniteRAZ = 0;
                for (int i = 0; i < 500; i++) {
                    if (i % 9 == 0) {
                        if (uniteRAZ == 5) {
                            String num = "" + unite;
                            if (num.length() < 2) {
                                gc.drawText(num, i - 2, 2);
                            } else if (num.length() < 3) {
                                gc.drawText(num, i - 5, 2);
                            } else if (num.length() < 4) {
                                gc.drawText(num, i - 8, 2);
                            } else {
                                gc.drawText(num, i - 11, 2);
                            }
                            gc.drawLine(i, 16, i, 24);
                            uniteRAZ = 0;
                        } else {
                            gc.drawLine(i, 18, i, 22);
                        }
                        uniteRAZ += 1;
                        unite += 1;
                    }
                }
                gc.dispose();
            }
        });
    }
}
