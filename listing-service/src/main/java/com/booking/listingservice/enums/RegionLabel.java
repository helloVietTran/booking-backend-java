package com.booking.listingservice.enums;

import lombok.Getter;

@Getter
public enum RegionLabel {
    COUNTRYSIDE("Miền quê"),
    BEACH("Bãi biển"),
    CITY("Thành phố"),
    ROMANTIC("Lãng mạn"),
    RELAX("Thư giãn"),
    ;
    RegionLabel(String label){
        this.label = label;
    }
    private final String label;
}
