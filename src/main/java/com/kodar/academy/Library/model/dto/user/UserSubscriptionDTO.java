package com.kodar.academy.Library.model.dto.user;

import com.kodar.academy.Library.model.constants.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class UserSubscriptionDTO {

    @Min(value = 1, message = Constants.LESS_THAN_1_INVALID)
    @Max(value = 3, message = Constants.MORE_THAN_3_INVALID)
    private int subscription;

    public int getSubscription() {
        return subscription;
    }

    public void setSubscription(int subscription) {
        this.subscription = subscription;
    }
}
