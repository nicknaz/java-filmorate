package ru.yandex.practicum.filmorate.exception;

public class NotFoundedException extends RuntimeException {
    public NotFoundedException() {
    }

    public NotFoundedException(String message) {
        super(message);
    }

    public NotFoundedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundedException(Throwable cause) {
        super(cause);
    }
}
