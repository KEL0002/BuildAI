package de.kel0002.buildai.util;

import de.kel0002.buildai.BuildAI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;


import java.util.*;

public class ConfigManager {

    public static Dictionary<String, Object> getPayload(String modelName) {

        FileConfiguration config = BuildAI.getInstance().getConfig();
        String payloadpath = "models." + modelName + ".payload";


        if (config.contains(payloadpath)) {
            Dictionary<String, Object> payload = new Hashtable<>();


            List<?> configpayload = config.getList(payloadpath);

            if (configpayload == null) return null;
            for (Object singledic : configpayload){

                if (singledic instanceof LinkedHashMap){
                    LinkedHashMap<?, ?> map = (LinkedHashMap<?, ?>) singledic;
                    Map.Entry<?, ?> entry = map.entrySet().iterator().next();
                    payload.put(entry.getKey().toString(), entry.getValue());
                } else {
                    Bukkit.getLogger().warning("config garbage: the payload has to be structured correctly");
                }
            }

            return payload;
        }
        return null;
    }

    public static String getUrl(String modelName) {
        FileConfiguration config = BuildAI.getInstance().getConfig();
        if (config.contains("models." + modelName + ".endpoint")) {
            return config.getString("models." + modelName + ".endpoint");
        }
        return null;
    }


    public static ArrayList<String> get_model_list(){
        FileConfiguration config = BuildAI.getInstance().getConfig();
        ArrayList<String> modelNames = new ArrayList<>();

        if (config.getList("model_list") == null) return modelNames;

        for (Object s : config.getList("model_list")){
            modelNames.add(String.valueOf(s));
        }

        return modelNames;
    }



    public static Dictionary<String, Object> replaceInDictionary(Dictionary<String, Object> dictionary, String target, String replacement) {
        Enumeration<String> keys = dictionary.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Object value = dictionary.get(key);

            if (value instanceof String) {
                String valueStr = (String) value;
                valueStr = valueStr.replace(target, replacement);
                dictionary.put(key, valueStr);
            }
        }
        return dictionary;
    }


    public static List<String> getVarsinDictionary(Dictionary<String, Object> dictionary) {
         List<String> result = new ArrayList<>();

        Enumeration<String> keys = dictionary.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Object value = dictionary.get(key);

            if (value instanceof String) {
                String stringValue = (String) value;

                if (stringValue.startsWith("%") && stringValue.endsWith("%")) {
                    result.add(stringValue);
                }
            }
        }

        return result.isEmpty() ? null : result;
    }
}
