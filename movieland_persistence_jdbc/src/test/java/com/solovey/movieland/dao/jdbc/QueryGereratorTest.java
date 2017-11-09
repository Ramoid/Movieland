package com.solovey.movieland.dao.jdbc;

import com.solovey.movieland.entity.enums.SortDirection;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class QueryGereratorTest {

    @Test
    public void testAddSorting() throws SQLException {
        String actualSql = "select rating from movie";
        String expectedSql = "select rating from movie order by price ASC";
        Map<String, SortDirection> sortType = new HashMap<>();
        sortType.put("price", SortDirection.ASC);
        sortType.put("rating", SortDirection.NOSORT);

        QueryGenerator generator = new QueryGenerator();
        assertEquals(generator.addSorting(actualSql, sortType), expectedSql);

    }
}