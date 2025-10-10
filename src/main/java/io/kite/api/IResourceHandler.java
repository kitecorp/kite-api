package io.kite.api;

import org.pf4j.ExtensionPoint;

import java.util.ArrayList;
import java.util.List;

public interface IResourceHandler<T> extends ExtensionPoint {
    T create(T resource);

    T read(T resource);

    default List<T> read(List<T> resource){
        var list = new ArrayList<T>(resource.size());
        for (T dummyResource : resource) {
            T read = read(dummyResource);
            list.add(read);
        }
        return list;
    }

    T update(T resource);

    boolean delete(T resource);

}
