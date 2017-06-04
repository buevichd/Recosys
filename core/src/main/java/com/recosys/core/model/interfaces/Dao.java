package com.recosys.core.model.interfaces;

import com.recosys.core.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Dao<T extends Entity> {
    @NotNull List<T> getAll();
    T get(@NotNull Long id);
    void update(@NotNull T item);
    void create(@NotNull T item);
    void delete(@NotNull T item);
}
