package global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public class ProjectInfo {
    private static AtomicInteger nextId = new AtomicInteger(0);
    private int projectID;
    private String projectName;
    private Set<Integer> modulesHash;
    private Set<String> imports;
    //private Set<String> fileAndDirName;
    private Set<String> pretrainedModels;
    private List<String> PYPath;
    // Record which projects this item is called by
    private Set<Integer> beIncludedProjects;
    // Constructor
    public ProjectInfo() {
        this.projectID = nextId.getAndIncrement();
        this.modulesHash = new HashSet<>();
        this.imports = new HashSet<>();
        //this.fileAndDirName = new HashSet<>();
        this.PYPath = new ArrayList<>();
    }

    // Getter and setter methods for 'projectID'
    public int getProjectID() {
        return projectID;
    }


    // Getter and setter methods for 'pretrainedModels'
    public Set<String> getPretrainedModels() {
        return pretrainedModels;
    }

    public void setPretrainedModels(Set<String> pretrainedModels) {
        this.pretrainedModels = pretrainedModels;
    }

    // Getter and setter methods for 'projectName'
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    // Getter and setter methods for 'modulesHash'
    public Set<Integer> getModulesHash() {
        return modulesHash;
    }

    public void setModulesHash(Set<Integer> modulesHash) {
        this.modulesHash = modulesHash;
    }

    // Getter and setter methods for 'imports'
    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    // Getter and setter methods for 'fileAndDirName'
    // public Set<String> getFileAndDirName() {
    //     return fileAndDirName;
    // }

    // public void setFileAndDirName(Set<String> fileAndDirName) {
    //     this.fileAndDirName = fileAndDirName;
    // }

    // Getter and setter methods for 'PYPath'
    public List<String> getPYPath() {
        return PYPath;
    }

    public void setPYPath(List<String> PYPath) {
        this.PYPath = PYPath;
    }

    // Getter and setter methods for 'includeProjects'
    public Set<Integer> getBeIncludedProjects() {
        return beIncludedProjects;
    }

    public void setBeIncludeProjects(Set<Integer> includeProjects) {
        this.beIncludedProjects = includeProjects;
    }
    

    
}
