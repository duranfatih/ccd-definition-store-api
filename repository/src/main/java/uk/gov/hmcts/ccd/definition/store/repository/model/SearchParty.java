package uk.gov.hmcts.ccd.definition.store.repository.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class SearchParty {

    private String caseTypeId;
    private Boolean searchPartyDoB;
    private Boolean searchPartyPostCode;
    private String searchPartyAddressLine1;
    private String searchPartyEmailAddress;
    private Date liveFrom;
    private Date liveTo;
    private String searchPartyName;

}
