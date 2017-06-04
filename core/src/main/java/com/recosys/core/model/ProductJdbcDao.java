package com.recosys.core.model;

import com.recosys.core.entity.Product;
import com.recosys.core.model.interfaces.ProductDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("productDao")
public class ProductJdbcDao extends AbstractJdbcDao<Product> implements ProductDao {
    public static final String TABLE_NAME = "recosys_product";

    @NotNull
    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @NotNull
    @Override
    protected Product convert(Map<String, Object> row) {
        return new Product((Long) row.get(ID));
    }

    @NotNull
    @Override
    protected RowMapper<Product> getRowMapper() {
        return (resultSet, i) -> new Product(resultSet.getLong(ID));
    }

    @NotNull
    @Override
    protected Map<String, Object> getFieldsMap(@NotNull Product item) {
        return new HashMap<>();
    }
}
