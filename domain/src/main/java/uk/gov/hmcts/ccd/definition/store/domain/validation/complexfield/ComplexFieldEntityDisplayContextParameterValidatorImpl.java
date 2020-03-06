package uk.gov.hmcts.ccd.definition.store.domain.validation.complexfield;

import joptsimple.internal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.definition.store.domain.displaycontextparameter.DisplayContextParameter;
import uk.gov.hmcts.ccd.definition.store.domain.displaycontextparameter.DisplayContextParameterType;
import uk.gov.hmcts.ccd.definition.store.domain.validation.ValidationResult;
import uk.gov.hmcts.ccd.definition.store.domain.validation.displaycontextparameter.AbstractDisplayContextParameterValidator;
import uk.gov.hmcts.ccd.definition.store.domain.validation.displaycontextparameter.DisplayContextParameterValidatorFactory;
import uk.gov.hmcts.ccd.definition.store.repository.FieldTypeUtils;
import uk.gov.hmcts.ccd.definition.store.repository.entity.*;

@Component
public class ComplexFieldEntityDisplayContextParameterValidatorImpl extends AbstractDisplayContextParameterValidator<ComplexFieldEntity> implements ComplexFieldValidator {

    private static final DisplayContextParameterType[] ALLOWED_TYPES =
        {DisplayContextParameterType.DATETIMEDISPLAY, DisplayContextParameterType.DATETIMEENTRY};
    private static final String[] ALLOWED_FIELD_TYPES =
        {FieldTypeUtils.BASE_DATE, FieldTypeUtils.BASE_DATE_TIME};

    @Autowired
    public ComplexFieldEntityDisplayContextParameterValidatorImpl(final DisplayContextParameterValidatorFactory displayContextParameterValidatorFactory) {
        super(displayContextParameterValidatorFactory, ALLOWED_TYPES, ALLOWED_FIELD_TYPES);
    }

    @Override
    public ValidationResult validate(ComplexFieldEntity complexFieldEntity, ValidationContext validationContext) {
        return validate(complexFieldEntity);
    }

    @Override
    protected void validateDisplayContextParameterType(final DisplayContextParameter displayContextParameter,
                                                       final ComplexFieldEntity entity,
                                                       final ValidationResult validationResult) {
        if (displayContextParameter.getType() != DisplayContextParameterType.DATETIMEDISPLAY &&
            displayContextParameter.getType() != DisplayContextParameterType.DATETIMEENTRY) {
            validationResult.addError(unsupportedDisplayContextParameterTypeError(entity));
        }
        super.validateDisplayContextParameterType(displayContextParameter, entity, validationResult);
    }

    @Override
    protected String getDisplayContextParameter(final ComplexFieldEntity entity) {
        return entity.getDisplayContextParameter();
    }

    @Override
    protected String getFieldType(final ComplexFieldEntity entity) {
        if (entity.getFieldType() == null) {
            return Strings.EMPTY;
        } else {
            return entity.getFieldType().getReference();
        }
    }

    protected String getCaseFieldReference(final ComplexFieldEntity entity) {
        return (entity.getReference() != null ? entity.getReference() : "");
    }

    @Override
    protected String getSheetName(ComplexFieldEntity entity) {
        return "ComplexTypes";
    }
}
