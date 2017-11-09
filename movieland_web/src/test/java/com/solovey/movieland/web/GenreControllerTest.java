package com.solovey.movieland.web;


import com.solovey.movieland.entity.Genre;
import com.solovey.movieland.service.GenreService;
import com.solovey.movieland.web.controller.GenreController;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class GenreControllerTest {


    @Test
    public void shouldReturnAllGenresList() throws Exception {
        List<Genre> actualGenres = createGenreList();

        String expectedJson = "[{\"id\":1,\"name\":\"Ужосы\"}]";

        GenreService mockService = mock(GenreService.class);
        when(mockService.getAllGenres()).thenReturn(actualGenres);

        JsonJacksonConverter mockConverter = mock(JsonJacksonConverter.class);
        when(mockConverter.convertGenresListToJson(actualGenres)).thenReturn(expectedJson);

        GenreController controller = new GenreController(mockService, mockConverter);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Ужосы")));
    }

    private List<Genre> createGenreList() {
        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre();
        genre.setId(1);
        genre.setName("Ужосы");
        genres.add(genre);
        return genres;
    }


}
