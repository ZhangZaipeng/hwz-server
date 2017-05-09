package com.hwz.platform.platform;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 帮助生成和访问Json-List对象的帮助类
 * Created by luoyifan on 2016/3/30.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class JsonArrayWapper implements Collection<Object> {

    private final List innerList;

    public JsonArrayWapper(List innerList) {
        this.innerList = innerList;
    }

    public JsonArrayWapper appendObj(Object... args) {
        JsonWapper jw = new JsonWapper();
        jw.set(args);
        innerList.add(jw.getInnerMap());
        return this;
    }

    public JsonArrayWapper append(Object obj) {
        innerList.add(obj);
        return this;
    }

    @Override
    public int size() {
        return innerList.size();
    }

    @Override
    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return innerList.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return innerList.iterator();
    }

    @Override
    public Object[] toArray() {
        return innerList.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return innerList.toArray(a);
    }

    @Override
    public boolean add(Object o) {
        if (o instanceof JsonWapper) {
            return innerList.add(((JsonWapper) o).getInnerMap());
        }
        return innerList.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return innerList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return innerList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<?> c) {
        return innerList.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return innerList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return innerList.retainAll(c);
    }

    @Override
    public void clear() {
        innerList.clear();
    }
}
