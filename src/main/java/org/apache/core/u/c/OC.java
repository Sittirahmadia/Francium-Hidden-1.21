package org.apache.core.u.c;

public enum OC {
    Handshake,
    Frame,
    Close,
    Ping,
    Pong;

    private static final OC[] VALUES = values();

    public static OC valueOf(int i) {
        return VALUES[i];
    }
}
