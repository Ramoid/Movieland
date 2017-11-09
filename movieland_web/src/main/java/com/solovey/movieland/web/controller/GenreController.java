package com.solovey.movieland.web.controller;

import com.solovey.movieland.service.GenreService;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("/v1/genre")
public class GenreController {
    private GenreService genreService;
    private JsonJacksonConverter jsonConverter;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public GenreController(GenreService movieService, JsonJacksonConverter jsonConverter
                           ) {
        this.genreService = movieService;
        this.jsonConverter = jsonConverter;
    }

    @RequestMapping(method = GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAllGenres() {
        log.info("Sending request to get all genres");
        long startTime = System.currentTimeMillis();
        String jsonMovies = jsonConverter.convertGenresListToJson(genreService.getAllGenres());
        log.info("Genres are received. It took {} ms", System.currentTimeMillis() - startTime);
        return jsonMovies;
    }


}
