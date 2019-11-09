

package famousproblems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class FileSizeTooSmall extends Exception{  

    public FileSizeTooSmall(String s) {
        super(s);
    }
    
}  
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
                    
                    if( result.get(key)==null ){
                        resultArray = new JSONArray();
                        resultArray.addAll(jsonArray);
                    }else{
                        resultArray = (JSONArray)result.get(key);
                        resultArray.addAll(jsonArray);
                    }
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
        
        System.out.println(result);
        for( Object key : result.keySet() ){
            location= folderPath+outputFileBaseName+outputFileSuffix+".json";
            JSONObject writeJson = new JSONObject(),fileJson = new JSONObject();
            JSONArray writeArray,fileArray;
            
            writeJson.put(key.toString(),result.get(key));
            writeArray = (JSONArray)writeJson.get(key.toString());
            
            
            for( int k = 0 ; k<writeArray.size() ; ){
                fileArray = new JSONArray();
                fileJson = new JSONObject();
                int initialSize = 6+key.toString().length();
                int oneObjectSize ,fileSize=initialSize;
                int objectCountPerFile=0,ind=0;

                oneObjectSize = ((JSONObject)writeArray.get(k)).toString().toCharArray().length+1;
                if( oneObjectSize+initialSize>maxSize ){
                    try{
                        throw new FileSizeTooSmall("ObjectSize : "+(oneObjectSize+initialSize)+" file size : "+maxSize);
                    }catch(FileSizeTooSmall e){
                        e.printStackTrace();
                        return;
                    }
                }
                while( (fileSize+oneObjectSize) <= maxSize ){
                    if( oneObjectSize+initialSize>maxSize ){
                        try{
                            throw new FileSizeTooSmall("ObjectSize : "+(oneObjectSize+initialSize)+" file size : "+maxSize);
                        }catch(FileSizeTooSmall e){
                            e.printStackTrace();
                            return;
                        }
                    }
                    if( k>=writeArray.size())break;
                    fileArray.add(writeArray.get(k));
                    fileSize += ((JSONObject)writeArray.get(k)).toString().toCharArray().length+1; 
                    k++;
                    if( k>=writeArray.size())break;
                    oneObjectSize = ((JSONObject)writeArray.get(k)).toString().toCharArray().length+1;
                }
                fileJson.put(key.toString(), fileArray);
                File file = new File(location);
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(fileJson.toJSONString());
                    fileWriter.flush(); 
    //                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("File"+outputFileSuffix+" size -> "+file.length());
                outputFileSuffix++;
                location= folderPath+outputFileBaseName+outputFileSuffix+".json";
            }
            
        }
    }
    public static void main(String[] args) {
        //sample function call
        //merge("C:\\Users\\sachin\\Desktop\\Json files\\","data", "result", 204);

        //Note : finish the folderpath with backslash
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Specify Folder path(With backslash at end) : ");
//        String folderPath = sc.nextLine();
//        System.out.println("Specify inputFilePrefix : ");
//        String inputFilePrefix = sc.next();
//        System.out.println("Specify outputFilePrefix : ");
//        String outputFilePrefix = sc.next();
//        System.out.println("Specify max file size");
//        int maxSize = sc.nextInt();
//        merge(folderPath,inputFilePrefix,outputFilePrefix,maxSize);
        merge("C:\\Users\\sachin\\Desktop\\Json files\\","data", "result", 150);
    }
}
