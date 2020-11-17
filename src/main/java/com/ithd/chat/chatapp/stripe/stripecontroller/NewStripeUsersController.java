package com.ithd.chat.chatapp.stripe.stripecontroller;


import com.ithd.chat.chatapp.stripe.stripe.NewStripeUsersResponse;

import java.sql.SQLException;

public class NewStripeUsersController {

    private NewStripeUsersControllerDao newStripeUsersControllerDao = new NewStripeUsersControllerDao();

    public int saveNewStripeUser(NewStripeUsersResponse newStripeUsersResponse) throws SQLException {
        return newStripeUsersControllerDao.save(newStripeUsersResponse);
    }

    public NewStripeUsersResponse getStripUserObjectByUserId(int id) throws SQLException {
        return newStripeUsersControllerDao.getStripUserObjectByUserId(id);
    }

    public int deleteStripeUSer(String customerId) throws SQLException {
        return newStripeUsersControllerDao.deleteStripeUSer(customerId);
    }
}
