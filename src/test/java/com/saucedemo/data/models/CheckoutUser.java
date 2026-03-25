package com.saucedemo.data.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckoutUser {
    private String firstName;
    private String lastName;
    private String postalCode;
}