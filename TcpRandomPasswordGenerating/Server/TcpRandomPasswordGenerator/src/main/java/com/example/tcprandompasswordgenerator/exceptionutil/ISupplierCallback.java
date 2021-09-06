package com.example.tcprandompasswordgenerator.exceptionutil;


@FunctionalInterface
public interface ISupplierCallback<R> {
    R get() throws Exception;
}

