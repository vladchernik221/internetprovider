package com.chernik.internetprovider.servlet.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestParameter {
    private String uri;
    private RequestType type;

    public RequestParameter(String uri) {
        this.uri = uri;
    }
}
