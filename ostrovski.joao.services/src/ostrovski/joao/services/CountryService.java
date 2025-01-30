package ostrovski.joao.services;

import org.hibernate.Session;
import ostrovski.joao.db.helpers.SessionExecute;
import ostrovski.joao.db.model.jpa.CountryJPA;
import ostrovski.joao.db.model.jpa.UserJPA;

import java.util.ArrayList;
import java.util.List;

public class CountryService {

    public static List<CountryJPA> updateOrInsert(Session session, List<String> countriesNames, UserJPA user) {
        List<CountryJPA> countryList = new ArrayList<>();
        for (String country : countriesNames) {
            CountryJPA findCountry = SessionExecute.executeFind(session, country, CountryJPA.class, "countryName");
            CountryJPA countryJPA;
            countryJPA = findCountry != null ?
                    findCountry : new CountryJPA(country, user);
            SessionExecute.executeUpsert(session, countryJPA);
            countryList.add(countryJPA);
        }
        return countryList;
    }
}
