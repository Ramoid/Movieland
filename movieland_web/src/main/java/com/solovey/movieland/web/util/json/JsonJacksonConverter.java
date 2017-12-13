package com.solovey.movieland.web.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.solovey.movieland.entity.Review;
import com.solovey.movieland.entity.User;
import com.solovey.movieland.dao.enums.Currency;
import com.solovey.movieland.entity.reporting.Report;
import com.solovey.movieland.entity.reporting.ReportOutputType;
import com.solovey.movieland.entity.reporting.ReportState;
import com.solovey.movieland.entity.reporting.ReportType;
import com.solovey.movieland.web.security.entity.LoginRequest;
import com.solovey.movieland.web.security.entity.PrincipalUser;
import com.solovey.movieland.web.security.exceptions.BadLoginRequestException;
import com.solovey.movieland.web.util.dto.MovieDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JsonJacksonConverter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Pattern datePattern = Pattern.compile("^[\\d]{4}-[\\d]{2}-[\\d]{2}$");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

    private ObjectMapper objectMapper = new ObjectMapper();


    public void extractCurrencyRates(InputStream jsonStream, Map<Currency, Double> currencyRateMap) {
        log.info("Start retriveving currency rates from json ");
        long startTime = System.currentTimeMillis();

        try {
            JsonNode root = objectMapper.readTree(jsonStream);

            currencyRateMap.put(Currency.USD, root.at("/USD/nbu/sell").asDouble());
            currencyRateMap.put(Currency.EUR, root.at("/EUR/nbu/sell").asDouble());
            log.info("CurrencyRates is received from json. It took {} ms", System.currentTimeMillis() - startTime);

        } catch (IOException e) {
            log.error("Error retriveving currency rates from json {}", e);
            throw new RuntimeException(e);
        }

    }

    public double extractRate(String jsonRate) {

        JsonNode root = null;
        try {
            root = objectMapper.readTree(jsonRate);
            return root.at("/rating").asDouble();
        } catch (IOException e) {
            log.error("Error retriveving rating from json {}", e);
            throw new RuntimeException(e);
        }


    }

    String convertMoviesToJson(List<MovieDto> movies, String... fields) {
        log.info("Start parsing movies to json ");
        long startTime = System.currentTimeMillis();
        String json;
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

    public String convertObjectToJson(Object objectToJson) {
        log.info("Start parsing object to json ");
        long startTime = System.currentTimeMillis();
        String json;
        try {
            json = objectMapper.writer().writeValueAsString(objectToJson);
            log.info("Object json is received. It took {} ms", System.currentTimeMillis() - startTime);
        } catch (JsonProcessingException e) {
            log.error("Error parsing object with error {}", e);
            throw new RuntimeException(e);
        }
        return json;

    }

    public LoginRequest parseLoginJson(String loginJson) {
        log.info("Start parsing jsonLogin ");
        long startTime = System.currentTimeMillis();
        try {
            LoginRequest loginRequest = objectMapper.readValue(loginJson, LoginRequest.class);
            log.info("Finish parsing jsonLogin {}. It took {} ms", loginRequest.getEmail(), System.currentTimeMillis() - startTime);
            return loginRequest;
        } catch (IOException e) {
            log.error("Error parsing incoming json exception {}", e);
            throw new BadLoginRequestException();
        }

    }

    public Review parseJsonToReview(String jsonReview, int userId) {

        try {
            JsonNode root = objectMapper.readTree(jsonReview);
            Review review = new Review();
            User user = new User();
            user.setId(userId);
            review.setUser(user);
            review.setMovieId(root.at("/movieId").asInt());
            review.setText(root.at("/text").asText());
            return review;
        } catch (IOException e) {
            log.error("Error parsing incoming json{} exception {}", jsonReview, e);
            throw new RuntimeException(e);
        }


    }

    public <T> T jsonToObject(String json, Class<T> clazz) {

        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("Error parsing incoming json{} exception {}", json, e);
            throw new RuntimeException(e);
        }
    }

    public Report parseJsonToReport(String jsonReport, PrincipalUser user) {

        try {
            JsonNode root = objectMapper.readTree(jsonReport);

            Matcher matcher;

            Report report = new Report();
            report.setUserId(user.getUserId());
            report.setUserEmail(user.getName());

            report.setReportType(ReportType.getTypeName(root.at("/reportType").asText()));
            if (report.getReportType() == ReportType.ADDED_DURING_PERIOD) {
                String dateFrom = root.at("/dateFrom").asText();
                matcher = datePattern.matcher(dateFrom);
                if (matcher.matches()) {
                    report.setDateFrom(LocalDate.parse(dateFrom, formatter));
                } else {
                    throw new RuntimeException("dateFrom should be provided");
                }
                String dateTo = root.at("/dateTo").asText();
                matcher = datePattern.matcher(dateTo);
                if (matcher.matches()) {
                    report.setDateTo(LocalDate.parse(dateTo, formatter));
                }else {
                    throw new RuntimeException("dateTo should be provided");
                }
            }
            report.setReportOutputType(ReportOutputType.getReportOutputType(root.at("/reportOutputType").asText()));
            report.setReportState(ReportState.NEW);

            return report;
        } catch (IOException e) {
            String message = String.format("Error parsing incoming json %s exception", jsonReport);
            log.error(message, e);
            throw new RuntimeException("parseJsonToReport Error parsing incoming json " + jsonReport, e);
        }


    }


}
