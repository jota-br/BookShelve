package ostrovski.joao.ui.helpers;

import org.hibernate.Session;
import ostrovski.joao.common.helpers.Logger;
import ostrovski.joao.db.helpers.GetSessionJPA;
import ostrovski.joao.db.helpers.PaginatedQuery;

import java.util.Map;

public class UpdatePage {

    public static Map<String, Integer> updateCurrentPage(Map<String, Integer> paginationConfig, int value, boolean setCurrentPage) {
        int newPageValue = paginationConfig.get("currentPage") + value;
        if (setCurrentPage) {
            newPageValue = (value > 0) ? value : newPageValue;
        }

        if (newPageValue > paginationConfig.get("totalPages")) {
            newPageValue = paginationConfig.get("currentPage");
        }

        paginationConfig = Map.of(
                "currentPage", newPageValue,
                "itemsPerPage", paginationConfig.get("itemsPerPage"),
                "totalPages", paginationConfig.get("totalPages")
        );
        return paginationConfig;
    }

    public static Map<String, Integer> getPagesAndRecordsCount(Map<String, String> queryFilters, Map<String, Integer> pageConfig) {

        Session session = null;
        try {
            session = GetSessionJPA.getSessionFactory().openSession();

            Map<String, Integer> pageDetail = PaginatedQuery.queryCount(session, pageConfig.get("itemsPerPage"), queryFilters.get("currentFilter"),
                    queryFilters.get("bookInstanceVariable"), queryFilters.get("requestedEntityQueryColumn"));

            return Map.of(
                    "currentPage", pageConfig.get("currentPage"),
                    "itemsPerPage", pageConfig.get("itemsPerPage"),
                    "totalPages", pageDetail.get("totalPages")
            );
        } catch (Exception e) {
            Logger.log(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return null;
    }
}