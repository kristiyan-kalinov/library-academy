package com.kodar.academy.Library.model.dto.book;

public class BookChangeStatusDTO {

    private boolean isActive;
    private String deactivationReason;

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getDeactivationReason() {
        return deactivationReason;
    }

    public void setDeactivationReason(String deactivationReason) {
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
