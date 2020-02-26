package xyz.silverspoon.component;

import lombok.Data;

@Data
public class WSResult<T> {
    private int type;
    private T data;
}
