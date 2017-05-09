package com.hwz.platform.platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwz.platform.Conv;

import java.io.IOException;
import java.util.*;

/**
 * 帮助生成和访问Json-Map对象的帮助类
 * Created by luoyifan on 2016/3/30.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class JsonWapper {
    private static final ObjectMapper mapper = new ObjectMapper();
    private final Map innerMap;

    public JsonWapper(Map innerMap) {
        this.innerMap = innerMap;
    }

    public JsonWapper(Object obj) throws JsonProcessingException {
        this(mapper.writeValueAsString(obj));
    }

    public JsonWapper() {
        this(new LinkedHashMap());
    }

    public JsonWapper(String jsonString) {
        this(JsonToMap(jsonString));
    }

    private static Map JsonToMap(String jsonString) {
        try {
            return mapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, ?> getInnerMap() {
        return (Map<String, ?>) innerMap;
    }

    public boolean contains(String... args) {
        assert (args.length >= 1);
        List<String> lst = Arrays.asList(args);
        Map cnode = this.innerMap;
        for (int i = 0; i < lst.size() - 1; i++) {
            String v = lst.get(i);
            if (!cnode.containsKey(v)) {
                return false;
            }
            cnode = (Map) cnode.get(v);
        }
        return (cnode.containsKey(lst.get(lst.size() - 1)));
    }

    public Object get(String... args) {
        assert (args.length >= 1);
        List<String> lst = Arrays.asList(args);
        Map cnode = this.innerMap;
        for (int i = 0; i < lst.size() - 1; i++) {
            String v = lst.get(i);
            if (!cnode.containsKey(v)) {
                return null;
            }
            cnode = (Map) cnode.get(v);
        }
        if (!cnode.containsKey(lst.get(lst.size() - 1))) {
            return null;
        }
        return cnode.get(lst.get(lst.size() - 1));
    }

    public String asStr(String... args) {
        return Conv.NS(get(args));
    }

    public int asInt(String... args) {
        return Conv.NI(get(args));
    }

    public long asLong(String... args) {
        return Conv.NL(get(args));
    }

    public boolean asBoolean(String... args) {
        return Conv.NB(get(args));
    }

    public Collection<String> keys() {
        return innerMap.keySet();
    }

    public JsonWapper asDic(String... args) {
        assert (args.length >= 1);
        List<String> lst = Arrays.asList(args);
        Map jb = null;
        if (lst.size() >= 2) {
            jb = buildPath(lst.subList(0, lst.size() - 1));
        } else {
            jb = innerMap;
        }
        Object v = lst.get(lst.size() - 1);
        Map lr = null;
        if (!jb.containsKey(v)) {
            lr = new LinkedHashMap<String, Object>();
            jb.put(v, lr);
        } else {
            lr = (Map) jb.get(v);
        }
        return new JsonWapper(lr);
    }

    public JsonArrayWapper asList(String... args) {
        assert (args.length >= 1);
        List<String> lst = Arrays.asList(args);
        Map jb = null;
        if (lst.size() >= 2) {
            jb = buildPath(lst.subList(0, lst.size() - 1));
        } else {
            jb = innerMap;
        }
        Object v = lst.get(lst.size() - 1);
        List lr = null;
        if (!jb.containsKey(v)) {
            lr = new ArrayList();
            jb.put(v, lr);
        } else {
            lr = (List) jb.get(v);
        }
        return new JsonArrayWapper(lr);
    }

    private Map buildPath(List<?> lst) {
        Map cnode = this.innerMap;
        for (Object obj : lst) {
            assert ((obj instanceof String) && obj.toString().length() > 0);
            String v = (String) obj;
            if (!cnode.containsKey(v)) {
                cnode.put(v, new LinkedHashMap<String, Object>());
            }
            cnode = (Map) cnode.get(v);
        }
        return cnode;
    }

    public JsonWapper set(Object... args) {
        assert (args.length >= 2);
        List<Object> lst = Arrays.asList(args);
        Map jb = buildPath(lst.subList(0, lst.size() - 2));
        Object v = lst.get(lst.size() - 1);
        jb.put(lst.get(lst.size() - 2).toString(), v);
        return this;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean pretty) {
        try {
            if (pretty) {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(innerMap);
            } else {
                return mapper.writeValueAsString(innerMap);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T asObject(String key, Class<T> clazz) {
        try {
            String jsonStr = mapper.writeValueAsString(this.innerMap.get(key));
            return mapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
