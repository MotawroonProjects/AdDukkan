package com.addukkan.models;

import java.io.Serializable;

public class SingleMessageDataModel extends ResponseModel implements Serializable {
    private MessageModel data;

    public MessageModel getData() {
        return data;
    }
}
