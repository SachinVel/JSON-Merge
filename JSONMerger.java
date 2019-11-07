

package famousproblems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONMerger {
    static void merge(String folderPath,String inputFileBaseName , String outputFileBaseName,int maxSize ){
        
        String location ;
        int outputFileSuffix=1;
        JSONParser jsonParser = new JSONParser();
        JSONArray resultArray = new JSONArray();
        JSONObject result = new JSONObject();
        for( int inputFileSuffix = 1 ; ;inputFileSuffix++ ){ 
            location= folderPath+inputFileBaseName+inputFileSuffix+".json";
            try ( FileReader reader = new FileReader(location) )
            {
                Object obj = jsonParser.parse(reader);
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray jsonArray = new JSONArray();
                for( Object key : jsonObject.keySet() ){
                    jsonArray = (JSONArray)jsonObject.get(key);
                    jsonArray.forEach(record -> resultArray.add(record) );
                    result.put(key.toString(), resultArray);
                }

            } catch (FileNotFoundException e) {
                break;
            }catch (IOException e ){
                e.printStackTrace();
            }catch(ParseException e){
                e.printStackTrace();
            }
        }
        for( Object key : result.keySet() ){
            location= folderPath+outputFileBaseName+outputFileSuffix+".json";
            JSONObject writeJson = new JSONObject();
            JSONArray writeArray;
            File file = new File(location);
            writeJson.put(key.toString(),result.get(key));
            try (FileWriter fileWriter = new FileWriter(file)) {
                while( writeJson.toString().toCharArray().length> maxSize){
                    writeArray = (JSONArray)writeJson.get(key.toString());
                    if(writeArray.size()==0){
                        break;
                    }
                    writeArray.remove(writeArray.size()-1);
                }
                if( writeJson.toJSONString().toCharArray().length<=maxSize ){
                    fileWriter.write(writeJson.toJSONString());
                    fileWriter.flush(); 
                }
                System.out.println("File length -> "+file.length());
            } catch (IOException e) {
                e.printStackTrace();
            }
            outputFileSuffix++;
        }
    }
    public static void main(String[] args) {
        //sample function call
        //Note : finish the folderpath with backslash
        merge("C:\\Users\\sachin\\Desktop\\Json files\\","data", "result", 204);
    }
}
