package es.thepuar.InfiniteSpace.manager;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ResourceBundle;

@Service
public class ResourceManager {

    ResourceBundle bundle;

    @PostConstruct
    public void init(){
        bundle = ResourceBundle.getBundle("config");
    }

    public String getProperty(String property){
        return this.bundle.getString(property);
    }
}
