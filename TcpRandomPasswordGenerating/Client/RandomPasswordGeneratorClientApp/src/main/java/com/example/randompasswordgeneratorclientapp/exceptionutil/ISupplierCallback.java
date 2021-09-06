package com.example.randompasswordgeneratorclientapp.exceptionutil;


@FunctionalInterface
public interface ISupplierCallback<R> {
    R get() throws Exception;
}

