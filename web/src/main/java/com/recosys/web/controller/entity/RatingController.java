package com.recosys.web.controller.entity;

import com.recosys.core.entity.Rating;
import com.recosys.core.model.interfaces.ProductDao;
import com.recosys.core.model.interfaces.RatingDao;
import com.recosys.core.model.interfaces.UserDao;
import com.recosys.web.dto.RatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Controller
@RequestMapping("/ratings")
@ResponseBody
public class RatingController {
    @Autowired private RatingDao ratingDao;
    @Autowired private ProductDao productDao;
    @Autowired private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    public List<RatingDTO> getRatings() {
        return ratingDao.getAll().stream().map(RatingDTO::new).collect(toList());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public RatingDTO getRating(@PathVariable("id") Long id) {
        return new RatingDTO(ratingDao.get(id));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public RatingDTO updateRating(@PathVariable("id") Long id, @RequestBody RatingDTO ratingDTO) {
        Rating rating = ratingDao.get(id);
        rating.setRating(rating.getRating());
        rating.setProduct(productDao.get(ratingDTO.getProductId()));
        rating.setUser(userDao.get(ratingDTO.getUserId()));
        return new RatingDTO(rating);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public RatingDTO createRating(@RequestBody RatingDTO ratingDTO) {
        Rating rating = new Rating();
        rating.setRating(ratingDTO.getRating());
        rating.setProduct(productDao.get(ratingDTO.getProductId()));
        rating.setUser(userDao.get(ratingDTO.getUserId()));
        ratingDao.create(rating);
        return new RatingDTO(rating);
    }
}
