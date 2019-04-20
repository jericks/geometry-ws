package org.cugos.geometry.ws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class GeometryWriters {

    /**
     * Get a list of all registered Writers
     * @return A List of Writers
     */
    public static List<GeometryWriter> list() {
        List<GeometryWriter> writers = new ArrayList<>();
        ServiceLoader<GeometryWriter> serviceLoader = ServiceLoader.load(GeometryWriter.class);
        Iterator<GeometryWriter> iterator = serviceLoader.iterator();
        while(iterator.hasNext()) {
            writers.add(iterator.next());
        }
        return writers;
    }

    /**
     * Find a Writer by name or return null
     * @param name The name
     * @return A Writer or null
     */
    public static GeometryWriter find(String name) {
        List<GeometryWriter> writers = list();
        for(GeometryWriter writer : writers) {
            if (writer.getName().equalsIgnoreCase(name)) {
                return writer;
            }
        }
        return null;
    }

}
