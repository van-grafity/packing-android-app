package com.app.ivansuhendra.packinggla.utils;

public class NetworkState {
    private final Status status;
    private final String msg;

    static final NetworkState LOADED;
    static final NetworkState LOADING;
    static final NetworkState MAXPAGE;

    NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    static {
        LOADED=new NetworkState(Status.SUCCESS,"Success");
        LOADING=new NetworkState(Status.RUNNING,"Running");
        MAXPAGE=new NetworkState(Status.MAX,"No More page");
    }

    Status getStatus() {
        return status;
    }

    String getMsg() {
        return msg;
    }
}
