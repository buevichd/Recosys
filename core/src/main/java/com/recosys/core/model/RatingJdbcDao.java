package com.recosys.core.model;

import com.recosys.core.entity.Product;
import com.recosys.core.entity.Rating;
import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.RatingDao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("ratingDao")
public class RatingJdbcDao extends AbstractJdbcDao<Rating> implements RatingDao {
    private static final String UPDATE_QUERY = "UPDATE recosys_rating SET rating=:rating, user=:user, product=:product " +
            "WHERE id = :id";
    private static final String CREATE_QUERY = "INSERT INTO recosys_rating (rating, user, product) VALUES " +
            "(:rating, :user, :product)";
    private static final String GET_BY_USER_AND_PRODUCT_QUERY = "SELECT * FROM recosys_rating " +
            "WHERE recosys_rating.user = :user AND product = :product";

    private static final String RATING = "rating";
    private static final String USER = "user";
    private static final String PRODUCT = "product";
    private static final String TABLE_NAME = "recosys_rating";

    @Autowired private ProductJdbcDao productDao;
    @Autowired private UserJdbcDao userDao;

    @NotNull
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @NotNull
    @Override
    protected String getSqlQueryForUpdate() {
        return UPDATE_QUERY;
    }

    @NotNull
    @Override
    protected String getSqlQueryForCreate() {
        return CREATE_QUERY;
    }

    @NotNull
    @Override
    protected Rating convert(Map<String, Object> row) {
        Rating rating = new Rating((Long) row.get(ID));
        rating.setRating((Integer) row.get(RATING));
        rating.setUser(userDao.get((Long) row.get(USER)));
        rating.setProduct(productDao.get((Long) row.get(PRODUCT)));
        return rating;
    }

    @NotNull
    @Override
    protected RowMapper<Rating> getRowMapper() {
        return (resultSet, i) -> {
            Rating rating = new Rating(resultSet.getLong(ID));
            rating.setRating(resultSet.getInt(RATING));
            rating.setProduct(productDao.get(resultSet.getLong(PRODUCT)));
            rating.setUser(userDao.get(resultSet.getLong(USER)));
            return rating;
        };
    }

    @NotNull
    @Override
    protected Map<String, Object> getFieldsMap(@NotNull Rating rating) {
        Map<String, Object> map = new HashMap<>();
        map.put(RATING, rating.getRating());
        map.put(PRODUCT, rating.getProduct().getId());
        map.put(USER, rating.getUser().getId());
        return map;
    }

    @Override
    @Nullable
    public Rating get(@NotNull User user, @NotNull Product product) {
        return get(user.getId(), product.getId());
    }

    @Override
    @Nullable
    public Rating get(@NotNull Long userId, @NotNull Long productId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(USER, userId);
        map.put(PRODUCT, productId);
        try {
            return getNamedParameterJdbcTemplate().queryForObject(GET_BY_USER_AND_PRODUCT_QUERY, map, getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            // there is no such ratings.
            return null;
        }
    }
}
