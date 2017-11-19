package com.solovey.movieland.web.controller;


import com.solovey.movieland.entity.Genre;
import com.solovey.movieland.dao.enums.Currency;
import com.solovey.movieland.dao.enums.SortDirection;
import com.solovey.movieland.service.MovieService;
import com.solovey.movieland.web.controller.MovieController;
import com.solovey.movieland.web.util.currency.CurrencyService;
import com.solovey.movieland.web.util.dto.MovieDto;
import com.solovey.movieland.web.util.dto.ToDtoConverter;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import com.solovey.movieland.entity.Movie;
import com.solovey.movieland.web.util.security.entity.PrincipalUser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieControllerTest {

    @Before

    @Test
    public void shouldReturnAllMoviesList() throws Exception {
        List<Movie> actualMovies = createMovieList();
        List<MovieDto> actualMoviesDto = createMovieDtoList();
        String expectedJson = "{\"movieId\":1,\"nameRussian\":\"Test russian name\",\"nameNative\":\"Test native name\",\"yearOfRelease\":3000,\"rating\":1.1,\"price\":123.12,\"picturePath\":\"Test path\"}";

        Map<String, SortDirection> sortType = new HashMap<>();
        sortType.put("rating", SortDirection.ASC);
        sortType.put("price", SortDirection.NOSORT);

        MovieService mockService = mock(MovieService.class);
        when(mockService.getAllMovies(sortType)).thenReturn(actualMovies);

        JsonJacksonConverter mockConverter = mock(JsonJacksonConverter.class);
        when(mockConverter.convertAllMoviesToJson(actualMoviesDto)).thenReturn(expectedJson);

        ToDtoConverter mockToDtoConverter = mock(ToDtoConverter.class);
        when(mockToDtoConverter.convertMoviestoMoviesDto(actualMovies)).thenReturn(actualMoviesDto);

        CurrencyService mockCurrencyService = mock(CurrencyService.class);

        MovieController controller = new MovieController(mockService, mockConverter, mockToDtoConverter, mockCurrencyService);
        MockMvc mockMvc = standaloneSetup(controller).build();

        MvcResult result = mockMvc.perform(get("/movie?rating=asc"))
                .andReturn();
        String actualResult = result.getResponse().getContentAsString();
        //System.out.println(actualResult);
        assertEquals(expectedJson, actualResult);
    }

    @Test
    public void shouldReturnRandomMoviesList() throws Exception {
        List<Movie> actualMovies = createMovieList();
        List<MovieDto> actualMoviesDto = createMovieDtoList();
        String expectedJson = "[{\"movieId\":1,\"nameRussian\":\"Test russian name\",\"nameNative\":\"Test native name\"" +
                ",\"yearOfRelease\":3000,\"description\":\"Test Description\",\"rating\":1.1,\"price\":123.12," +
                "\"picturePath\":\"Test path\",\"genres\":[{\"id\":1,\"name\":\"Ужосы\"}]}]";

        MovieService mockService = mock(MovieService.class);
        when(mockService.getRandomMovies()).thenReturn(actualMovies);

        ToDtoConverter mockToDtoConverter = mock(ToDtoConverter.class);
        when(mockToDtoConverter.convertMoviestoMoviesDto(actualMovies)).thenReturn(actualMoviesDto);

        JsonJacksonConverter mockConverter = mock(JsonJacksonConverter.class);
        when(mockConverter.convertRandomMoviesToJson(actualMoviesDto)).thenReturn(expectedJson);

        CurrencyService mockCurrencyService = mock(CurrencyService.class);

        MovieController controller = new MovieController(mockService, mockConverter, mockToDtoConverter, mockCurrencyService);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/movie/random"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].movieId", is(1)))
                .andExpect(jsonPath("$[0].nameRussian", is("Test russian name")))
                .andExpect(jsonPath("$[0].nameNative", is("Test native name")))
                .andExpect(jsonPath("$[0].yearOfRelease", is(3000)))
                .andExpect(jsonPath("$[0].rating", is(1.1)))
                .andExpect(jsonPath("$[0].price", is(123.12)))
                .andExpect(jsonPath("$[0].description", is("Test Description")))
                .andExpect(jsonPath("$[0].genres[0].id", is(1)))
                .andExpect(jsonPath("$[0].genres[0].name", is("Ужосы")));
    }

    @Test
    public void shouldReturnMoviesListByGenreId() throws Exception {
        final int ID = 1;
        List<Movie> actualMovies = createMovieList();
        List<MovieDto> actualMoviesDto = createMovieDtoList();
        String expectedJson = "[{\"movieId\":1,\"nameRussian\":\"Test russian name\",\"nameNative\":\"Test native name\"" +
                ",\"yearOfRelease\":3000,\"description\":\"Test Description\",\"rating\":1.1,\"price\":123.12," +
                "\"picturePath\":\"Test path\"}]";

        Map<String, SortDirection> sortType = new HashMap<>();
        sortType.put("rating", SortDirection.ASC);
        sortType.put("price", SortDirection.NOSORT);

        MovieService mockService = mock(MovieService.class);
        when(mockService.getMoviesByGenreId(ID, sortType)).thenReturn(actualMovies);

        ToDtoConverter mockToDtoConverter = mock(ToDtoConverter.class);
        when(mockToDtoConverter.convertMoviestoMoviesDto(actualMovies)).thenReturn(actualMoviesDto);

        JsonJacksonConverter mockConverter = mock(JsonJacksonConverter.class);
        when(mockConverter.convertAllMoviesToJson(actualMoviesDto)).thenReturn(expectedJson);

        CurrencyService mockCurrencyService = mock(CurrencyService.class);

        MovieController controller = new MovieController(mockService, mockConverter, mockToDtoConverter, mockCurrencyService);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/movie/genre/" + ID + "?rating=asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].movieId", is(1)))
                .andExpect(jsonPath("$[0].nameRussian", is("Test russian name")))
                .andExpect(jsonPath("$[0].nameNative", is("Test native name")))
                .andExpect(jsonPath("$[0].yearOfRelease", is(3000)))
                .andExpect(jsonPath("$[0].rating", is(1.1)))
                .andExpect(jsonPath("$[0].price", is(123.12)));
    }

    @Test
    public void shouldReturnMovieByMovieId() throws Exception {
        Movie actualMovie = createMovieList().get(0);
        MovieDto actualMovieDto = createMovieDtoList().get(0);
        String expectedJson = "{\"movieId\":1,\"nameRussian\":\"Test russian name\",\"nameNative\":\"Test native name\"" +
                ",\"yearOfRelease\":3000,\"description\":\"Test Description\",\"rating\":1.1,\"price\":100.00," +
                "\"picturePath\":\"Test path\",\"genres\":[{\"id\":1,\"name\":\"Ужосы\"}],\"reviews\":[{\"id\":1,\"user\":{\"id\":1,\"nickname\":\"UnknownUser\"},\"text\":\"Review text\"}]}";

        double currencyRate = 26.00;

        MovieService mockService = mock(MovieService.class);
        when(mockService.getMovieById(1)).thenReturn(actualMovie);

        ToDtoConverter mockToDtoConverter = mock(ToDtoConverter.class);
        when(mockToDtoConverter.convertMovietoMovieDto(actualMovie)).thenReturn(actualMovieDto);

        JsonJacksonConverter mockConverter = mock(JsonJacksonConverter.class);
        when(mockConverter.convertMovieToJson(actualMovieDto)).thenReturn(expectedJson);

        CurrencyService mockCurrencyService = mock(CurrencyService.class);
        when(mockCurrencyService.getCurrencyRate(Currency.USD)).thenReturn(currencyRate);


        MovieController controller = new MovieController(mockService, mockConverter, mockToDtoConverter, mockCurrencyService);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/movie/1?currency=USD"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                //.andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.movieId", is(1)))
                .andExpect(jsonPath("$.nameRussian", is("Test russian name")))
                .andExpect(jsonPath("$.nameNative", is("Test native name")))
                .andExpect(jsonPath("$.yearOfRelease", is(3000)))
                .andExpect(jsonPath("$.rating", is(1.1)))
                .andExpect(jsonPath("$.price", is(100.00)))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.genres[0].id", is(1)))
                .andExpect(jsonPath("$.genres[0].name", is("Ужосы")));
    }

    @Test
    public void addMovieTest() throws Exception {
        Movie actualMovie = createMovieList().get(0);

        String actualJson = "{\"movieId\":1,\"nameRussian\":\"Test russian name\",\"nameNative\":\"Test native name\"" +
                ",\"yearOfRelease\":3000,\"description\":\"Test Description\",\"rating\":1.1,\"price\":100.00," +
                "\"picturePath\":\"Test path\",\"genres\":[{\"id\":1,\"name\":\"Ужосы\"}],\"reviews\":[{\"id\":1,\"user\":{\"id\":1,\"nickname\":\"UnknownUser\"},\"text\":\"Review text\"}]}";

        PrincipalUser principal = new PrincipalUser("test user", 1);

        MovieService mockService = mock(MovieService.class);
        when(mockService.addMovie(actualMovie)).thenReturn(actualMovie);

        ToDtoConverter mockToDtoConverter = mock(ToDtoConverter.class);

        JsonJacksonConverter mockConverter = mock(JsonJacksonConverter.class);
        when(mockConverter.jsonToObject(actualJson, Movie.class)).thenReturn(actualMovie);

        CurrencyService mockCurrencyService = mock(CurrencyService.class);


        MovieController controller = new MovieController(mockService, mockConverter, mockToDtoConverter, mockCurrencyService);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(post("/movie").
                principal(principal).
                contentType("application/json;charset=UTF-8").
                content(actualJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.movieId", is(1)))
                .andExpect(jsonPath("$.nameRussian", is("Test russian name")))
                .andExpect(jsonPath("$.nameNative", is("Test native name")))
                .andExpect(jsonPath("$.yearOfRelease", is(3000)))
                .andExpect(jsonPath("$.rating", is(1.1)))
                .andExpect(jsonPath("$.price", is(123.12)))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.genres[0].id", is(1)))
                .andExpect(jsonPath("$.genres[0].name", is("Ужосы")));

    }

    private List<Movie> createMovieList() {
        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Ужосы");
        genres.add(genre);
        List<Movie> movies = new ArrayList<>();
        Movie movie = new Movie();
        movie.setMovieId(1);
        movie.setNameRussian("Test russian name");
        movie.setNameNative("Test native name");
        movie.setDescription("Test Description");
        movie.setYearOfRelease(3000);
        movie.setPrice(123.12);
        movie.setRating(1.1);
        movie.setPicturePath("Test path");
        movie.setGenres(genres);
        movies.add(movie);
        return movies;
    }

    private List<MovieDto> createMovieDtoList() {
        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Ужосы");
        genres.add(genre);
        List<MovieDto> movies = new ArrayList<>();
        MovieDto movie = new MovieDto();
        movie.setMovieId(1);
        movie.setNameRussian("Test russian name");
        movie.setNameNative("Test native name");
        movie.setDescription("Test Description");
        movie.setYearOfRelease(3000);
        movie.setPrice(123.12);
        movie.setRating(1.1);
        movie.setPicturePath("Test path");
        movie.setGenres(genres);
        movies.add(movie);
        return movies;
    }
}
