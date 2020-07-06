package com.galid.card_refund.common.aws;

public enum ImageType {
    PASSPORT_IMAGE("PASSPORT"), REFUND_IMAGE("REFUND"), BARCODE_IMAGE("REFUND_BARCODE");

    String type;

    ImageType(String type) {
        this.type = type;
    }
}
