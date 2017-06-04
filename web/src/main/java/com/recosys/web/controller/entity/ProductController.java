package com.recosys.web.controller.entity;

import com.recosys.core.entity.Product;
import com.recosys.core.model.interfaces.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/products")
@ResponseBody
public class ProductController {
    @Autowired
    private ProductDao productDao;

    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getProducts() {
        return productDao.getAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Product getProduct(@PathVariable("id") Long id) {
        return productDao.get(id);
    }

//    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
//    public Product updateProduct(@PathVariable("id") Long id) {
//        Product product = productDao.getProduct(id);
//        // update product values
//        return product;
//    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@RequestBody Product product) {
        productDao.create(product);
        return getProduct(product.getId());
    }

}
