package com.recosys.core.model;

import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.UserDao;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("userDao")
public class UserJdbcDao extends AbstractJdbcDao<User> implements UserDao {
    public static final String TABLE_NAME = "recosys_user";

    @NotNull
    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @NotNull
    @Override
    protected User convert(Map<String, Object> row) {
        return new User((Long) row.get(ID));
    }

    @NotNull
    @Override
    protected RowMapper<User> getRowMapper() {
        return (resultSet, i) -> new User(resultSet.getLong(ID));
    }

    @NotNull
    @Override
    protected Map<String, Object> getFieldsMap(@NotNull User item) {
        return new HashMap<>();
    }
}
