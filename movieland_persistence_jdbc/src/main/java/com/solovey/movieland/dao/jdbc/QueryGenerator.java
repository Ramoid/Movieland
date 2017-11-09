package com.solovey.movieland.dao.jdbc;


import com.solovey.movieland.entity.enums.SortDirection;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QueryGenerator {
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
}
