package com.ithd.chat.chatapp.stripe.stripecontroller;


import com.ithd.chat.chatapp.stripe.stripe.NewStripeSubscriptionResponse;
import com.ithd.chat.chatapp.stripe.stripeDao.NewStripeSubscriptionDao;

import java.sql.SQLException;

public class NewStripeSubscriptionController {
    private NewStripeSubscriptionDao newStripeSubscriptionDao = new NewStripeSubscriptionDao();

    public int saveNewStripeSubscription(NewStripeSubscriptionResponse newStripeSubscriptionResponse) throws SQLException {
        return newStripeSubscriptionDao.save(newStripeSubscriptionResponse);

    }
}
