package org.cugos.geometry.ws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class GeometryReaders {

    /**
     * Get a list of all registered Readers
     * @return A List of Readers
     */
    public static List<GeometryReader> list() {
        List<GeometryReader> readers = new ArrayList<>();
        ServiceLoader<GeometryReader> serviceLoader = ServiceLoader.load(GeometryReader.class);
        Iterator<GeometryReader> iterator = serviceLoader.iterator();
        while(iterator.hasNext()) {
            readers.add(iterator.next());
        }
        return readers;
    }

    /**
     * Find a Reader by name or return null
     * @param name The name
     * @return A Reader or null
     */
    public static GeometryReader find(String name) {
        List<GeometryReader> readers = list();
        for(GeometryReader reader : readers) {
            if (reader.getName().equalsIgnoreCase(name)) {
                return reader;
            }
        }
        return null;
    }

}
