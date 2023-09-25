package com.kodar.academy.Library.model.dto.book;

import com.kodar.academy.Library.model.enums.Deactivation;

public class BookChangeStatusDTO {

    private boolean isActive;
    private Deactivation deactivationReason;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Deactivation getDeactivationReason() {
        return deactivationReason;
    }

    public void setDeactivationReason(Deactivation deactivationReason) {
        this.deactivationReason = deactivationReason;
    }

    @Override
    public String toString() {
        return "{" +
                "isActive=" + isActive +
                ", deactivationReason=" + deactivationReason +
                '}';
    }
}
