package global;

import java.util.concurrent.atomic.AtomicInteger;

public class ModuleInfo {
    private static AtomicInteger nextModuleId = new AtomicInteger(0);
    private int moduleID;
    private int moduleHash;
    private int moduleProjectID;
    private String modulePath;
    private String moduleName;
    private short startLine;
    private short endLine;

    public ModuleInfo() {
        this.moduleID = nextModuleId.getAndIncrement();
    }
    public ModuleInfo(String[] vs) {
        if (vs.length!= 7) {
            return;
        }
        this.moduleID = Integer.parseInt(vs[0]);
        this.moduleProjectID = Integer.parseInt(vs[1]);
        this.modulePath = vs[2];
        this.moduleName = vs[3];
        this.startLine = Short.parseShort(vs[4]);
        this.endLine = Short.parseShort(vs[5]);
        this.moduleHash = Integer.parseInt(vs[6]);

    }

    public int getModuleID() {
        return moduleID;
    }

    public int getModuleHash() {
        return moduleHash;
    }

    public void setModuleHash(int moduleHash) {
        this.moduleHash = moduleHash;
    }

    public int getModuleProjectID() {
        return moduleProjectID;
    }

    public void setModuleProjectID(int projectID) {
        this.moduleProjectID = projectID;
    }

    public String getModulePath() {
        return modulePath;
    }
    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }
    public String getModuleName() {
        return moduleName;
    }
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    public short getStartLine() {
        return startLine;
    }
    public void setStartLine(short startLine) {
        this.startLine = startLine;
    }
    public short getEndLine() {
        return endLine;
    }
    public void setEndLine(short endLine) {
        this.endLine = endLine;
    }

}