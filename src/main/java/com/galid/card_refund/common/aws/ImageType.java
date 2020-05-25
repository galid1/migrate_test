package com.galid.card_refund.common.aws;

public enum ImageType {
    PASSPORT_IMAGE("user"), REFUND_IMAGE("refund"), BARCODE_IMAGE("refund-result-barcode");

    String group;

    ImageType(String group) {
        this.group = group;
    }
}
