package com.recosys.web.controller.entity;

import com.recosys.core.model.interfaces.EstimatedRatingDao;
import com.recosys.web.dto.EstimatedRatingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/estimatedRatings")
@ResponseBody
public class EstimatedRatingController {
    @Autowired EstimatedRatingDao estimatedRatingDao;

    @RequestMapping(method = RequestMethod.GET)
    public List<EstimatedRatingDTO> getAllEstimatedRatings() {
        return estimatedRatingDao.getAll().stream().map(EstimatedRatingDTO::new).collect(toList());
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public EstimatedRatingDTO getEstimatedRating(@PathVariable("id") Long id) {
        return new EstimatedRatingDTO(estimatedRatingDao.get(id));
    }
}
