package com.example.arouter_annotation.api;

import java.util.Map;

public interface ARouterGroup {
    /**
     * key:"order或app或personal
     * value:order组下的所有 path-class
     * @return
     */
    Map<String,Class<? extends ARouterPath>> getGroupMap();
}
