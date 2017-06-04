package com.recosys.core.model;

import com.recosys.core.entity.Product;
import com.recosys.core.model.interfaces.ProductDao;
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
public class ProductJdbcDaoTest {

    @Autowired ProductDao productDao;

    @Test
    public void shouldCreateAndReturnProduct() {
        Product product = new Product();

        productDao.create(product);
        Product createdProduct = productDao.get(product.getId());

        assertTrue(EqualsBuilder.reflectionEquals(product, createdProduct));
    }

    @Test
    public void shouldCreateAndReturnAllProducts() {
        Product product = new Product();

        productDao.create(product);
        List<Product> products = productDao.getAll();

        assertEquals(1, products.size());
        assertTrue(EqualsBuilder.reflectionEquals(product, products.get(0)));
    }
}
