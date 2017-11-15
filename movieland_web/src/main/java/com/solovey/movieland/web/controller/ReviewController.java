package com.solovey.movieland.web.controller;

import com.solovey.movieland.dao.enums.UserRole;
import com.solovey.movieland.service.ReviewService;
import com.solovey.movieland.service.UserService;
import com.solovey.movieland.web.util.json.JsonJacksonConverter;
import com.solovey.movieland.web.util.security.entity.PrincipalUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


@Controller

public class ReviewController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private ReviewService reviewService;
    private JsonJacksonConverter jsonJacksonConverter;
    private UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, JsonJacksonConverter jsonJacksonConverter, UserService userService) {
        this.reviewService = reviewService;
        this.jsonJacksonConverter = jsonJacksonConverter;
        this.userService = userService;
    }

    @RequestMapping(value = "/v1/review", method = POST, consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity saveReview(@RequestBody String reviewJson, Principal principal) {
        log.info("Start save review {}", reviewJson);
        long startTime = System.currentTimeMillis();
        if (principal != null) {
            int userId = ((PrincipalUser) principal).getUserId();
            List<UserRole> userRoles = userService.getUserRoles(userId);
            if (userRoles.size() > 0 && userRoles.contains(UserRole.USER)) {
                reviewService.saveReviewToDb(jsonJacksonConverter.parseJsonToReview(reviewJson, userId));
                log.info("Review has been saved to DB. It took {} ms", System.currentTimeMillis() - startTime);
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.status(401).build();

    }

}
