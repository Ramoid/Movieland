package com.solovey.movieland.dao.jdbc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class QueryConfig {

    @Bean
    public String getAllMoviesSql() {
        return "select m.movie_id,m.movie_name_russian,m.movie_name_native,m.movie_year,m.rating,m.price,m.link" +
                " from movies.movie m";
    }

    @Bean
    public String getMovieByIdSql() {
        return "select m.movie_id,m.movie_name_russian,m.movie_name_native,m.movie_year,m.description,m.rating,m.price,m.link," +
                "   GROUP_CONCAT(DISTINCT gma.genre_id ORDER BY gma.genre_id ASC SEPARATOR ',') AS genres_id," +
                "   GROUP_CONCAT(DISTINCT cma.country_id ORDER BY cma.country_id ASC SEPARATOR ',') AS countries_id" +
                " from movies.movie m, " +
                "      movies.genre_mapping gma," +
                "      movies.country_mapping cma" +
                " where  m.movie_id=gma.movie_id " +
                "   and m.movie_id=cma.movie_id " +
                "   and m.movie_id=?" +
                " group by m.movie_id";
    }

    @Bean
    public String getRandomMoviesSql() {
        return "select m.movie_id,m.movie_name_russian,m.movie_name_native,m.movie_year,m.description,m.rating,m.price,m.link," +
                "   GROUP_CONCAT(DISTINCT gma.genre_id ORDER BY gma.genre_id ASC SEPARATOR ',') AS genres_id," +
                "   GROUP_CONCAT(DISTINCT cma.country_id ORDER BY cma.country_id ASC SEPARATOR ',') AS countries_id" +
                " from movies.movie m, " +
                "      movies.genre_mapping gma," +
                "      movies.country_mapping cma" +
                " where m.movie_id=gma.movie_id " +
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
        return "select m.movie_id,m.movie_name_russian,m.movie_name_native,m.movie_year,m.rating,m.price,m.link" +
                " from movies.movie m, " +
                " movies.genre_mapping map" +
                " where m.movie_id=map.movie_id" +
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
    public String getUserByEmail() {
        return "select u.user_id,u.user_name,u.password,u.email " +
                "from movies.users u " +
                "where u.email=?";
    }

    @Bean
    public String insertReviewSql(){
        return "insert into movies.review(movie_id,user_id,rtext) " +
                "values(:movieId,:userId,:text)";
    }

    @Bean
    public String gerUserRolesSql(){
        return "select role_name " +
                "from movies.user_roles u, movies.roles r " +
                "where r.role_id=u.role_id" +
                "      and user_id=?";
    }

    @Bean
    public String insertMovieSql(){
        return "insert into movies.movie(movie_name_russian,movie_name_native,movie_year,description,rating,price,link) " +
                "values(:nameRussian,:nameNative,:movieYear,:descriprion,:rating,:price,:picturePath)";
    }

    @Bean
    public String insertGenreMappingSql(){
        return "insert ignore into movies.genre_mapping(movie_id,genre_id) " +
                "values(?,?)";
    }

    @Bean
    public String insertCountryMappingSql(){
        return "insert ignore into movies.country_mapping(movie_id,country_id) " +
                "values(?,?)";
    }

    @Bean
    public String updateMovieSql(){
        return "update movies.movie " +
                "set movie_name_russian=ifnull(:nameRussian,movie_name_russian)," +
                "movie_name_native=ifnull(:nameNative,movie_name_native)," +
                "movie_year=ifnull(:yearOfRelease,movie_year)," +
                "description=ifnull(:description,description)," +
                "rating=ifnull(:rating,rating)," +
                "price=ifnull(:price,price)," +
                "link=ifnull(:picturePath,link) " +
                "where movie_id=:movieId";
    }

    @Bean
    public String deleteGenreMappingsByIdsSql(){
        return "delete from movies.genre_mapping " +
                "where movie_id=:movieId " +
                "and genre_id not in (:genreIds)";
    }

    @Bean
    public String deleteCountryMappingsByIdsSql(){
        return "delete from movies.country_mapping " +
                "where movie_id=:movieId " +
                "and country_id not in (:countryIds)";
    }

    @Bean
    public String inserMovieRateSql(){
        return "insert ignore into movies.movie_rates(movie_id,user_id,rate) " +
                "values (?,?,?)";
    }

    @Bean
    public String getMoviesRatingsSql(){
        return "select movie_id,round(sum(rate)/count(*) ,1) rating,count(*) cnt " +
                "from  movies.movie_rates mr " +
                "group by movie_id";
    }

}
