package uk.gov.hmcts.ccd.definition.store.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import uk.gov.hmcts.ccd.definition.store.repository.SecurityClassification;

public class CaseType implements HasAcls {
    private String id = null;
    private String description = null;
    private Version version = null;
    private String name = null;
    private Jurisdiction jurisdiction = null;
    private List<CaseEvent> events = new ArrayList<>();
    private List<CaseState> states = new ArrayList<>();
    @JsonProperty("case_fields")
    private List<CaseField> caseFields = new ArrayList<>();
    @JsonProperty("printable_document_url")
    private String printableDocumentsUrl;
    @JsonProperty("callback_get_case_url")
    private String callbackGetCaseUrl;
    @JsonProperty("retries_get_case_url")
    private List<Integer> retriesGetCaseUrl;
    @JsonProperty("security_classification")
    private SecurityClassification securityClassification;
    private List<AccessControlList> acls = new ArrayList<>();
    private final List<SearchAliasField> searchAliasFields = new ArrayList<>();
    private List<RoleToAccessProfiles> roleToAccessProfiles = new ArrayList<>();
    private List<SearchCriteria> searchCriterias = new ArrayList<>();
    private List<SearchParty> searchParties = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Jurisdiction getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(Jurisdiction jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public List<CaseEvent> getEvents() {
        return events;
    }

    public void setEvents(List<CaseEvent> events) {
        this.events = events;
    }

    public List<CaseState> getStates() {
        return states;
    }

    public void setStates(List<CaseState> states) {
        this.states = states;
    }

    public List<CaseField> getCaseFields() {
        return caseFields;
    }

    public void addCaseFields(List<CaseField> caseFields) {
        if (this.caseFields != null) {
            this.caseFields.addAll(caseFields);
        }
    }

    public void setCaseFields(List<CaseField> caseFields) {
        this.caseFields = caseFields;
    }

    public String getPrintableDocumentsUrl() {
        return printableDocumentsUrl;
    }

    public void setPrintableDocumentsUrl(String printableDocumentsUrl) {
        this.printableDocumentsUrl = printableDocumentsUrl;
    }

    public String getCallbackGetCaseUrl() {
        return callbackGetCaseUrl;
    }

    public void setCallbackGetCaseUrl(String callbackGetCaseUrl) {
        this.callbackGetCaseUrl = callbackGetCaseUrl;
    }

    public List<Integer> getRetriesGetCaseUrl() {
        return retriesGetCaseUrl == null ? Collections.emptyList() : retriesGetCaseUrl;
    }

    public void setRetriesGetCaseUrl(List<Integer> retriesGetCaseUrl) {
        this.retriesGetCaseUrl = retriesGetCaseUrl;
    }

    public SecurityClassification getSecurityClassification() {
        return securityClassification;
    }

    public void setSecurityClassification(SecurityClassification securityClassification) {
        this.securityClassification = securityClassification;
    }

    public List<AccessControlList> getAcls() {
        return acls;
    }

    @Override
    public void setAcls(List<AccessControlList> acls) {
        this.acls = acls;
    }

    public List<SearchAliasField> getSearchAliasFields() {
        return searchAliasFields;
    }

    public void setSearchAliasFields(List<SearchAliasField> searchAliasFields) {
        if (searchAliasFields != null) {
            this.searchAliasFields.addAll(searchAliasFields);
        }
    }

    public List<RoleToAccessProfiles> getRoleToAccessProfiles() {
        return roleToAccessProfiles;
    }

    public List<SearchCriteria> getSearchCriterias() {
        return searchCriterias;
    }

    public List<SearchParty> getSearchParties() {
        return searchParties;
    }
}
