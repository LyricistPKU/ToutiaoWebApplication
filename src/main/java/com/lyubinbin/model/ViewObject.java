package com.lyubinbin.model;

import java.util.HashMap;
import java.util.Map;

/**
 * for convenience of pass velocity model
 * Created by Lyu binbin on 2016/7/25.
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
