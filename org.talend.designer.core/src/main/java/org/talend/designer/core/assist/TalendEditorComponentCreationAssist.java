package org.talend.designer.core.assist;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.designer.core.ui.editor.PaletteComponentFactory;
import org.talend.designer.core.ui.editor.nodes.NodePart;
import org.talend.repository.model.ComponentsFactoryProvider;

class TalendEditorComponentCreationAssist {

    private static Map<String, Map<String, IComponent>> entries = new HashMap<String, Map<String, IComponent>>();

    private static Text assistText;

    private String categoryName;

    private GraphicalViewer graphicViewer;

    private Control graphicControl;

    private CommandStack commandStack;

    public TalendEditorComponentCreationAssist(String categoryName, GraphicalViewer viewer, CommandStack commandStack) {
        this.categoryName = categoryName;
        this.graphicViewer = viewer;
        this.graphicControl = viewer.getControl();
        this.commandStack = commandStack;
    }

    /**
     * read all components belongs to some category (DI, CAMEL etc.) then store it into a map which can be reused
     * 
     * @param categoryName
     * @param entries
     */
    /*
     * TODO this can be improved after refactoring org.talend.core.model.components.IComponentsHandler implementation in each editor
     */
    private void readComponentsInCategory(String categoryName, Map<String, IComponent> entries) {
        IComponentsFactory componentsFactory = ComponentsFactoryProvider.getInstance();
        Set<IComponent> allComponents = componentsFactory.getComponents();
        for (IComponent component : allComponents) {
            String compType = component.getPaletteType();
            if (!component.isTechnical() && compType != null && categoryName.equals(compType)) {
                entries.put(component.getName(), component);
            }
        }
    }

    /**
     * get all components belongs to some category
     * 
     * @param categoryName
     * @return
     */
    private Map<String, IComponent> getComponentsInCategory(String categoryName) {
        Map<String, IComponent> map = entries.get(categoryName);
        if (map == null) {
            map = new HashMap<String, IComponent>();
            entries.put(categoryName, map);
            readComponentsInCategory(categoryName, map);
        }
        return map;
    }

    /**
     * open the creation assist according to the trigger character
     * 
     * @param triggerChar
     */
    public void showComponentCreationAssist(char triggerChar) {

        /*
         * calculate the cursor position on current editor
         */
        org.eclipse.swt.graphics.Point cursorAbsLocation = graphicControl.getDisplay().getCursorLocation();
        org.eclipse.swt.graphics.Point cursorRelativePosition = graphicControl.getDisplay().map(null, graphicControl,
                cursorAbsLocation);
        if (!isInsideGraphic(cursorRelativePosition, graphicControl.getSize())) {
            return;
        }

        /*
         * only one assist text at the same time in all editors
         */
        disposeAssistText();

        // create assist input text
        assistText = new Text((Composite) graphicControl, SWT.BORDER);
        assistText.setLocation(cursorRelativePosition.x, cursorRelativePosition.y);
        assistText.setSize(200, assistText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
        assistText.setFocus();

        /*
         * create the proposal by using available components list
         */
        // TODO the trigger way may need improved, currently, any visible character will trigger it
        final Map<String, IComponent> components = getComponentsInCategory(categoryName);
        TalendEditorComponentProposalProvider proposalProvider = new TalendEditorComponentProposalProvider(components);
        final ContentProposalAdapter contentProposalAdapter = new ContentProposalAdapter(assistText, new TextContentAdapter(),
                proposalProvider, null, null);
        contentProposalAdapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
        contentProposalAdapter.setLabelProvider(new TalendEditorComponentLabelProvider(components));

        /*
         * add listeners to control when the proposal dialog shows or hide
         */
        // TODO this may need improvement.
        initListeners(components, contentProposalAdapter);

        // set init text content
        assistText.setText(triggerChar + "");
        assistText.setSelection(assistText.getText().length());

        // trigger proposal dialog
        Event event = new Event();
        event.character = triggerChar;
        assistText.notifyListeners(SWT.KeyDown, event);
        assistText.notifyListeners(SWT.Modify, event);
    }

    private void initListeners(final Map<String, IComponent> components, final ContentProposalAdapter contentProposalAdapter) {
        assistText.addKeyListener(new KeyListener() {

            public void keyReleased(KeyEvent e) {
                if (e.stateMask == SWT.NONE) {
                    if (e.keyCode == SWT.ESC) {
                        disposeAssistText();
                    } else if (e.keyCode == SWT.CR) {
                        createAndSelectComponent(components.get(assistText.getText().trim()), assistText.getLocation());
                        disposeAssistText();
                    }
                }
            }

            public void keyPressed(KeyEvent e) {

            }
        });
        assistText.addFocusListener(new FocusListener() {

            public void focusLost(FocusEvent e) {
                if (!(contentProposalAdapter.isProposalPopupOpen())) {
                    disposeAssistText();
                }
            }

            public void focusGained(FocusEvent e) {

            }
        });
        contentProposalAdapter.addContentProposalListener(new IContentProposalListener2() {

            public void proposalPopupOpened(ContentProposalAdapter adapter) {

            }

            public void proposalPopupClosed(ContentProposalAdapter adapter) {
                if (!assistText.isFocusControl()) {
                    disposeAssistText();
                }
            }
        });
        contentProposalAdapter.addContentProposalListener(new IContentProposalListener() {

            public void proposalAccepted(IContentProposal proposal) {
                createAndSelectComponent(components.get(assistText.getText().trim()), assistText.getLocation());
                disposeAssistText();
            }
        });
    }

    /**
     * create component at current position, according to select proposal label DOC talend2 Comment method
     * "createComponent".
     * 
     * @param componentName
     * @param location
     */
    protected void createAndSelectComponent(IComponent component, org.eclipse.swt.graphics.Point location) {
        Object createdNode = createComponent(component, location);
        selectComponent(createdNode);
    }

    private void selectComponent(Object createdNode) {
        Object nodePart = graphicViewer.getEditPartRegistry().get(createdNode);
        if (nodePart != null && nodePart instanceof NodePart) {
            graphicViewer.select((EditPart) nodePart);
        }
    }

    protected Object createComponent(IComponent component, org.eclipse.swt.graphics.Point location) {
        CreateRequest createRequest = new CreateRequest();
        createRequest.setLocation(new Point(location.x, location.y));
        createRequest.setFactory(new PaletteComponentFactory(component));
        Command command = graphicViewer.getContents().getCommand(createRequest);
        if (!command.canExecute()) {
            MessageDialog.openWarning(graphicControl.getShell(), "Failed", "Component can't be created here");
            return null;
        }
        commandStack.execute(command);
        Object obj = createRequest.getNewObject();
        createRequest = null;
        return obj;
    }

    /*
     * this used to judge the cursor is inside the editor or not
     */
    private boolean isInsideGraphic(org.eclipse.swt.graphics.Point cursorPosition, org.eclipse.swt.graphics.Point graphicSize) {
        if (cursorPosition.x < 0 || cursorPosition.y < 0) {
            return false;
        }
        if (cursorPosition.x > graphicSize.x || cursorPosition.y > graphicSize.y) {
            return false;
        }
        return true;
    }

    private void disposeAssistText() {
        if (assistText != null && !assistText.isDisposed()) {
            assistText.dispose();
            assistText = null;
        }
    }
}
