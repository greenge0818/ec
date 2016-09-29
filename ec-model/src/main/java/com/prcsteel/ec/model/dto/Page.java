package com.prcsteel.ec.model.dto;

import java.util.ArrayList;

/**
 * Created by Rolyer on 2016/5/5.
 */
public class Page<T> extends ArrayList<T> {
    private T data;

    public Page() {
    }

//    public void clear(){
//
//    }

//    public void add(T o){
//
//    }

    public Page(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
