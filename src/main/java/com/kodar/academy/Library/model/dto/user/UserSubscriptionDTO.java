package com.kodar.academy.Library.model.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UserSubscriptionDTO {

    @Min(value = 1, message = "cant be less than 0")
    @Max(value = 3, message = "cant be more than 3")
    private int subscription;

    public int getSubscription() {
        return subscription;
    }

    public void setSubscription(int subscription) {
        this.subscription = subscription;
    }
}
