package ostrovski.joao.db.helpers;

import ostrovski.joao.common.model.dto.*;
import ostrovski.joao.db.model.jpa.*;

import java.util.ArrayList;
import java.util.List;

public class TransformToDTO {

    public static AuthorDTO transformToAuthorDTO(AuthorJPA e) {

        if (e == null) {
            return null;
        }

        CountryDTO country = transformToCountryDTO(e.getCountry());
        UserDTO user = transformToUserDTO(e.getLastUpdatedBy());

        return new AuthorDTO(e.getAuthorId(), e.getAuthorName(), e.getDateOfBirth(),
                country, user, e.getLastUpdated());
    }

    public static BookDTO transformToBookDTO(BookJPA e) {

        if (e == null) {
            return null;
        }

        UserDTO user = transformToUserDTO(e.getLastUpdatedBy());
        List<CategoryDTO> categories = new ArrayList<>();

        for (CategoryJPA category : e.getCategories()) {
            if (category == null) {
                continue;
            }
            categories.add(transformToCategoryDTO(category));
        }

        List <PublisherDTO> publishers = new ArrayList<>();
        for (PublisherJPA publisher : e.getPublishers()) {
            if (publisher == null) {
                continue;
            }
            publishers.add(transformToPublisherDTO(publisher));
        }

        List<AuthorDTO> authors = new ArrayList<>();

        for (AuthorJPA author : e.getAuthors()) {
            if (author == null) {
                continue;
            }
            authors.add(transformToAuthorDTO(author));
        }

        return new BookDTO(e.getBookId(), e.getTitle(), authors, e.getReleaseDate(),
                e.getDescription(), user, e.getLastUpdated(), categories,
                publishers);
    }

    public static CategoryDTO transformToCategoryDTO(CategoryJPA e) {

        if (e == null) {
            return null;
        }

        UserDTO user = transformToUserDTO(e.getLastUpdatedBy());

        return new CategoryDTO(e.getCategoryId(), e.getCategoryName(), user,
                e.getLastUpdated());
    }

    public static CountryDTO transformToCountryDTO(CountryJPA e) {

        if (e == null) {
            return null;
        }

        UserDTO user = transformToUserDTO(e.getLastUpdatedBy());

        return new CountryDTO(e.getCountryId(), e.getCountryName(), user,
                e.getLastUpdated());
    }

    public static PublisherDTO transformToPublisherDTO(PublisherJPA e) {

        if (e == null) {
            return null;
        }

        UserDTO user = transformToUserDTO(e.getLastUpdatedBy());
        CountryDTO country = transformToCountryDTO(e.getCountry());

        return new PublisherDTO(e.getPublisherId(), e.getPublisherName(), country, user,
                e.getLastUpdated());
    }

    public static ProfileDTO transformToProfileDTO(ProfileJPA e) {

        if (e == null) {
            return null;
        }

        return new ProfileDTO(e.getProfileId(), e.getProfileName(), e.isActive(),
                e.getLastUpdated());
    }

    public static UserDTO transformToUserDTO(UserJPA e) {

        if (e == null) {
            return null;
        }

        ProfileDTO profile = transformToProfileDTO(e.getProfile());

        return new UserDTO(e.getUserId(), e.getLogin(), e.getHash(), e.getSalt(), profile,
                e.isActive(), e.getLastUpdated());
    }
}
