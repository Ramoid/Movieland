package com.solovey.movieland.dao.jdbc;


import com.solovey.movieland.dao.enums.SortDirection;
import com.solovey.movieland.entity.reporting.ReportType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
class QueryGenerator {
    public String addSorting(String sql, Map<String, SortDirection> sortType) {
        SortDirection ratingSort = sortType.get("rating");
        if (ratingSort != SortDirection.NOSORT) {
            return sql + " order by rating " + ratingSort.getDirection();
        }
        SortDirection priceSort = sortType.get("price");
        if (priceSort != SortDirection.NOSORT) {
            return sql + " order by price " + priceSort.getDirection();
        }
        return sql;
    }

    public String buildMoviesReportQuery(String sql, ReportType reportType) {
        if (reportType == ReportType.ADDED_DURING_PERIOD) {
            return sql + " where m.added_date between ? and ?" +
                    " group by m.movie_id";
        } else {
            return sql + " group by m.movie_id";
        }

    }
}
