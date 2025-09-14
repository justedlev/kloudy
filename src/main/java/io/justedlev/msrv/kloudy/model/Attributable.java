package io.justedlev.msrv.kloudy.model;

import java.util.Map;

public interface Attributable<V> {
    Map<String, V> getAttributes();
}
