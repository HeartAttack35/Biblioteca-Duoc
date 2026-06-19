package cl.duoc.biblioteca.ms_libro.dto;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;

    public ApiResponse(boolean success, String message, T data, String error) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getError() { return error; }
}