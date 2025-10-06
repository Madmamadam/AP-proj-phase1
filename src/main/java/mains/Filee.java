package mains;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import model.LevelGame_StaticDataModel;
import view.Paintt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;



//not used in this version
public class Filee {
//    public static LevelGame_StaticDataModel level_gamemodelll = new LevelGame_StaticDataModel();
    static ObjectMapper objectMapper = new ObjectMapper();

    static File file = new File("level_gamemodel.json");

//    static void read(){
////        User ali = new User("alij","12345678");
////        users.add(ali);
//        ObjectMapper mapper = new ObjectMapper();
//        if (file.exists()) {
//            try (InputStream input = new FileInputStream(file)) {
//                CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class, LevelGame_StaticDataModel.class);
//                level_gamemodel = mapper.readValue(input, listType);
//                //System.out.println("Users loaded: " + loadedUsers);
//
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    static void save(){
//        try {
//            objectMapper.writeValue(file, level_gamemodel);
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
