package com.example.caiyue.androidstuiodemo.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonManager {
    public static <T> T stringToBean(String jsonStr, Class<T> cla){
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, cla);
    }
    public static JsonObject stringToJson(String json){
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        return jsonObj;
    }
    public static JsonObject mapToJson(Map<String, Object> oMap){
        String jsonStr = new Gson().toJson(oMap);
        return new JsonParser().parse(jsonStr).getAsJsonObject();
    }
    /**
     * 依据json字符串返回Map对象
     * @param json
     * @return
     */
    public static Map<String,Object> stringToMap(String json){
        return JsonManager.toMap(JsonManager.stringToJson(json));
    }

    /**
     * bean 转 jsonString
     * @param bean
     * @return
     */
    public static String beanToJSONString(Object bean) {
        return new Gson().toJson(bean);
    }

    /**
     * listBean 转 jsonString
     * @param list
     * @param <T>
     * @return
     */
    public static <T> String listBeanToJsonString(List<T> list) {
        return new Gson().toJson(list);
    }

    /**
     *
     * @param jsonString
     * @return
     */
    public static JsonArray jsonStringToJsonArray(String jsonString){
        return new JsonParser().parse(jsonString).getAsJsonArray();
    }
    public static <T> List<T> jsonStringToListBean(String jsonString, Class clazz){

        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list =  new Gson().fromJson(jsonString, type);
        return list;
    }
    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
    /**
     * 将JSONObjec对象转换成Map-List集合
     * @param json
     * @return
     */
    public static Map<String, Object> toMap(JsonObject json){
        Map<String, Object> map = new HashMap<String, Object>();
        Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
        for (Iterator<Map.Entry<String, JsonElement>> iter = entrySet.iterator(); iter.hasNext(); ){
            Map.Entry<String, JsonElement> entry = iter.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof JsonArray)
                map.put((String) key, jsonArrayToList((JsonArray) value));
            else if(value instanceof JsonObject)
                map.put((String) key, toMap((JsonObject) value));
            else
                map.put((String) key, value);
        }
        return map;
    }

    /**
     * 将JSONArray对象转换成List集合
     * @param json
     * @return
     */
    public static List<Object> jsonArrayToList(JsonArray json){
        List<Object> list = new ArrayList<Object>();
        for (int i=0; i<json.size(); i++){
            Object value = json.get(i);
            if(value instanceof JsonArray){
                list.add(jsonArrayToList((JsonArray) value));
            }
            else if(value instanceof JsonObject){
                list.add(toMap((JsonObject) value));
            }
            else{
                list.add(value);
            }
        }
        return list;
    }
}
