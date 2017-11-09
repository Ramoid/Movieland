package com.solovey.movieland.web.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.solovey.movieland.entity.Genre;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.enums.Currency;
import com.solovey.movieland.web.util.dto.MovieDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class JsonJacksonConverter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();

    public Movie parseJsonToMovie(String json) {
        Movie movie = null;
        try {
            movie = objectMapper.readValue(json, Movie.class);
        } catch (IOException e) {
            log.error("Error parsing json {} to movie with error {}", json, e);
            throw new RuntimeException(e);
        }
        return movie;
    }

    public Map<Currency, Double> extractCurrencyRates(InputStream jsonStream, Map<Currency, Double> currencyRateMap) {
        log.info("Start retriveving currency rates from json ");
        long startTime = System.currentTimeMillis();

        try {
            JsonNode root = objectMapper.readTree(jsonStream);

            currencyRateMap.put(Currency.USD, root.at("/USD/nbu/sell").asDouble());
            currencyRateMap.put(Currency.EUR, root.at("/EUR/nbu/sell").asDouble());
            log.info("CurrencyRates is received from json. It took {} ms", System.currentTimeMillis() - startTime);
            return currencyRateMap;
        } catch (IOException e) {
            log.error("Error retriveving currency rates from json {}", e);
            throw new RuntimeException(e);
        }

    }

    public String convertMoviesToJson(List<MovieDto> movies, String... fields) {
        log.info("Start parsing movies to json ");
        long startTime = System.currentTimeMillis();
        String json = null;
        try {
            Set<String> properties = new HashSet<>(Arrays.asList(fields));

            SimpleBeanPropertyFilter movieFilter = SimpleBeanPropertyFilter
                    .serializeAll(properties);//.serializeAllExcept("description");
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter("movieFilter", movieFilter);
            json = objectMapper.writer(filters).writeValueAsString(movies);
            log.info("Movies json is received. It took {} ms", System.currentTimeMillis() - startTime);

        } catch (JsonProcessingException e) {
            log.error("Error parsing movies list {} with error {}", movies, e);
            throw new RuntimeException(e);
        }

        return json;
    }


    public String convertAllMoviesToJson(List<MovieDto> movies) {
        return convertMoviesToJson(movies
                , "movieId", "nameRussian", "nameNative", "yearOfRelease", "rating", "price", "picturePath");
    }

    public String convertRandomMoviesToJson(List<MovieDto> movies) {
        return convertMoviesToJson(movies
                , "movieId", "nameRussian", "nameNative", "yearOfRelease", "description", "rating", "price", "picturePath",
                "genres", "countries");
    }

    public String convertMovieToJson(MovieDto movieDto) {
        List<MovieDto> movieDtos = new ArrayList<>();
        movieDtos.add(movieDto);
        return convertMoviesToJson(movieDtos
                , "movieId", "nameRussian", "nameNative", "yearOfRelease", "description", "rating", "price", "picturePath",
                "genres", "countries", "reviews", "id", "nickname");
    }

    public String convertGenresListToJson(List<Genre> genres) {
        log.info("Start parsing movies to json ");
        long startTime = System.currentTimeMillis();
        String json = null;
        try {
            json = objectMapper.writer().writeValueAsString(genres);
            log.info("Jenres json is received. It took {} ms", System.currentTimeMillis() - startTime);
        } catch (JsonProcessingException e) {
            log.error("Error parsing genres list with error {}", e);
            throw new RuntimeException(e);
        }
        return json;

    }

}
