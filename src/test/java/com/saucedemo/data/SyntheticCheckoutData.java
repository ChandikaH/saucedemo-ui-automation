package com.saucedemo.data;

import com.saucedemo.data.models.CheckoutUser;
import net.datafaker.Faker;

public class SyntheticCheckoutData {

    private static final Faker faker = new Faker();

    public static CheckoutUser randomUser() {
        return CheckoutUser.builder().firstName(faker.name().firstName()).lastName(faker.name().lastName()).postalCode(faker.address().zipCode()).build();
    }
}