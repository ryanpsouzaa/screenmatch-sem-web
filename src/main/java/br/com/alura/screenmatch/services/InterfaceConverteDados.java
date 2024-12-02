package br.com.alura.screenmatch.services;

public interface InterfaceConverteDados {

    <T> T converteDados(String json, Class<T> classe);
}
