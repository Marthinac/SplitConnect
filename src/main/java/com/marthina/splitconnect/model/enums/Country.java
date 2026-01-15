package com.marthina.splitconnect.model.enums;

public enum Country {
    BR("Brazil"),
    US("United States"),
    CA("Canada"),
    MX("Mexico"),
    AR("Argentina"),
    CL("Chile"),
    PE("Peru"),
    CO("Colombia"),
    UY("Uruguay"),
    BO("Bolivia"),
    GB("United Kingdom"),
    DE("Germany"),
    FR("France"),
    IT("Italy"),
    ES("Spain"),
    PT("Portugal"),
    NL("Netherlands"),
    BE("Belgium"),
    DK("Denmark"),
    PL("Poland"),
    HU("Hungary"),
    CZ("Czech Republic"),
    RO("Romania"),
    CH("Switzerland"),
    LU("Luxembourg"),
    SE("Sweden"),
    NO("Norway"),
    TR("Turkey"),
    JP("Japan"),
    CN("China"),
    TW("Taiwan"),
    IN("India"),
    BD("Bangladesh"),
    PK("Pakistan"),
    VN("Vietnam"),
    KR("South Korea"),
    SG("Singapore"),
    HK("Hong Kong"),
    TH("Thailand"),
    ID("Indonesia"),
    PH("Philippines"),
    MY("Malaysia"),
    AE("United Arab Emirates"),
    SA("Saudi Arabia"),
    QA("Qatar"),
    KW("Kuwait"),
    BH("Bahrain"),
    OMR("Oman"),
    IL("Israel"),
    AU("Australia"),
    NZ("New Zealand"),
    RU("Russia"),
    ZA("South Africa"),
    NG("Nigeria"),
    KE("Kenya"),
    EG("Egypt"),
    MA("Morocco");

    private final String fullName;

    Country(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
