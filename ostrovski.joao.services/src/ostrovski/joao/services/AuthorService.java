package ostrovski.joao.services;

import org.hibernate.Session;
import ostrovski.joao.common.helpers.DateChecker;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.helpers.ToUpperCase;
import ostrovski.joao.db.helpers.SessionExecute;
import ostrovski.joao.db.model.jpa.AuthorJPA;
import ostrovski.joao.db.model.jpa.CountryJPA;
import ostrovski.joao.db.model.jpa.UserJPA;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthorService {

    public static List<AuthorJPA> processUpsertData(Session session, String authorsNames, String authorsDatesOfBirth, String authorsCountries, int... ids) {

        if (authorsNames.isEmpty()) {
            Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            return null;
        }

        List<String> authorsNamesClean = Arrays.stream(authorsNames.split(","))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .map(ToUpperCase::firstLetters)
                .toList();
        List<String> authorsDatesOfBirthClean = Arrays.stream(authorsDatesOfBirth.split(","))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .filter(DateChecker::dateChecker)
                .toList();
        List<String> authorsCountriesClean = Arrays.stream(authorsCountries.split(","))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .map(ToUpperCase::firstLetters)
                .toList();

        UserJPA user = session.find(UserJPA.class, UserSession.getInstance().getUserInSessionId());

        return AuthorService.updateOrInsert(session, authorsNamesClean, authorsDatesOfBirthClean, authorsCountriesClean, user, ids);
    }

    public static List<AuthorJPA> updateOrInsert(Session session, List<String> authors, List<String> dobs, List<String> countries, UserJPA user, int... ids) {
        List<AuthorJPA> authorsList = new ArrayList<>();
        for (int i = 0; i < authors.size(); i++) {
            String name = authors.get(i);

            if (name.isEmpty()) {
                continue;
            }

            LocalDate dob = null;
            String country;

            if (i < dobs.size()) {
                if (dobs.get(i) != null && !dobs.get(i).equals("_")) {
                    dob = LocalDate.parse(dobs.get(i));
                }
            }

            CountryJPA countryAuthor = null;
            if (i < countries.size()) {
                country = countries.get(i).equals("_") ? "" : countries.get(i);

                if (country != null && !country.isEmpty()) {
                    CountryJPA findCountry = SessionExecute.executeFind(session, country, CountryJPA.class, "countryName");
                    if (findCountry != null) {
                        countryAuthor = findCountry;
                    } else {
                        countryAuthor = new CountryJPA(country, user);
                    }
                }
            }

            AuthorJPA findAuthor;
            if (i < ids.length) {
                findAuthor = session.find(AuthorJPA.class, ids[i]);
                AuthorJPA exists = SessionExecute.executeFind(session, name, AuthorJPA.class, "authorName");
                if (exists != null && exists.getAuthorId() != findAuthor.getAuthorId()) {
                    return null;
                }
            } else {
                findAuthor = SessionExecute.executeFind(session, name, AuthorJPA.class, "authorName");
            }
            AuthorJPA authorJPA = findAuthor != null ?
                    findAuthor.updateAuthorJPA(new AuthorJPA(name, dob, countryAuthor, user)) :
                    new AuthorJPA(name, dob, countryAuthor, user);

            SessionExecute.executeUpsert(session, authorJPA);
            authorsList.add(authorJPA);
        }
        return authorsList;
    }
}
