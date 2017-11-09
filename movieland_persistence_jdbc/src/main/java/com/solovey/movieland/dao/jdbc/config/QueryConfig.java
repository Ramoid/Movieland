package com.solovey.movieland.dao.jdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryConfig {

    @Bean
    public String getAllMoviesSql() {
        return "select m.movie_id,m.movie_name_russian,m.movie_name_native,m.movie_year,m.rating,m.price,p.link" +
                " from movies.movie m, movies.poster p" +
                " where m.movie_id=p.movie_id";
    }

    @Bean
    public String getMovieByIdSql() {
        return "select m.movie_id,m.movie_name_russian,m.movie_name_native,m.movie_year,m.description,m.rating,m.price,p.link," +
                "   GROUP_CONCAT(DISTINCT gma.genre_id ORDER BY gma.genre_id ASC SEPARATOR ',') AS genres_id," +
                "   GROUP_CONCAT(DISTINCT cma.country_id ORDER BY cma.country_id ASC SEPARATOR ',') AS countries_id" +
                " from movies.movie m, " +
                "      movies.poster p," +
                "      movies.genre_mapping gma," +
                "      movies.country_mapping cma" +
                " where m.movie_id=p.movie_id" +
                "   and m.movie_id=gma.movie_id " +
                "   and m.movie_id=cma.movie_id " +
                "   and m.movie_id=?" +
                " group by m.movie_id";
    }

    @Bean
    public String getRandomMoviesSql() {
        return "select m.movie_id,m.movie_name_russian,m.movie_name_native,m.movie_year,m.description,m.rating,m.price,p.link," +
                "   GROUP_CONCAT(DISTINCT gma.genre_id ORDER BY gma.genre_id ASC SEPARATOR ',') AS genres_id," +
                "   GROUP_CONCAT(DISTINCT cma.country_id ORDER BY cma.country_id ASC SEPARATOR ',') AS countries_id" +
                " from movies.movie m, " +
                "      movies.poster p," +
                "      movies.genre_mapping gma," +
                "      movies.country_mapping cma" +
                " where m.movie_id=p.movie_id" +
                "   and m.movie_id=gma.movie_id " +
                "   and m.movie_id=cma.movie_id " +
                " group by m.movie_id  " +
                " order by rand() limit 3";
    }

    @Bean
    public String getAllGenresSql() {
        return "select g.genre_id,g.genre_name " +
                "from movies.genre g";
    }

    @Bean
    public String getAllCountriesSql() {
        return "select g.country_id,g.country " +
                "from movies.country g";
    }

    @Bean
    public String getMoviesByGenreIdSql() {
        return "select m.movie_id,m.movie_name_russian,m.movie_name_native,m.movie_year,m.rating,m.price,p.link" +
                " from movies.movie m, " +
                " movies.poster p," +
                " movies.genre_mapping map" +
                " where m.movie_id=p.movie_id" +
                " and m.movie_id=map.movie_id" +
                " and map.genre_id=?";
    }

    @Bean
    public String getReviewsByMovieIdSql() {
        return "select r.review_id,r.user_id,u.user_name,r.rtext\n" +
                "from  movies.review r," +
                "      movies.users u " +
                "where r.user_id=u.user_id" +
                "      and r.movie_id=?";
    }

    @Bean
    public String getuserByEmail() {
        return "select u.user_id,u.user_name " +
                "from movies.users u " +
                "where u.password=?" +
                " and u.email=?";
    }
}
