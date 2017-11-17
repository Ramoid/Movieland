package com.solovey.movieland.web.controller;


import com.solovey.movieland.entity.Country;
import com.solovey.movieland.entity.Genre;
import com.solovey.movieland.service.CountryService;
import com.solovey.movieland.service.GenreService;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class CountryControllerTest {


    @Test
    public void shouldReturnAllCountriesList() throws Exception {
        List<Country> actualCountries = createCountryList();

        String expectedJson = "[{\"id\":1,\"name\":\"Ukraine\"}]";

        CountryService mockService = mock(CountryService.class);
        when(mockService.getAllGCountries()).thenReturn(actualCountries);

        CountryController controller = new CountryController(mockService);
        MockMvc mockMvc = standaloneSetup(controller).build();

        mockMvc.perform(get("/country"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Ukraine")));
    }

    private List<Country> createCountryList() {
        List<Country> countries = new ArrayList<>();
        Country country = new Country();
        country.setId(1);
        country.setName("Ukraine");
        countries.add(country);
        return countries;
    }


}
