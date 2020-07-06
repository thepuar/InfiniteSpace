package es.thepuar.InfiniteSpace.manager;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ResourceBundle;


public class ResourceManager {

    static ResourceBundle bundle = ResourceBundle.getBundle("config");

    public static String getProperty(String property){
        return bundle.getString(property);
    }
}
