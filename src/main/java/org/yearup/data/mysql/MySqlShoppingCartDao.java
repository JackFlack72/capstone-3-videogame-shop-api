package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    private ProductDao productDao;

    @Override
    public ShoppingCart getByUserId(int userId) {
        return null;
    }

    @Override
    public ShoppingCart addProductToCart(int userId, int productId) {
        return null;
    }

    @Override
    public void updateProductQuantity(int userId, int productId, int quantity) {

    }

    @Override
    public void clearCart(int userId) {

    }

    public




}
