package com.galid.card_refund.domains.user.application.request_response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRefundRequestList {
    private List<UserRefundRequest> userRefundRequestList;

    public void refundItemImageToByte() {
        this.userRefundRequestList.stream()
                .forEach(request -> request.setRefundItemImageByte());
    }
}
