package com.recosys.core.model;

import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.UserDao;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:config.xml")
@ActiveProfiles({"test", "default"})
@Transactional
public class UserJdbcDaoTest {

    @Autowired UserDao userDao;

    @Test
    public void shouldCreateAndReturnUser() {
        User user = new User();

        userDao.create(user);
        User createdUser = userDao.get(user.getId());

        assertTrue(EqualsBuilder.reflectionEquals(user, createdUser));
    }

    @Test
    public void shouldCreateAndReturnAllUsers() {
        User user = new User();
        userDao.create(user);

        List<User> users = userDao.getAll();

        assertEquals(1, users.size());
        assertTrue(EqualsBuilder.reflectionEquals(user, users.get(0)));
    }

    @Test
    public void shouldDeleteUser() {
        User user = new User();
        userDao.create(user);

        userDao.delete(user);

        assertEquals(0, userDao.getAll().size());
    }
}
