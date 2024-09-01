package org.tinycode.pay.apple.command;

public interface Command<P, T> {

    public T execute(P p, String accessToken);
}
