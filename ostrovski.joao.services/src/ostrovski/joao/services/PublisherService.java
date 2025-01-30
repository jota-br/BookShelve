package ostrovski.joao.services;

import org.hibernate.Session;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.helpers.ToUpperCase;
import ostrovski.joao.db.helpers.SessionExecute;
import ostrovski.joao.db.model.jpa.CountryJPA;
import ostrovski.joao.db.model.jpa.PublisherJPA;
import ostrovski.joao.db.model.jpa.UserJPA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PublisherService {

    public static List<PublisherJPA> processUpsertData(Session session, String publishersNames, String publishersCountries, int... ids) {

        if (publishersNames.isEmpty()) {
            Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            return null;
        }

        List<String> publishersNamesClean = Arrays.stream(publishersNames.split(","))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .map(ToUpperCase::firstLetters)
                .toList();
        List<String> publishersCountriesClean = Arrays.stream(publishersCountries.split(","))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .map(ToUpperCase::firstLetters)
                .toList();

        UserJPA user = session.find(UserJPA.class, UserSession.getInstance().getUserInSessionId());

        return PublisherService.updateOrInsert(session, publishersNamesClean, publishersCountriesClean, user, ids);
    }

    public static List<PublisherJPA> updateOrInsert(Session session, List<String> publishersNames, List<String> countries, UserJPA user, int... ids) {
        List<PublisherJPA> publisherList = new ArrayList<>();
        for (int i = 0; i < publishersNames.size(); i++) {
            String name = publishersNames.get(i);

            if (name.isEmpty()) {
                continue;
            }

            String country;

            CountryJPA countryPublisher = null;
            if (i < countries.size()) {
                country = countries.get(i);

                if (country != null && !country.isEmpty()) {
                    CountryJPA findCountry = SessionExecute.executeFind(session, country, CountryJPA.class, "countryName");
                    if (findCountry != null) {
                        countryPublisher = findCountry;
                    } else {
                        countryPublisher = new CountryJPA(country, user);
                    }
                }
            }

            PublisherJPA findAuthor;
            if (i < ids.length) {
                findAuthor = session.find(PublisherJPA.class, ids[i]);
                PublisherJPA exists = SessionExecute.executeFind(session, name, PublisherJPA.class, "publisherName");
                if (exists != null && exists.getPublisherId() != findAuthor.getPublisherId()) {
                    return null;
                }
            } else {
                findAuthor = SessionExecute.executeFind(session, name, PublisherJPA.class, "publisherName");
            }
            PublisherJPA publisherJPA = findAuthor != null ?
                    findAuthor.updatePublisherJPA(new PublisherJPA(name, countryPublisher, user)) :
                    new PublisherJPA(name, countryPublisher, user);
            SessionExecute.executeUpsert(session, publisherJPA);
            publisherList.add(publisherJPA);
        }
        return publisherList;
    }
}
