package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class PythonExtract {
    public ArrayList<String> getDirectory(String path) {
        File file = new File(path);
        LinkedList<File> dirList = new LinkedList<>();
        ArrayList<String> fileList = new ArrayList<>();
        getOneDir(file, dirList, fileList);

        File tmp;
        while (!dirList.isEmpty()) {
            tmp = dirList.removeFirst();
            getOneDir(tmp, dirList, fileList);
        }
        return fileList;
    }

    private void getOneDir(File file, LinkedList<File> dirList, ArrayList<String> fileList) {
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {  
                dirList.add(f);
            } else if(f.getName().endsWith(".py")){
                fileList.add(f.getPath());
            }
        }
    }
}
