
import java.util.concurrent.CountDownLatch;

import cloneDetect.ModuleCloneDetect;
import global.Data;
import process.FileExtract;
import process.Preprocess;
import utils.myUtil;

public class Main { 
    private static String basePath = "../../../test_project/";
    private static String outputPath = "../../../test_project_output";

    
    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        System.out.println("Start time: " + startTime);

        Data.basePath = basePath;
        Data.basePathLength = Data.basePath.length();
        Data.outputPath = outputPath;
        int threadNum = Data.threadNum;

        Data.allModulesInit();


        FileExtract fe = new FileExtract();
        fe.setAllProjectsPYPath(Data.basePath);

        long endTime   = System.currentTimeMillis();
        System.out.println("FileExtract time: "+ (endTime-startTime)/1000f);
        printMemoryCondition();

        // process all projects
        int projectNum = Data.allProjects.size();
        int divProjectNum = (projectNum + threadNum - 1) / threadNum;
        Preprocess[] processes = new Preprocess[threadNum];
        final CountDownLatch latch1 = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum-1; i++) {
            processes[i] = new Preprocess(i*divProjectNum, Math.min((i+1)*divProjectNum, projectNum), latch1, i);
            processes[i].t.start();
        }
        processes[threadNum-1] = new Preprocess((threadNum-1)*divProjectNum, projectNum, latch1, threadNum-1);
        processes[threadNum-1].t.start();
        try {
            latch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        myUtil.storeProjectInfo(Data.outputPath);
        myUtil.storeModulesInfo(Data.outputPath);
        Data.mergeAllModulesList();
        endTime = System.currentTimeMillis();
        System.out.println("Preprocess time: "+ (endTime-startTime)/1000f);
        printMemoryCondition();
        

        // clone detect
        ModuleCloneDetect moduleCloneDetect = new ModuleCloneDetect();
        moduleCloneDetect.sortAndStoreDataModules(Data.outputPath);
        endTime = System.currentTimeMillis();
        System.out.println("CloneDetect time: "+ (endTime-startTime)/1000f);

    }

    // print memory condition
    private static void printMemoryCondition() {
        System.out.println("Memory consumption"+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024); 
        System.out.println("max memory"+Runtime.getRuntime().maxMemory()/1024/1024); 
        System.out.println("total memory"+Runtime.getRuntime().totalMemory()/1024/1024); 
        System.out.println("free memory"+Runtime.getRuntime().freeMemory()/1024/1024); 
    }


}
