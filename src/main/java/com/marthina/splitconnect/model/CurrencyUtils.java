package com.marthina.splitconnect.model;

import com.marthina.splitconnect.model.enums.Country;
import com.marthina.splitconnect.model.enums.Currency;

import java.util.EnumMap;
import java.util.Map;

public class CurrencyUtils {

    private static final Map<Country, Currency> COUNTRY_TO_CURRENCY = new EnumMap<>(Country.class);

    static {
        // Américas
        COUNTRY_TO_CURRENCY.put(Country.BR, Currency.BRL);
        COUNTRY_TO_CURRENCY.put(Country.US, Currency.USD);
        COUNTRY_TO_CURRENCY.put(Country.CA, Currency.CAD);
        COUNTRY_TO_CURRENCY.put(Country.MX, Currency.MXN);
        COUNTRY_TO_CURRENCY.put(Country.AR, Currency.ARS);
        COUNTRY_TO_CURRENCY.put(Country.CL, Currency.CLP);
        COUNTRY_TO_CURRENCY.put(Country.PE, Currency.PEN);
        COUNTRY_TO_CURRENCY.put(Country.CO, Currency.COP);
        COUNTRY_TO_CURRENCY.put(Country.UY, Currency.UYU);
        COUNTRY_TO_CURRENCY.put(Country.BO, Currency.BOB);

        // Europa
        COUNTRY_TO_CURRENCY.put(Country.GB, Currency.GBP);
        COUNTRY_TO_CURRENCY.put(Country.DE, Currency.EUR);
        COUNTRY_TO_CURRENCY.put(Country.FR, Currency.EUR);
        COUNTRY_TO_CURRENCY.put(Country.IT, Currency.EUR);
        COUNTRY_TO_CURRENCY.put(Country.ES, Currency.EUR);
        COUNTRY_TO_CURRENCY.put(Country.PT, Currency.EUR);
        COUNTRY_TO_CURRENCY.put(Country.NL, Currency.EUR);
        COUNTRY_TO_CURRENCY.put(Country.BE, Currency.EUR);
        COUNTRY_TO_CURRENCY.put(Country.DK, Currency.DKK);
        COUNTRY_TO_CURRENCY.put(Country.PL, Currency.PLN);
        COUNTRY_TO_CURRENCY.put(Country.HU, Currency.HUF);
        COUNTRY_TO_CURRENCY.put(Country.CZ, Currency.CZK);
        COUNTRY_TO_CURRENCY.put(Country.RO, Currency.RON);
        COUNTRY_TO_CURRENCY.put(Country.CH, Currency.CHF);
        COUNTRY_TO_CURRENCY.put(Country.SE, Currency.SEK);
        COUNTRY_TO_CURRENCY.put(Country.NO, Currency.NOK);
        COUNTRY_TO_CURRENCY.put(Country.TR, Currency.TRY);
        COUNTRY_TO_CURRENCY.put(Country.LU, Currency.EUR);

        // Ásia
        COUNTRY_TO_CURRENCY.put(Country.RU, Currency.RUB);
        COUNTRY_TO_CURRENCY.put(Country.JP, Currency.JPY);
        COUNTRY_TO_CURRENCY.put(Country.CN, Currency.CNY);
        COUNTRY_TO_CURRENCY.put(Country.TW, Currency.TWD);
        COUNTRY_TO_CURRENCY.put(Country.IN, Currency.INR);
        COUNTRY_TO_CURRENCY.put(Country.BD, Currency.BDT);
        COUNTRY_TO_CURRENCY.put(Country.PK, Currency.PKR);
        COUNTRY_TO_CURRENCY.put(Country.VN, Currency.VND);
        COUNTRY_TO_CURRENCY.put(Country.KR, Currency.KRW);
        COUNTRY_TO_CURRENCY.put(Country.SG, Currency.SGD);
        COUNTRY_TO_CURRENCY.put(Country.HK, Currency.HKD);
        COUNTRY_TO_CURRENCY.put(Country.TH, Currency.THB);
        COUNTRY_TO_CURRENCY.put(Country.ID, Currency.IDR);
        COUNTRY_TO_CURRENCY.put(Country.PH, Currency.PHP);
        COUNTRY_TO_CURRENCY.put(Country.MY, Currency.MYR);

        // Oriente Médio
        COUNTRY_TO_CURRENCY.put(Country.AE, Currency.AED);
        COUNTRY_TO_CURRENCY.put(Country.SA, Currency.SAR);
        COUNTRY_TO_CURRENCY.put(Country.QA, Currency.QAR);
        COUNTRY_TO_CURRENCY.put(Country.KW, Currency.KWD);
        COUNTRY_TO_CURRENCY.put(Country.BH, Currency.BHD);
        COUNTRY_TO_CURRENCY.put(Country.OMR, Currency.OMR);
        COUNTRY_TO_CURRENCY.put(Country.IL, Currency.ILS);

        // Oceania
        COUNTRY_TO_CURRENCY.put(Country.AU, Currency.AUD);
        COUNTRY_TO_CURRENCY.put(Country.NZ, Currency.NZD);

        // África
        COUNTRY_TO_CURRENCY.put(Country.ZA, Currency.ZAR);
        COUNTRY_TO_CURRENCY.put(Country.NG, Currency.NGN);
        COUNTRY_TO_CURRENCY.put(Country.KE, Currency.KES);
        COUNTRY_TO_CURRENCY.put(Country.EG, Currency.EGP);
        COUNTRY_TO_CURRENCY.put(Country.MA, Currency.MAD);
    }

    public static Currency getCurrencyByCountry(Country country) {
        return COUNTRY_TO_CURRENCY.getOrDefault(country, Currency.OTHER);
    }
}
