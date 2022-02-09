package uk.gov.hmcts.ccd.definition.store.excel.validation;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.ccd.definition.store.excel.endpoint.exception.InvalidImportException;
import uk.gov.hmcts.ccd.definition.store.excel.parser.ParseContext;
import uk.gov.hmcts.ccd.definition.store.excel.util.mapper.ColumnName;
import uk.gov.hmcts.ccd.definition.store.excel.util.mapper.SheetName;
import uk.gov.hmcts.ccd.definition.store.repository.entity.ComplexFieldEntity;
import uk.gov.hmcts.ccd.definition.store.repository.entity.FieldTypeEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DotNotationValidator {

    protected static final String DOT_SEPARATOR = ".";

    // format: "{SheetName}Tab Invalid value '{expression}' is not a valid {ColumnName} value.... "
    protected static final String ERROR_MESSAGE = "%sTab Invalid value '%s' is not a valid %s value. "
        + "The expression dot notation values should be valid caseTypes fields. CaseTypeID = %s";

    public static final BiFunction<String, String, String[]> SPIT_FUNCTION = (expression, separator) -> {
        final String[] split = Optional.ofNullable(expression)
            .map(x -> x.split(Pattern.quote(separator), -1))
            .orElse(new String[0]);

        final List<String> filtered = Arrays.stream(split)
            .filter(x -> x.strip().length() > 0)
            .collect(Collectors.toUnmodifiableList());

        return filtered.toArray(String[]::new);
    };
    public static final Function<String, String[]> DOT_SEPARATOR_SPLIT_FUNCTION =
        expression -> SPIT_FUNCTION.apply(expression, DOT_SEPARATOR);

    public void validate(ParseContext parseContext,
                         SheetName sheetName,
                         ColumnName columnName,
                         String caseType,
                         String expression) {

        if (!expression.contains(DOT_SEPARATOR)) {
            getTopLevelField(parseContext, sheetName, columnName, caseType, expression);
        } else {
            checkDotNotationField(parseContext, sheetName, columnName, caseType, expression);
        }
    }

//    private Optional<ComplexFieldEntity> getComplexFieldEntity(Set<ComplexFieldEntity> complexFieldEntities,
//                                                               String currentAttribute) {
//        return complexFieldEntities.stream().filter(complexFieldEntity ->
//            complexFieldEntity.getReference().equals(currentAttribute)).findAny();
//    }

    public ComplexFieldEntity findComplexFieldEntity(final Set<ComplexFieldEntity> complexFieldEntities,
                                                     final String attribute,
                                                     final String caseType,
                                                     final SheetName sheetName,
                                                     final ColumnName columnName) {
        return complexFieldEntities.stream()
            .filter(complexFieldEntity -> complexFieldEntity.getReference().equals(attribute))
            .findAny()
            .orElseThrow(() -> new InvalidImportException(
                    String.format(ERROR_MESSAGE, sheetName, attribute, columnName, caseType)
                )
            );
    }

    private FieldTypeEntity getTopLevelField(ParseContext parseContext,
                                             SheetName sheetName,
                                             ColumnName columnName,
                                             String caseType,
                                             String expression) {
        try {
            return parseContext.getCaseFieldType(caseType, expression);

        } catch (Exception e) {
            throw new InvalidImportException(String.format(ERROR_MESSAGE, sheetName, expression, columnName, caseType));
        }
    }

    private void checkDotNotationField(ParseContext parseContext,
                                       SheetName sheetName,
                                       ColumnName columnName,
                                       String caseType,
                                       String expression) {

        try {
            // use split with -1 limit to force inclusion of empty values
            final String[] splitDotNotationExpression = DOT_SEPARATOR_SPLIT_FUNCTION.apply(expression);
            // NB: start from depth = 1 to ignore top level field that is already processed
            final List<String> aa = Arrays.asList(splitDotNotationExpression)
                .subList(1, splitDotNotationExpression.length);

            final FieldTypeEntity fieldType =
                getTopLevelField(parseContext, sheetName, columnName, caseType, splitDotNotationExpression[0]);

            final Set<ComplexFieldEntity> complexFieldsBelongingToParent = fieldType.getComplexFields();

            performCheck(aa.toArray(String[]::new), complexFieldsBelongingToParent, caseType, sheetName, columnName);

        } catch (InvalidImportException invalidImportException) {
            // throw a new Exception using original full expression (nb: previous exception already logged)
            throw new InvalidImportException(String.format(ERROR_MESSAGE, sheetName, expression, columnName, caseType));
        }
    }

    public void checkDotNotationField(final Set<ComplexFieldEntity> parentComplexFields,
                                      final String caseType,
                                      final SheetName sheetName,
                                      final ColumnName columnName,
                                      final String expression) {
        final String[] splitDotNotationExpression = DOT_SEPARATOR_SPLIT_FUNCTION.apply(expression);

        performCheck(splitDotNotationExpression, parentComplexFields, caseType, sheetName, columnName);
    }

    private void performCheck(final String[] splitDotNotationExpression,
                              final Set<ComplexFieldEntity> parentComplexFields,
                              final String caseType,
                              final SheetName sheetName,
                              final ColumnName columnName) {
        Set<ComplexFieldEntity> complexFieldsBelongingToParent = parentComplexFields;

        for (String currentAttribute : splitDotNotationExpression) {

            final ComplexFieldEntity result = findComplexFieldEntity(
                complexFieldsBelongingToParent,
                currentAttribute,
                caseType,
                sheetName,
                columnName
            );

            // search complexFields belonging to parent for current attribute
//            final Optional<ComplexFieldEntity> result =
//                getComplexFieldEntity(complexFieldsBelongingToParent, currentAttribute);
//
//            if (result.isEmpty()) {
//                throw new InvalidImportException(
//                    String.format(ERROR_MESSAGE, sheetName, currentAttribute, columnName, caseType)
//                );
//            } else {
                // update fields list based on new field type
                complexFieldsBelongingToParent = result.getFieldType().getComplexFields();
//            }
        }
    }

}
