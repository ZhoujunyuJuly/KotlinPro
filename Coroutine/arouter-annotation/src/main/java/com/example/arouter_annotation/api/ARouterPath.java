package com.example.arouter_annotation.api;

import com.example.arouter_annotation.RouterBean;

import java.util.Map;

public interface ARouterPath {
    /**
     * key:"/order/Order_MainActivity" 路径名
     * value: Order_MainActivity.class  类
     * @return
     */
    Map<String, RouterBean> getPathMap();
}
