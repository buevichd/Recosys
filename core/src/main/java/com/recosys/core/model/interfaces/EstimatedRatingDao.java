package com.recosys.core.model.interfaces;

import com.recosys.core.entity.EstimatedRating;
import com.recosys.core.entity.Product;
import com.recosys.core.entity.Rating;
import com.recosys.core.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface EstimatedRatingDao extends Dao<EstimatedRating> {
    @Nullable EstimatedRating get(@NotNull User user, @NotNull Product product);
    @Nullable EstimatedRating get(@NotNull Long userId, @NotNull Long productId);
    @NotNull List<EstimatedRating> getForUser(@NotNull Long userId);
    @NotNull List<EstimatedRating> getForUser(@NotNull User user);
}
