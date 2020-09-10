package bsa.java.concurrency.exceptions;

public class MyIoException extends RuntimeException {
    public MyIoException(String message) {
        super(message);
    }
}