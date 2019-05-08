package eu.hopu.activage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Assert;

public class HopAsserts {

    private static GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static Gson gson = gsonBuilder.create();
    private static ObjectMapper jacksonMapper = new ObjectMapper();

    public static void assertEqualsJson(Object expected, Object actual) {
        Assert.assertEquals(gson.toJson(expected), gson.toJson(actual));
        System.out.println(gson.toJson(actual));
    }

    public static void assertEqualsJackson(Object expected, Object actual) throws JsonProcessingException {
        Assert.assertEquals(jacksonMapper.writeValueAsString(expected), jacksonMapper.writeValueAsString(actual));
        System.out.println("Result JSON: " + gson.toJson(actual));
    }

}
