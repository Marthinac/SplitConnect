package com.marthina.splitconnect.model.enums;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum Country {
    BR("Brazil", Currency.BRL),
    US("United States", Currency.USD),
    CA("Canada", Currency.CAD),
    MX("Mexico", Currency.MXN),
    AR("Argentina", Currency.ARS),
    CL("Chile", Currency.CLP),
    PE("Peru", Currency.PEN),
    CO("Colombia", Currency.COP),
    UY("Uruguay", Currency.UYU),
    BO("Bolivia", Currency.BOB),
    GB("United Kingdom", Currency.GBP),
    DE("Germany", Currency.EUR),
    FR("France", Currency.EUR),
    IT("Italy", Currency.EUR),
    ES("Spain", Currency.EUR),
    PT("Portugal", Currency.EUR),
    NL("Netherlands", Currency.EUR),
    BE("Belgium", Currency.EUR),
    DK("Denmark", Currency.DKK),
    PL("Poland", Currency.PLN),
    HU("Hungary", Currency.HUF),
    CZ("Czech Republic", Currency.CZK),
    RO("Romania", Currency.RON),
    CH("Switzerland", Currency.CHF),
    LU("Luxembourg", Currency.EUR),
    SE("Sweden", Currency.SEK),
    NO("Norway", Currency.NOK),
    TR("Turkey", Currency.TRY),
    JP("Japan", Currency.JPY),
    CN("China", Currency.CNY),
    TW("Taiwan", Currency.TWD),
    IN("India", Currency.INR),
    BD("Bangladesh", Currency.BDT),
    PK("Pakistan", Currency.PKR),
    VN("Vietnam", Currency.VND),
    KR("South Korea", Currency.KRW),
    SG("Singapore", Currency.SGD),
    HK("Hong Kong", Currency.HKD),
    TH("Thailand", Currency.THB),
    ID("Indonesia", Currency.IDR),
    PH("Philippines", Currency.PHP),
    MY("Malaysia", Currency.MYR),
    AE("United Arab Emirates", Currency.AED),
    SA("Saudi Arabia", Currency.SAR),
    QA("Qatar", Currency.QAR),
    KW("Kuwait", Currency.KWD),
    BH("Bahrain", Currency.BHD),
    OMR("Oman", Currency.OMR),
    IL("Israel", Currency.ILS),
    AU("Australia", Currency.AUD),
    NZ("New Zealand", Currency.NZD),
    RU("Russia", Currency.RUB),
    ZA("South Africa", Currency.ZAR),
    NG("Nigeria", Currency.NGN),
    KE("Kenya", Currency.KES),
    EG("Egypt", Currency.EGP),
    MA("Morocco", Currency.MAD);

    private final String fullName;
    private final Currency currency;  // ← SEU ENUM!

    Country(String fullName, Currency currency) {
        this.fullName = fullName;
        this.currency = currency;
    }

    public Locale getLocale() {
        return switch (this) {
            case BR -> Locale.forLanguageTag("pt-BR");
            case US -> Locale.US;
            case DE -> Locale.forLanguageTag("de-DE");
            case GB -> Locale.UK;
            case FR -> Locale.FRANCE;
            case ES -> Locale.forLanguageTag("es-ES");
            case IT -> Locale.ITALY;
            case PT -> Locale.forLanguageTag("pt-PT");
            case MX -> Locale.forLanguageTag("es-MX");
            case AR -> Locale.forLanguageTag("es-AR");
            case IN -> Locale.forLanguageTag("hi-IN");
            case JP -> Locale.JAPAN;
            case CN -> Locale.forLanguageTag("zh-CN");
            default -> Locale.ENGLISH;
        };
    }
}

