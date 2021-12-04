package com.chan.labelimg.error;

import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;

/**
 * @author: chen zheng
 * @Date: 12/4/2021 4:55 PM
 */
@ComponentScan
public class ErrorPage implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        org.springframework.boot.web.server.ErrorPage errorPage = new org.springframework.boot.web.server.ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
        registry.addErrorPages(errorPage);
    }
}
