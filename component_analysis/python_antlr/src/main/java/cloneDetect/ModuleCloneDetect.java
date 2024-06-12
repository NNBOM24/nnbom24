package cloneDetect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import global.Data;
import global.ModuleInfo;

public class ModuleCloneDetect {
    public void sortAndStoreDataModules(String outputPath){
        List<Map.Entry<Integer, ModuleInfo>> moduleInfoList = new ArrayList<>(Data.allModules.entrySet());

        Collections.sort(moduleInfoList, (entry1, entry2) ->
                Integer.compare(entry1.getValue().getModuleHash(), entry2.getValue().getModuleHash()));


        String outputFile = outputPath+"/ModulesClone.csv";

        File T1writeFile = new File(outputFile);
        try{
            BufferedWriter writeText = new BufferedWriter(new FileWriter(T1writeFile));
            writeText.write("ModuleID,moduleProjectID,modulePath,moduleName,startLine,endLine,moduleHash");
            writeText.newLine(); 
            int cloneClusterNum = 1;
            int tmpModuleHash = moduleInfoList.get(0).getValue().getModuleHash();
            for (Map.Entry<Integer, ModuleInfo> entry : moduleInfoList) {
                ModuleInfo moduleInfo = entry.getValue();
                if (moduleInfo.getModuleHash() != tmpModuleHash) {
                    tmpModuleHash = moduleInfo.getModuleHash();
                    writeText.newLine();
                    cloneClusterNum++;
                }
                writeText.write(
                        moduleInfo.getModuleID()+","+
                        moduleInfo.getModuleProjectID()+",\""+
                        moduleInfo.getModulePath()+"\","+
                        moduleInfo.getModuleName()+","+
                        moduleInfo.getStartLine()+","+
                        moduleInfo.getEndLine() + "," +
                        moduleInfo.getModuleHash()
                    );
                writeText.newLine();
            }
            writeText.flush();
            writeText.close();
            System.out.println("Clone cluster number: "+cloneClusterNum);
        }catch (FileNotFoundException e){
            System.out.println("Path don't exist: "+outputFile);
        }catch (IOException e){
            System.out.println("File write/read error!");
        }

    }
}
