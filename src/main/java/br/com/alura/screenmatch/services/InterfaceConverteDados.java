package br.com.alura.screenmatch.services;

public interface InterfaceConverteDados {

    <T> T converteDados(String json, Class<T> classe);
    /* Nesse método é convertido o conteúdo do Json para a classe que o usuário
    informar, assim podendo ter json de diversas api's sendo convertidas para diversas
    classes.
     */
}
