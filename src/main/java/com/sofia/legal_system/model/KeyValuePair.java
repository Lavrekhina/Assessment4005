/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sofia.legal_system.model;

/**
 * @author soflavre
 */
public class KeyValuePair<T, E> {
    private final T key;
    private final E value;


    @Override
    public String toString() {
        return getKey().toString();
    }


    public KeyValuePair(T key, E value) {

        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    public T getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public E getValue() {
        return value;
    }

    public static <T, E> KeyValuePair<T, E> of(T key, E value) {
        return new KeyValuePair<>(key, value);
    }
}
