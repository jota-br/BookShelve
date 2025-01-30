package ostrovski.joao.services;

import org.hibernate.Session;
import ostrovski.joao.common.helpers.ExceptionMessage;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.common.helpers.ToUpperCase;
import ostrovski.joao.db.helpers.SessionExecute;
import ostrovski.joao.db.model.jpa.CategoryJPA;
import ostrovski.joao.db.model.jpa.UserJPA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryService {

    public static List<CategoryJPA> processUpsertData(Session session, String categoriesString, int... ids) {

        if (categoriesString.isEmpty()) {
            Logger.log(new IllegalArgumentException(ExceptionMessage.INVALID_USER_INPUT.getMessage()));
            return null;
        }

        List<String> categoriesClean = Arrays.stream(categoriesString.split(","))
                .filter(s -> !s.isEmpty())
                .map(String::trim)
                .map(ToUpperCase::firstLetters)
                .toList();

        UserJPA user = session.find(UserJPA.class, UserSession.getInstance().getUserInSessionId());
        return CategoryService.updateOrInsert(session, categoriesClean, user, ids);
    }

    public static List<CategoryJPA> updateOrInsert(Session session, List<String> categories, UserJPA user, int... ids) {
        List<CategoryJPA> categoryList = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {

            String category = categories.get(i);

            CategoryJPA findCategory;
            if (i < ids.length) {
                findCategory = session.find(CategoryJPA.class, ids[i]);
                CategoryJPA exists = SessionExecute.executeFind(session, category, CategoryJPA.class, "categoryName");
                if (exists != null && exists.getCategoryId() != findCategory.getCategoryId()) {
                    return null;
                }
            } else {
                findCategory = SessionExecute.executeFind(session, category, CategoryJPA.class, "categoryName");
            }
            CategoryJPA categoryJPA;
            categoryJPA = findCategory != null ?
                    findCategory.updateCategoryJPA(new CategoryJPA(category, user)) : new CategoryJPA(category, user);
            SessionExecute.executeUpsert(session, categoryJPA);
            categoryList.add(categoryJPA);
        }
        return categoryList;
    }
}
