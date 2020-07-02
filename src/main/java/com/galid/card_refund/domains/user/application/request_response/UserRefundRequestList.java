package com.galid.card_refund.domains.user.application.request_response;

import lombok.Getter;

import java.util.List;

@Getter
public class UserRefundRequestList {
    private List<UserRefundRequest> userRefundRequestList;

    public void setUserRefundRequestList(List<UserRefundRequest> userRefundRequestList) {
        this.userRefundRequestList = userRefundRequestList;
        refundItemImageToByte();
    }

    public void refundItemImageToByte() {
        this.userRefundRequestList.stream()
                .forEach(request -> request.setRefundItemImageByte());
    }
}
