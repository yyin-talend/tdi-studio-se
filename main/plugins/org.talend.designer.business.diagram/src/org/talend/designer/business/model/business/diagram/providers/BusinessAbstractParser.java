package org.talend.designer.business.model.business.diagram.providers;

import java.text.MessageFormat;
import java.text.ParsePosition;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.gmf.runtime.common.ui.services.parser.IParserEditStatus;
import org.eclipse.gmf.runtime.common.ui.services.parser.ParserEditStatus;
import org.eclipse.gmf.runtime.emf.type.core.commands.SetValueCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.talend.designer.business.diagram.i18n.Messages;
import org.talend.designer.business.model.business.diagram.part.BusinessDiagramEditorPlugin;

/**
 * @generated
 */
public abstract class BusinessAbstractParser implements IParser {

    /**
     * @generated
     */
    private String viewPattern;

    /**
     * @generated
     */
    private MessageFormat viewProcessor;

    /**
     * @generated
     */
    private String editPattern;

    /**
     * @generated
     */
    private MessageFormat editProcessor;

    /**
     * @generated
     */
    public String getViewPattern() {
        return viewPattern;
    }

    /**
     * @generated
     */
    protected MessageFormat getViewProcessor() {
        return viewProcessor;
    }

    /**
     * @generated
     */
    public void setViewPattern(String viewPattern) {
        this.viewPattern = viewPattern;
        viewProcessor = createViewProcessor(viewPattern);
    }

    /**
     * @generated
     */
    protected MessageFormat createViewProcessor(String viewPattern) {
        return new MessageFormat(viewPattern);
    }

    /**
     * @generated
     */
    public String getEditPattern() {
        return editPattern;
    }

    /**
     * @generated
     */
    protected MessageFormat getEditProcessor() {
        return editProcessor;
    }

    /**
     * @generated
     */
    public void setEditPattern(String editPattern) {
        this.editPattern = editPattern;
        editProcessor = createEditProcessor(editPattern);
    }

    /**
     * @generated
     */
    protected MessageFormat createEditProcessor(String editPattern) {
        return new MessageFormat(editPattern);
    }

    /**
     * @generated
     */
    public String getPrintString(IAdaptable adapter, int flags) {
        return getStringByPattern(adapter, flags, getViewPattern(), getViewProcessor());
    }

    /**
     * @generated
     */
    public String getEditString(IAdaptable adapter, int flags) {
        return getStringByPattern(adapter, flags, getEditPattern(), getEditProcessor());
    }

    /**
     * @generated
     */
    protected abstract String getStringByPattern(IAdaptable adapter, int flags, String pattern, MessageFormat processor);

    /**
     * @generated NOT
     */
    public IParserEditStatus isValidEditString(IAdaptable element, String editString) {
        ParsePosition pos = new ParsePosition(0);
        Object[] values = getEditProcessor().parse(editString, pos);
        if (values == null) {
            return new ParserEditStatus(BusinessDiagramEditorPlugin.ID, IParserEditStatus.UNEDITABLE, Messages
                    .getString("BusinessAbstractParser.InvalidInputAt") //$NON-NLS-1$
                    + pos.getErrorIndex());
        }
        return validateNewValues(values);
    }

    /**
     * @generated
     */
    protected IParserEditStatus validateNewValues(Object[] values) {
        return ParserEditStatus.EDITABLE_STATUS;
    }

    /**
     * @generated
     */
    public ICommand getParseCommand(IAdaptable adapter, String newString, int flags) {
        Object[] values = getEditProcessor().parse(newString, new ParsePosition(0));
        if (values == null || validateNewValues(values).getCode() != IParserEditStatus.EDITABLE) {
            return UnexecutableCommand.INSTANCE;
        }
        return getParseCommand(adapter, values);
    }

    /**
     * @generated
     */
    protected abstract ICommand getParseCommand(IAdaptable adapter, Object[] values);

    /**
     * @generated
     */
    public IContentAssistProcessor getCompletionProcessor(IAdaptable element) {
        return null;
    }

    /**
     * @generated
     */
    protected ICommand getModificationCommand(EObject element, EStructuralFeature feature, Object value) {
        value = getValidNewValue(feature, value);
        if (value instanceof InvalidValue) {
            return UnexecutableCommand.INSTANCE;
        }
        SetRequest request = new SetRequest(element, feature, value);
        return new SetValueCommand(request);
    }

    /**
     * @generated
     */
    protected Object getValidValue(EStructuralFeature feature, Object value) {
        EClassifier type = feature.getEType();
        if (type instanceof EDataType) {
            Class iClass = type.getInstanceClass();
            if (String.class.equals(iClass)) {
                if (value == null) {
                    value = ""; //$NON-NLS-1$
                }
            }
        }
        return value;
    }

    /**
     * @generated NOT
     */
    protected Object getValidNewValue(EStructuralFeature feature, Object value) {
        EClassifier type = feature.getEType();
        if (type instanceof EDataType) {
            Class iClass = type.getInstanceClass();
            if (Boolean.TYPE.equals(iClass)) {
                if (value instanceof Boolean) {
                    // ok
                } else if (value instanceof String) {
                    value = Boolean.valueOf((String) value);
                } else {
                    value = new InvalidValue(Messages.getString("BusinessAbstractParser.ValueOfBooleanIsExpected")); //$NON-NLS-1$
                }
            } else if (Character.TYPE.equals(iClass)) {
                if (value instanceof Character) {
                    // ok
                } else if (value instanceof String) {
                    String s = (String) value;
                    if (s.length() == 0) {
                        value = null;
                    } else {
                        value = new Character(s.charAt(0));
                    }
                } else {
                    value = new InvalidValue(Messages
                            .getString("BusinessAbstractParser.ValueOfTypeCharacterIsExpected")); //$NON-NLS-1$
                }
            } else if (Byte.TYPE.equals(iClass)) {
                if (value instanceof Byte) {
                    // ok
                } else if (value instanceof Number) {
                    value = new Byte(((Number) value).byteValue());
                } else if (value instanceof String) {
                    String s = (String) value;
                    if (s.length() == 0) {
                        value = null;
                    } else {
                        try {
                            value = Byte.valueOf(s);
                        } catch (NumberFormatException nfe) {
                            value = new InvalidValue(Messages
                                    .getString("BusinessAbstractParser.StringValueDoesNotConvertToByteValue")); //$NON-NLS-1$
                        }
                    }
                } else {
                    value = new InvalidValue(Messages.getString("BusinessAbstractParser.ValueOfTypeByteIsExpected")); //$NON-NLS-1$
                }
            } else if (Short.TYPE.equals(iClass)) {
                if (value instanceof Short) {
                    // ok
                } else if (value instanceof Number) {
                    value = new Short(((Number) value).shortValue());
                } else if (value instanceof String) {
                    String s = (String) value;
                    if (s.length() == 0) {
                        value = null;
                    } else {
                        try {
                            value = Short.valueOf(s);
                        } catch (NumberFormatException nfe) {
                            value = new InvalidValue(Messages
                                    .getString("BusinessAbstractParser.StringShortConverError")); //$NON-NLS-1$
                        }
                    }
                } else {
                    value = new InvalidValue(Messages.getString("BusinessAbstractParser.ValueOfShortIsExpected")); //$NON-NLS-1$
                }
            } else if (Integer.TYPE.equals(iClass)) {
                if (value instanceof Integer) {
                    // ok
                } else if (value instanceof Number) {
                    value = new Integer(((Number) value).intValue());
                } else if (value instanceof String) {
                    String s = (String) value;
                    if (s.length() == 0) {
                        value = null;
                    } else {
                        try {
                            value = Integer.valueOf(s);
                        } catch (NumberFormatException nfe) {
                            value = new InvalidValue(Messages
                                    .getString("BusinessAbstractParser.StringIntegerConvertError")); //$NON-NLS-1$
                        }
                    }
                } else {
                    value = new InvalidValue(Messages.getString("BusinessAbstractParser.ValueOfIntegerIsExpected")); //$NON-NLS-1$
                }
            } else if (Long.TYPE.equals(iClass)) {
                if (value instanceof Long) {
                    // ok
                } else if (value instanceof Number) {
                    value = new Long(((Number) value).longValue());
                } else if (value instanceof String) {
                    String s = (String) value;
                    if (s.length() == 0) {
                        value = null;
                    } else {
                        try {
                            value = Long.valueOf(s);
                        } catch (NumberFormatException nfe) {
                            value = new InvalidValue(Messages.getString("BusinessAbstractParser.StringLongConverError")); //$NON-NLS-1$
                        }
                    }
                } else {
                    value = new InvalidValue(Messages.getString("BusinessAbstractParser.ValueOfLongIsExpected")); //$NON-NLS-1$
                }
            } else if (Float.TYPE.equals(iClass)) {
                if (value instanceof Float) {
                    // ok
                } else if (value instanceof Number) {
                    value = new Float(((Number) value).floatValue());
                } else if (value instanceof String) {
                    String s = (String) value;
                    if (s.length() == 0) {
                        value = null;
                    } else {
                        try {
                            value = Float.valueOf(s);
                        } catch (NumberFormatException nfe) {
                            value = new InvalidValue(Messages
                                    .getString("BusinessAbstractParser.StringFloatConvertError")); //$NON-NLS-1$
                        }
                    }
                } else {
                    value = new InvalidValue(Messages.getString("BusinessAbstractParser.ValueOfFloatIsExpected")); //$NON-NLS-1$
                }
            } else if (Double.TYPE.equals(iClass)) {
                if (value instanceof Double) {
                    // ok
                } else if (value instanceof Number) {
                    value = new Double(((Number) value).doubleValue());
                } else if (value instanceof String) {
                    String s = (String) value;
                    if (s.length() == 0) {
                        value = null;
                    } else {
                        try {
                            value = Double.valueOf(s);
                        } catch (NumberFormatException nfe) {
                            value = new InvalidValue(Messages
                                    .getString("BusinessAbstractParser.StringDoubleConvertError")); //$NON-NLS-1$
                        }
                    }
                } else {
                    value = new InvalidValue(Messages.getString("BusinessAbstractParser.ValueOfDoubleIsExpected")); //$NON-NLS-1$
                }
            } else if (type instanceof EEnum) {
                if (value instanceof String) {
                    EEnumLiteral literal = ((EEnum) type).getEEnumLiteralByLiteral((String) value);
                    if (literal == null) {
                        value = new InvalidValue(Messages.getString("BusinessAbstractParser.UnknownLiteral") + value); //$NON-NLS-1$
                    } else {
                        value = literal.getInstance();
                    }
                } else {
                    value = new InvalidValue(Messages.getString("BusinessAbstractParser.ValueOfTypeStringIsExpected")); //$NON-NLS-1$
                }
            }
        }
        return value;
    }

    /**
     * @generated
     */
    protected class InvalidValue {

        private String description;

        public InvalidValue(String description) {
            this.description = description;
        }

        public String toString() {
            return description;
        }
    }
}
