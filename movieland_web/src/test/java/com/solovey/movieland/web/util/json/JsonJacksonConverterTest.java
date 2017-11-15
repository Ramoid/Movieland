package com.solovey.movieland.web.util.json;

import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.Review;
import com.solovey.movieland.entity.enums.Currency;
import com.solovey.movieland.web.util.auth.entity.LoginRequest;
import com.solovey.movieland.web.util.dto.MovieDto;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JsonJacksonConverterTest {


    @Test
    public void testParseMoviesToJson() {
        List<MovieDto> movies = createMovieList();
        String expectedJson = "[{\"movieId\":1,\"nameRussian\":\"Test russian name\",\"nameNative\":\"Test native name\"," +
                "\"yearOfRelease\":3000,\"rating\":1.1,\"price\":123.12,\"picturePath\":\"Test path\"}]";
        JsonJacksonConverter converter = new JsonJacksonConverter();

        Movie movie = new Movie();
        movie.setMovieId(1);
        movie.setNameRussian("Test russian name");
        movie.setNameNative("Test native name");
        movie.setYearOfRelease(3000);
        movie.setPrice(123.12);
        movie.setRating(1.1);
        movie.setPicturePath("Test path");
        String actualJson = converter.convertMoviesToJson(movies, "movieId", "nameRussian", "nameNative", "yearOfRelease",
                "rating", "price", "picturePath");

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testParseJsonToCurrencyRates() {
        String actualJson = "{\"USD\":{\"interbank\":{\"buy\":\"26.77\",\"sell\":\"26.79\"},\"nbu\":{\"buy\":\"26.63625\",\"sell\":\"26.63625\"}}," +
                "\"EUR\":{\"interbank\":{\"buy\":\"31.3797\",\"sell\":\"31.4032\"},\"nbu\":{\"buy\":\"30.87141\",\"sell\":\"30.87141\"}}," +
                "\"RUB\":{\"interbank\":{\"buy\":\"0.4653\",\"sell\":\"0.4662\"},\"nbu\":{\"buy\":\"0.44957\",\"sell\":\"0.44957\"}}}";
        try {
            InputStream jsonStream = new ByteArrayInputStream(actualJson.getBytes(StandardCharsets.UTF_8.name()));
            JsonJacksonConverter converter = new JsonJacksonConverter();
            Map<Currency, Double> currencyRateMap = new HashMap<>();
            converter.extractCurrencyRates(jsonStream, currencyRateMap);
            assertEquals(26.63625, currencyRateMap.get(Currency.USD), 0);
            assertEquals(30.87141, currencyRateMap.get(Currency.EUR), 0);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Test
    public void parseLoginJson(){
        String actualJson="{\"email\":\"ronald.reynolds66@example.com\",\"password\":\"paco\"}";
        JsonJacksonConverter converter = new JsonJacksonConverter();
        LoginRequest loginRequest = converter.parseLoginJson(actualJson);
        assertEquals("ronald.reynolds66@example.com", loginRequest.getEmail());
        assertEquals("paco", loginRequest.getPassword());

    }

    @Test
    public void testParseJsonToReview() {
        String actualJson = "{\"movieId\" : 1,\"text\" : \"Очень понравилось!\"}";
        int userId = 1;
        JsonJacksonConverter converter = new JsonJacksonConverter();
        Review review = converter.parseJsonToReview(actualJson, userId);
        assertEquals(1, review.getMovieId());
        assertEquals("Очень понравилось!", review.getText());
        assertEquals(1, review.getUser().getId());

    }

    private List<MovieDto> createMovieList() {
        List<MovieDto> movies = new ArrayList<>();
        MovieDto movie = new MovieDto();
        movie.setMovieId(1);
        movie.setNameRussian("Test russian name");
        movie.setNameNative("Test native name");
        movie.setYearOfRelease(3000);
        movie.setPrice(123.12);
        movie.setRating(1.1);
        movie.setPicturePath("Test path");
        movies.add(movie);
        return movies;
    }
}