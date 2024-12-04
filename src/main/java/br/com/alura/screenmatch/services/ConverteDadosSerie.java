package br.com.alura.screenmatch.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDadosSerie implements InterfaceConverteDados{

    ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T converteDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);

        } catch (JsonMappingException e) {
            throw new RuntimeException(e);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /* Realizando para Gson:

    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();


    public <T> T converteDados(String json, Class<T> classe){

        return gson.fromJson(json, classe);
        }



     */
}
