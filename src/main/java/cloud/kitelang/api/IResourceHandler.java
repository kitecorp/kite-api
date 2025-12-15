package cloud.kitelang.api;

import java.util.ArrayList;
import java.util.List;

public interface IResourceHandler<T> {
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
