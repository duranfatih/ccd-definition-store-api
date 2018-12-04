package uk.gov.hmcts.ccd.definition.store.domain.validation.searchaliasfield;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.defaultString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.definition.store.domain.validation.ValidationResult;
import uk.gov.hmcts.ccd.definition.store.repository.SearchAliasFieldRepository;
import uk.gov.hmcts.ccd.definition.store.repository.entity.SearchAliasFieldEntity;

@Component
public class SearchAliasFieldMapperTypeValidator implements SearchAliasFieldValidator {

    private final SearchAliasFieldRepository repository;

    @Autowired
    public SearchAliasFieldMapperTypeValidator(SearchAliasFieldRepository repository) {
        this.repository = repository;
    }

    @Override
    public ValidationResult validate(SearchAliasFieldEntity searchAliasField) {

        ValidationResult validationResult = new ValidationResult();

        List<SearchAliasFieldEntity> searchAliasFields = repository.findByReference(searchAliasField.getReference());
        searchAliasFields.forEach(field -> {
            if (!field.getFieldType().getReference().equalsIgnoreCase(searchAliasField.getFieldType().getReference())) {
                validationResult.addError(new ValidationError(String.format("Search alias type for '%s' is invalid. This search alias ID has been already "
                                                                                + "registered as '%s' for case type '%s'",
                                                                            defaultString(searchAliasField.getReference()),
                                                                            field.getFieldType().getReference(),
                                                                            field.getCaseType().getReference()),
                                                              searchAliasField));
            }
        });

        return validationResult;
    }
}
