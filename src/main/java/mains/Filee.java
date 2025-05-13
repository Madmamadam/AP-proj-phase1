package mains;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import model.Level_Stack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Filee {
//    public static Level_Stack level_stack;
    static ObjectMapper objectMapper = new ObjectMapper();

    static File file = new File("level_stack.json");

    static void read(){
//        User ali = new User("alij","12345678");
//        users.add(ali);
        ObjectMapper mapper = new ObjectMapper();
        if (file.exists()) {
            try (InputStream input = new FileInputStream(file)) {
                CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, Level_Stack.class);
                level_stack = mapper.readValue(input, listType);
                //System.out.println("Users loaded: " + loadedUsers);

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static void save(){
        try {
            objectMapper.writeValue(file,level_stack);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
