package fr.triedge.todo.utils;

import fr.triedge.todo.model.Entry;

import java.util.*;

public class Utils {

    public static TreeMap<String, ArrayList<Entry>> filterByProject(ArrayList<Entry> entries){
        TreeMap<String, ArrayList<Entry>> map = new TreeMap<>();
        for (Entry e : entries){
            if (!e.getStatus().getName().equalsIgnoreCase("closed")){
                if (map.containsKey(e.getProject().getName())){
                    map.get(e.getProject().getName()).add(e);
                }else{
                    map.put(e.getProject().getName(), new ArrayList<Entry>(Collections.singletonList(e)));
                }
            }
        }
        return map;
    }

    public static String shrinkText(String text, int length){
        if (text == null)
            return "";
        if (text.length() > length){
            return text.substring(0, length+1)+"[...]";
        }else{
            return text;
        }
    }

}
