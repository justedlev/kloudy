package io.justedlev.msrv.kloudy.model;

import java.util.Map;

public interface FluentAttributable<V> extends Attributable<V> {
    Map<String, V> attributes();

    @Override
    default Map<String, V> getAttributes() {
        return attributes();
    }
}
