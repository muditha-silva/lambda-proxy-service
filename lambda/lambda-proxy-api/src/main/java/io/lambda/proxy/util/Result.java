package io.lambda.proxy.util;

import java.util.Optional;

/**
 * 
 * @author muditha
 *
 * @param <T>
 */
public class Result<T> {

    private Error error;
    private T result;

    private Result(T result, Error error) {
        this.error = error;
        this.result = result;
    }

    public static <K> Result<K> withError(Error error) {
        return new Result<K>(null, error);
    }
    public static <K> Result<K> with(K result) {
        return new Result<K> (result, null);
    }

    public T get() {
        return this.result;
    }

    public Optional<Error> getError() {
        return Optional.ofNullable(this.error);
    }

}
