package com.recosys.core.model;

import com.recosys.core.entity.Entity;
import com.recosys.core.model.interfaces.Dao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.PostConstruct;
import javax.naming.OperationNotSupportedException;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractJdbcDao<T extends Entity> extends NamedParameterJdbcDaoSupport implements Dao<T> {
    protected static final String ID = "id";

    private final String sqlQueryForAll = "SELECT * FROM " + getTableName();
    private final String sqlQueryForGetObject = "SELECT * FROM " + getTableName() + " WHERE id = :id";
    private final String sqlQueryForCreate = "INSERT INTO " + getTableName() + " (id) VALUES (NULL)";
    private final String sqlQueryForDelete = "DELETE FROM " + getTableName() + " WHERE id = :id";

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);

    }

    @NotNull
    @Override
    public List<T> getAll() {
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList(getSqlQueryForAll());
        List<T> allItems = new ArrayList<>(rows.size());
        for (Map<String, Object> row : rows) {
            allItems.add(convert(row));
        }
        return allItems;
    }

    @Override
    public T get(@NotNull Long id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put(ID, id);
        return getNamedParameterJdbcTemplate().queryForObject(getSqlQueryForGetObject(), map, getRowMapper());
    }

    @Override
    public void update(@NotNull T item) {
        Map<String, Object> map = getFieldsMap(item);
        map.put(ID, item.getId());
        getNamedParameterJdbcTemplate().update(getSqlQueryForUpdate(), map);
    }

    @Override
    public void create(@NotNull T item) {
        SqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(getFieldsMap(item));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(getSqlQueryForCreate(), mapSqlParameterSource, keyHolder);
        item.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void delete(@NotNull T item) {
        Map<String, Object> map = getFieldsMap(item);
        map.put(ID, item.getId());
        getNamedParameterJdbcTemplate().update(getSqlQueryForDelete(), map);
    }

    @NotNull protected String getSqlQueryForAll() {
        return sqlQueryForAll;
    }

    @NotNull protected String getSqlQueryForGetObject() {
        return sqlQueryForGetObject;
    }

    @NotNull protected String getSqlQueryForCreate() {
        return sqlQueryForCreate;
    }

    @NotNull protected String getSqlQueryForUpdate() {
        throw new UnsupportedOperationException();
    }

    @NotNull protected String getSqlQueryForDelete() {
        return sqlQueryForDelete;
    }

    @NotNull abstract protected String getTableName();

    @NotNull abstract protected T convert(Map<String, Object> row);
    @NotNull abstract protected RowMapper<T> getRowMapper();
    @NotNull abstract protected Map<String,Object> getFieldsMap(@NotNull T item);
}
