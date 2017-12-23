package com.chernik.internetprovider.servlet.command;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpRequestParameter {
    String uri;
    HttpRequestType type;

    public HttpRequestParameter(String uri) {
        this.uri = uri;
    }
}
