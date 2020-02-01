package xyz.silverspoon.component;

import lombok.Data;
import xyz.silverspoon.bean.ImUser;

@Data
public class ImCommonResult<T> {
    private int code;
    private T data;

    public ImCommonResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public static <T> ImCommonResult<T> success(T data) {
        return new ImCommonResult<>(200, data);
    }

    public static <T> ImCommonResult<T> NotFound(T data) {
        return new ImCommonResult<>(404, data);
    }

    public static <T> ImCommonResult<T> error(int i, T s) {
        return new ImCommonResult<>(i, s);
    }
}
