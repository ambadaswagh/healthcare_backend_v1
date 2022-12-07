package com.healthcare.conf;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.dto.BlackListDTO;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.List;

public class ObjectListConverter implements AttributeConverter<List<BlackListDTO>, String> {

    private static ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<BlackListDTO> list) {
        if(list == null)
            return null;

        try {
            String output = mapper.writeValueAsString(list);
            return output;
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<BlackListDTO> convertToEntityAttribute(String joined) {
        if(joined == null)
            return null;

        try {
            TypeReference<List<BlackListDTO>> mapType = new TypeReference<List<BlackListDTO>>() {};
            List<BlackListDTO> jsonToBlackList = mapper.readValue(joined, mapType);
            return jsonToBlackList;
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
