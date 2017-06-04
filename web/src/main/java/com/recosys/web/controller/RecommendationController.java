package com.recosys.web.controller;

import com.recosys.core.entity.EstimatedRating;
import com.recosys.core.model.interfaces.EstimatedRatingDao;
import com.recosys.core.svd.SvdEngine;
import com.recosys.web.dto.EstimatedRatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Controller
@RequestMapping("/recommendations")
@ResponseBody
public class RecommendationController {
    @Autowired SvdEngine svdEngine;
    @Autowired EstimatedRatingDao estimatedRatingDao;

    @RequestMapping(path = "/calculate", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void calculateRecommendations() {
        svdEngine.calculateRecommendations();
    }

    @RequestMapping(path = "/user/{userId}/product/{productId}", method = RequestMethod.GET)
    public EstimatedRatingDTO getRecommendation(@PathVariable("userId") Long userId,
                                                @PathVariable("productId") Long productId) {
        EstimatedRating rating = estimatedRatingDao.get(userId, productId);
        return rating != null ? new EstimatedRatingDTO(rating) : null;
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public List<EstimatedRatingDTO> getUserRecommendations(@PathVariable("userId") Long userId,
            @RequestParam(name = "quantity", defaultValue = "3") Long quantity) {
        return estimatedRatingDao.getForUser(userId).stream()
                .sorted(Comparator.comparingDouble(EstimatedRating::getRating).reversed())
                .limit(quantity)
                .map(EstimatedRatingDTO::new)
                .collect(toList());
    }
}
