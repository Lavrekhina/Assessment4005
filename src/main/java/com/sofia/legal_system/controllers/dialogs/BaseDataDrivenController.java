package com.sofia.legal_system.controllers.dialogs;

public abstract class BaseDataDrivenController<T> {
    protected T data;

    public void setData(T data) {
        this.data = data;
    }

    public abstract void init();
}
