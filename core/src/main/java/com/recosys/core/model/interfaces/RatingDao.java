package com.recosys.core.model.interfaces;

import com.recosys.core.entity.Product;
import com.recosys.core.entity.Rating;
import com.recosys.core.entity.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface RatingDao extends Dao<Rating> {
    @Nullable Rating get(@NotNull User user, @NotNull Product product);
    @Nullable Rating get(@NotNull Long userId, @NotNull Long productId);
}
