package com.example.spring6.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceI18n {

    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("message", new Locale("zh", "CN"));
        String title = bundle.getString("title");
        System.out.println(title);
    }
}
