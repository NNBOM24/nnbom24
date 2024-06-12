package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import global.Data;
import global.ModuleInfo;
import global.ProjectInfo;

public class myUtil {

    public static String getFileNameWithoutExtension(String path) {
        Path path2 = Paths.get(path);
        String fileName = path2.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName; // no extension
        } else {
            return fileName.substring(0, dotIndex);
        }
    }
    
    public static void storeProjectInfo(String path) {
        String csvPath = path + "/projectInfo.csv";
        File functionMap = new File(csvPath);
        try{
            BufferedWriter writeText = new BufferedWriter(new FileWriter(functionMap));
            writeText.write("projectID,projectName,imports,pretrainedModels");
            writeText.newLine();
            for (ProjectInfo tmpProject : Data.allProjects.values()) {
                writeText.write(tmpProject.getProjectID()+","+
                                tmpProject.getProjectName()+","+
                                tmpProject.getImports().toString()+","+
                                tmpProject.getPretrainedModels().toString());
                writeText.newLine(); 
            }
            writeText.flush();
            writeText.close();
        }catch (FileNotFoundException e){
            System.out.println("Path doesn't exist: "+csvPath);
        }catch (IOException e){
            System.out.println("File write/read error!");
        }
        // Release allProjects
        Data.allProjects.clear();
        Data.allProjects = null;
    }

    public static void storeModulesInfo(String path) {
        String csvPath = path + "/moduleInfo.csv";
        File functionMap = new File(csvPath);
        try{
            BufferedWriter writeText = new BufferedWriter(new FileWriter(functionMap));
            writeText.write("moduleID,moduleProjectID,modulePath,moduleName,startLine,endLine,moduleHash");
            writeText.newLine();
            for (HashMap<Integer, ModuleInfo> tmpHashMap : Data.allModulesList) {
                for (ModuleInfo tmpModule : tmpHashMap.values()) {
                    writeText.write(tmpModule.getModuleID()+","+
                                    tmpModule.getModuleProjectID()+",\""+
                                    tmpModule.getModulePath()+"\","+
                                    tmpModule.getModuleName()+","+
                                    tmpModule.getStartLine()+","+
                                    tmpModule.getEndLine()+","+
                                    tmpModule.getModuleHash()
                                    );
                    writeText.newLine(); 
                }
            }
            
            writeText.flush();
            writeText.close();
        }catch (FileNotFoundException e){
            System.out.println("Path doesn't exist: "+csvPath);
        }catch (IOException e){
            System.out.println("File write/read error!");
        }
    }

}
