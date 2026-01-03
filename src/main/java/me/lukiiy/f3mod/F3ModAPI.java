package me.lukiiy.f3mod;

import java.util.Collection;
import java.util.Collections;

public interface F3ModAPI {
    default Collection<DebugEntry> getEntries() {
        return Collections.emptyList();
    }
}