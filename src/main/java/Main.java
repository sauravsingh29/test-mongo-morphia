import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Saurav Singh
 **/
public class Main {
    public static void main(String[] args) {
//        MongoConnection.getInstance().init();
        try{
            final String property = PropertiesProcessor.getProperty("jdsc.aws.secremanaer.name");
            System.out.println(property);
        }catch (Exception e){
            System.err.println(e.getMessage());
        }

    }
}
