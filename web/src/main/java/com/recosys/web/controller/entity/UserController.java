package com.recosys.web.controller.entity;

import com.recosys.core.entity.User;
import com.recosys.core.model.interfaces.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
@ResponseBody
public class UserController {
    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers() {
        return userDao.getAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable("id") Long id) {
        return userDao.get(id);
    }

//    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
//    public User updateUser(@PathVariable("id") Long id) {
//        User user = userDao.get(id);
//        // update user values
//        return user;
//    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        userDao.create(user);
        return user;
    }
}
