package com.recosys.core.model;

import com.recosys.core.entity.Product;
import com.recosys.core.entity.EstimatedRating;
import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.EstimatedRatingDao;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("estimatedRatingDao")
public class EstimatedRatingJdbcDao extends AbstractJdbcDao<EstimatedRating> implements EstimatedRatingDao {
    private static final String UPDATE_QUERY = "UPDATE recosys_estimated_rating SET rating=:rating, user=:user, " +
            "product=:product WHERE id = :id";
    private static final String CREATE_QUERY = "INSERT INTO recosys_estimated_rating (rating, user, product) VALUES " +
            "(:rating, :user, :product)";
    private static final String GET_BY_USER_AND_PRODUCT_QUERY = "SELECT * FROM recosys_estimated_rating " +
            "WHERE recosys_estimated_rating.user = :user AND product = :product";
    private static final String GET_BY_USER_QUERY = "SELECT * FROM recosys_estimated_rating " +
            "WHERE recosys_estimated_rating.user = :user";

    private static final String RATING = "rating";
    private static final String USER = "user";
    private static final String PRODUCT = "product";
    private static final String TABLE_NAME = "recosys_estimated_rating";

    @Autowired private ProductJdbcDao productDao;
    @Autowired private UserJdbcDao userDao;

    @NotNull
    @Override
    protected String getTableName() {
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
    protected String getSqlQueryForGetByUser() {
        return GET_BY_USER_QUERY;
    }

    @NotNull
    @Override
    protected EstimatedRating convert(Map<String, Object> row) {
        EstimatedRating rating = new EstimatedRating((Long) row.get(ID));
        rating.setRating((Double) row.get(RATING));
        rating.setUser(userDao.get((Long) row.get(USER)));
        rating.setProduct(productDao.get((Long) row.get(PRODUCT)));
        return rating;
    }

    @NotNull
    @Override
    protected RowMapper<EstimatedRating> getRowMapper() {
        return (resultSet, i) -> {
            EstimatedRating rating = new EstimatedRating(resultSet.getLong(ID));
            rating.setRating(resultSet.getDouble(RATING));
            rating.setProduct(productDao.get(resultSet.getLong(PRODUCT)));
            rating.setUser(userDao.get(resultSet.getLong(USER)));
            return rating;
        };
    }

    @NotNull
    @Override
    protected Map<String, Object> getFieldsMap(@NotNull EstimatedRating rating) {
        Map<String, Object> map = new HashMap<>();
        map.put(RATING, rating.getRating());
        map.put(PRODUCT, rating.getProduct().getId());
        map.put(USER, rating.getUser().getId());
        return map;
    }

    @Override
    @Nullable
    public EstimatedRating get(@NotNull User user, @NotNull Product product) {
        return get(user.getId(), product.getId());
    }

    @Override
    @Nullable
    public EstimatedRating get(@NotNull Long userId, @NotNull Long productId) {
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

    @Override
    public @NotNull List<EstimatedRating> getForUser(@NotNull User user) {
        return getForUser(user.getId());
    }

    @Override
    public @NotNull List<EstimatedRating> getForUser(@NotNull Long userId) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(USER, userId);
        List<Map<String, Object>> row = getNamedParameterJdbcTemplate().queryForList(getSqlQueryForGetByUser(), map);
        return row.stream().map(this::convert).collect(Collectors.toList());
    }
}
