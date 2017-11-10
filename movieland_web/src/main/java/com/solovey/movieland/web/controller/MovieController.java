package com.solovey.movieland.web.controller;

import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.entity.enums.SortDirection;
import com.solovey.movieland.service.MovieService;
import com.solovey.movieland.web.util.dto.ToDtoConverter;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value="/v1/movie" , method = GET, produces = "application/json;charset=UTF-8")
public class MovieController {
    private MovieService movieService;
    private JsonJacksonConverter jsonConverter;
    private ToDtoConverter toDtoConverter;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public MovieController(MovieService movieService, JsonJacksonConverter jsonConverter,
                           ToDtoConverter toDtoConverter) {
        this.movieService = movieService;
        this.jsonConverter = jsonConverter;
        this.toDtoConverter = toDtoConverter;

    }

    @RequestMapping()
    @ResponseBody
    public String getAllMovies(
            @RequestParam(value = "rating", defaultValue = "nosort") String rating,
            @RequestParam(value = "price", defaultValue = "nosort") String price) {
        log.info("Sending request to get all movies");
        long startTime = System.currentTimeMillis();

        Map<String, SortDirection> sortType = new HashMap<>();
        sortType.put("rating", SortDirection.getDirection(rating.toUpperCase()));
        sortType.put("price", SortDirection.getDirection(price.toUpperCase()));

        String jsonMovies = jsonConverter.convertAllMoviesToJson(
                toDtoConverter.convertMoviestoMoviesDto(movieService.getAllMovies(sortType)));
        log.info("Movies are received. It took {} ms", System.currentTimeMillis() - startTime);
        return jsonMovies;
    }

    @RequestMapping(value = "/random")
    @ResponseBody
    public String getRandomMovies() {
        log.info("Sending request to get random movies");
        long startTime = System.currentTimeMillis();
        String jsonMovies = jsonConverter.convertRandomMoviesToJson(
                toDtoConverter.convertMoviestoMoviesDto(movieService.getRandomMovies()));
        log.info("Random Movies are received. It took {} ms", System.currentTimeMillis() - startTime);
        return jsonMovies;
    }

    @RequestMapping(value = "/genre/{genreid}")
    @ResponseBody
    public String getMoviesByGenreId(@PathVariable int genreid,
                                     @RequestParam(value = "rating", defaultValue = "nosort") String rating,
                                     @RequestParam(value = "price", defaultValue = "nosort") String price) {
        log.info("Sending request to get movies by genre id {}", genreid);
        long startTime = System.currentTimeMillis();

        Map<String, SortDirection> sortType = new HashMap<>();
        sortType.put("rating", SortDirection.getDirection(rating));
        sortType.put("price", SortDirection.getDirection(price));

        String jsonMovies = jsonConverter.convertAllMoviesToJson(
                toDtoConverter.convertMoviestoMoviesDto(movieService.getMoviesByGenreId(genreid, sortType)));
        log.info("Movies are received. It took {} ms", System.currentTimeMillis() - startTime);
        return jsonMovies;
    }

    @RequestMapping(value = "/{movieId}")
    @ResponseBody
    public String getMovieById(@PathVariable int movieId,
                               @RequestParam(value = "currency", defaultValue = "UAH") String currency) {
        log.info("Sending request to get movie by movie id {}", movieId);
        long startTime = System.currentTimeMillis();

        Movie movie = movieService.getMovieById(movieId);

        String jsonMovie = jsonConverter.convertMovieToJson(toDtoConverter.convertMovietoMovieDto(movie));
        log.info("Movie are received. It took {} ms", System.currentTimeMillis() - startTime);
        return jsonMovie;
    }
}
