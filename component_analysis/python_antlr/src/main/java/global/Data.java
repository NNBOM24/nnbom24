package global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Data {
    public static HashMap<Integer, ProjectInfo> allProjects = new HashMap<>();
    public static List<HashMap<Integer, ModuleInfo>> allModulesList = new ArrayList<>(); 
    public static HashMap<Integer, ModuleInfo> allModules = new HashMap<>();
    public static int minTokenNum = 50;
    public static String basePath = "";
    public static String outputPath = "";
    public static int basePathLength = 0;
    public static int threadNum = 1;

    public static void allModulesInit() {
        for (int i = 0; i < threadNum; i++) {
            allModulesList.add(new HashMap<Integer, ModuleInfo>());
        }
    }


    public static void mergeAllModulesList() {
        for (int i = 0; i < threadNum; i++) {
            allModules.putAll(allModulesList.get(i));
            allModulesList.get(i).clear();
        }
        allModulesList.clear();
        allModulesList = null;
    }
}
