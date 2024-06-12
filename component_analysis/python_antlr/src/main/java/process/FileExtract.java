package process;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import global.Data;
import global.ProjectInfo;
import utils.PythonExtract;
import utils.myUtil;

public class FileExtract {

    public void setAllProjectsPYPath(String path) {
        HashMap<String, List<String>> projectPYPath = getAllProjectPYFiles(path);
        for (Map.Entry<String, List<String>> entry : projectPYPath.entrySet() ) {
            String projectName = entry.getKey();
            ProjectInfo tmpProject = new ProjectInfo();
            tmpProject.setProjectName(projectName);
            tmpProject.setPYPath(entry.getValue());
            
            // Set<String> fileAndDirName = getPYNameFromPYPath(path.length(), tmpProject.getPYPath());
            // tmpProject.setFileAndDirName(fileAndDirName);
            Data.allProjects.put(tmpProject.getProjectID(), tmpProject);
        }
    }

    public ArrayList<String> getProjectPath(String path) {
        File file = new File(path);
        ArrayList<String> fileList = new ArrayList<>();
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {  
                fileList.add(f.getPath());
            } 
        }
        return fileList;
    }

    public ArrayList<String> getFileFromOneProject(String Path) {
        PythonExtract pe = new PythonExtract();
        ArrayList<String> pyFile = pe.getDirectory(Path);
        return pyFile;
    }

    public HashMap<String, List<String>> getAllProjectPYFiles(String Path) {
        HashMap<String, List<String>> allProjectPYFiles = new HashMap<>();
        List<String> projectList = getProjectPath(Path);
        for (String project : projectList) { 
            List<String> pyList = getFileFromOneProject(project);
            String pureProject = myUtil.getFileNameWithoutExtension(project);
            allProjectPYFiles.put(pureProject, pyList);
        }
        return allProjectPYFiles;
    }

    public Set<String> getPYNameFromPYPath(int basePathLen, List<String> PYPath) {
        Set<String> fileAndDirName = new HashSet<>();
        for (String str : PYPath) {
            String relativePath = str.substring(basePathLen);
            Path path = Paths.get(relativePath);
            Path parentPath = path.getParent();
            if (parentPath != null) {
                for (Path folder : parentPath) {
                    //System.out.println("Folder Name: " + folder.toString());
                    fileAndDirName.add(folder.toString());
                }
            }
            fileAndDirName.add(myUtil.getFileNameWithoutExtension(str));
        }
        return fileAndDirName;
    }

}
