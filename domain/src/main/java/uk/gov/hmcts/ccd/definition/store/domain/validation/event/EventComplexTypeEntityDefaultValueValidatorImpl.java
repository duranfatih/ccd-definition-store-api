package uk.gov.hmcts.ccd.definition.store.domain.validation.event;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.definition.store.domain.validation.ValidationResult;
import uk.gov.hmcts.ccd.definition.store.domain.validation.eventcasefield.EventCaseFieldEntityValidationContext;
import uk.gov.hmcts.ccd.definition.store.domain.validation.eventcasefieldcomplextype.EventComplexTypeEntityDefaultValueError;
import uk.gov.hmcts.ccd.definition.store.domain.validation.eventcasefieldcomplextype.EventComplexTypeEntityValidator;
import uk.gov.hmcts.ccd.definition.store.repository.entity.EventComplexTypeEntity;

@Component
public class EventComplexTypeEntityDefaultValueValidatorImpl implements EventComplexTypeEntityValidator {

    @Override
    public ValidationResult validate(EventComplexTypeEntity eventCaseFieldEntity, EventCaseFieldEntityValidationContext eventCaseFieldEntityValidationContext) {

        final ValidationResult validationResult = new ValidationResult();

        if (eventCaseFieldEntity.getReference().equals("OrgPolicyCaseAssignedRole")) {
            if (!eventCaseFieldEntityValidationContext.getCaseRoles().contains(eventCaseFieldEntity.getDefaultValue())) {
                validationResult.addError(
                    new EventComplexTypeEntityDefaultValueError(
                        eventCaseFieldEntity,
                        eventCaseFieldEntityValidationContext
                    ));

            }

        }
        return validationResult;
    }
}
