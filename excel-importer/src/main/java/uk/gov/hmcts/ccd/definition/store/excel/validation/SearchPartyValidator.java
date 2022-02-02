package uk.gov.hmcts.ccd.definition.store.excel.validation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.definition.store.excel.endpoint.exception.InvalidImportException;
import uk.gov.hmcts.ccd.definition.store.excel.parser.ParseContext;
import uk.gov.hmcts.ccd.definition.store.excel.util.mapper.ColumnName;
import uk.gov.hmcts.ccd.definition.store.excel.util.mapper.SheetName;
import uk.gov.hmcts.ccd.definition.store.repository.entity.ComplexFieldEntity;
import uk.gov.hmcts.ccd.definition.store.repository.entity.FieldTypeEntity;
import uk.gov.hmcts.ccd.definition.store.repository.entity.SearchPartyEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class SearchPartyValidator {

    protected static final String NAME_FIELD_SEPARATOR = ",";
    private static final String COLLECTION_FIELD_TYPE = "Collection";
    private static final String ERROR_MESSAGE = "SearchPartyTab Invalid value '%s' "
        + "is not a valid SearchPartyCollectionFieldName value "
        + "as it does not reference a collection of a complex type.";

    @Autowired
    private final DotNotationValidator dotNotationValidator;

    public SearchPartyValidator(DotNotationValidator dotNotationValidator) {
        this.dotNotationValidator = dotNotationValidator;
    }

    public void validate(final List<SearchPartyEntity> searchPartyEntities,
                         final ParseContext parseContext) {

        searchPartyEntities.forEach(searchPartyEntity -> {
            String caseType = searchPartyEntity.getCaseType().getReference();

            if (StringUtils.isNoneBlank(searchPartyEntity.getSearchPartyCollectionFieldName())) {
                validateSearchPartyCollectionFieldName(parseContext, caseType, searchPartyEntity);

                validateDataType(parseContext, caseType, searchPartyEntity.getSearchPartyCollectionFieldName());
            } else {
                validateSearchPartyName(parseContext, caseType, searchPartyEntity);
                validateSearchPartyEmailAddress(parseContext, caseType, searchPartyEntity);
                validateSearchPartyAddressLine1(parseContext, caseType, searchPartyEntity);
                validateSearchPartyPostCode(parseContext, caseType, searchPartyEntity);
                validateSearchPartyDob(parseContext, caseType, searchPartyEntity);
                validateSearchPartyDod(parseContext, caseType, searchPartyEntity);
            }
        });

    }

    private void validateDataType(final ParseContext parseContext,
                                  final String caseType,
                                  final String searchPartyCollectionFieldValue) {
        final FieldTypeEntity caseFieldType = parseContext.getCaseFieldType(caseType, searchPartyCollectionFieldValue);
        final Set<ComplexFieldEntity> complexFields = Optional.ofNullable(caseFieldType.getCollectionFieldType())
            .map(FieldTypeEntity::getComplexFields)
            .orElse(Collections.emptySet());

        if (!COLLECTION_FIELD_TYPE.equals(caseFieldType.getBaseFieldType().getReference()) || complexFields.isEmpty()) {
            throw new InvalidImportException(String.format(ERROR_MESSAGE, searchPartyCollectionFieldValue));
        }
    }

    private void validateSearchPartyName(ParseContext parseContext,
                                         String caseType,
                                         SearchPartyEntity searchPartyEntity) {

        String spName = searchPartyEntity.getSearchPartyName();

        if (StringUtils.isNoneBlank(spName)) {
            // split CSV of fields
            Arrays.asList(spName.split(NAME_FIELD_SEPARATOR))
                .forEach(expression ->
                    validateDotNotation(parseContext, caseType, ColumnName.SEARCH_PARTY_NAME, expression.trim())
                );
        }
    }

    private void validateSearchPartyEmailAddress(ParseContext parseContext,
                                                 String caseType,
                                                 SearchPartyEntity searchPartyEntity) {

        String spEmailAddress = searchPartyEntity.getSearchPartyEmailAddress();

        if (StringUtils.isNoneBlank(spEmailAddress)) {
            validateDotNotation(parseContext,
                caseType,
                ColumnName.SEARCH_PARTY_EMAIL_ADDRESS,
                spEmailAddress
            );
        }
    }

    private void validateSearchPartyAddressLine1(ParseContext parseContext,
                                                 String caseType,
                                                 SearchPartyEntity searchPartyEntity) {

        String spAddressLine1 = searchPartyEntity.getSearchPartyAddressLine1();

        if (StringUtils.isNoneBlank(spAddressLine1)) {
            validateDotNotation(parseContext, caseType, ColumnName.SEARCH_PARTY_ADDRESS_LINE_1, spAddressLine1);
        }
    }

    private void validateSearchPartyPostCode(ParseContext parseContext,
                                             String caseType,
                                             SearchPartyEntity searchPartyEntity) {

        String spPostCode = searchPartyEntity.getSearchPartyPostCode();

        if (StringUtils.isNoneBlank(spPostCode)) {
            validateDotNotation(parseContext, caseType, ColumnName.SEARCH_PARTY_POST_CODE, spPostCode);
        }
    }

    private void validateSearchPartyDob(ParseContext parseContext,
                                             String caseType,
                                             SearchPartyEntity searchPartyEntity) {

        String searchPartyDob = searchPartyEntity.getSearchPartyDob();

        if (StringUtils.isNoneBlank(searchPartyDob)) {
            validateDotNotation(parseContext, caseType, ColumnName.SEARCH_PARTY_DOB, searchPartyDob);
        }
    }

    private void validateSearchPartyDod(ParseContext parseContext,
                                        String caseType,
                                        SearchPartyEntity searchPartyEntity) {

        String searchPartyDod = searchPartyEntity.getSearchPartyDod();

        if (StringUtils.isNoneBlank(searchPartyDod)) {
            validateDotNotation(parseContext, caseType, ColumnName.SEARCH_PARTY_DOD, searchPartyDod);
        }
    }

    private void validateSearchPartyCollectionFieldName(ParseContext parseContext,
                                         String caseType,
                                         SearchPartyEntity searchPartyEntity) {

        String spCollectionFieldValue = searchPartyEntity.getSearchPartyCollectionFieldName();

        if (StringUtils.isNoneBlank(spCollectionFieldValue)) {
            validateDotNotation(parseContext,
                caseType,
                ColumnName.SEARCH_PARTY_COLLECTION_FIELD_NAME,
                spCollectionFieldValue
            );
        }
    }

    private void validateDotNotation(ParseContext parseContext,
                                     String caseType,
                                     ColumnName columnName,
                                     String expression) {

        dotNotationValidator.validate(
            parseContext,
            SheetName.SEARCH_PARTY,
            columnName,
            caseType,
            expression
        );
    }

}
