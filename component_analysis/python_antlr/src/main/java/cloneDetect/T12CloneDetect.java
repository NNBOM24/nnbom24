package cloneDetect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import global.Data;
import global.ProjectInfo;

public class T12CloneDetect {
    class Pair implements Comparable<Pair> {
        private final int first;
        private final int second;
    
        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    
        public int getFirst() {
            return first;
        }
    
        public int getSecond() {
            return second;
        }

        @Override
        public int compareTo(Pair other) {
            int result = Integer.compare(this.second, other.second);
            if (result == 0) {
                result = Integer.compare(this.first, other.first);
            }
            
            return result;
        }
    }
    
    private List<Pair> moduleHashlist = new ArrayList<>();

    public void setAllProBeIncludedInfo() {
        moduleHashListInit();
        Collections.sort(moduleHashlist);
        convertClone2BeIncludeProjects();
    }
    
    private void moduleHashListInit() {
        for (Map.Entry<Integer, ProjectInfo> entry : Data.allProjects.entrySet()) {
            int projectID = entry.getKey();
            for (int moduleHash : entry.getValue().getModulesHash()) {
                moduleHashlist.add(new Pair(projectID, moduleHash));
            }
        }
    }


    private void convertClone2BeIncludeProjects() {
        Pair firstPair = moduleHashlist.get(0);
        int earliestCreationTime = firstPair.getFirst();
        int earliestCreationID = firstPair.getFirst();
        List<Integer> sameProject = new ArrayList<>();
        sameProject.add(firstPair.getFirst());
        for (int index = 1; index < moduleHashlist.size(); index++) {
            Pair secondPair = moduleHashlist.get(index);
            if (firstPair.getSecond() == secondPair.getSecond()) {
                sameProject.add(secondPair.getFirst());
                if (secondPair.getFirst() < earliestCreationTime) {
                    earliestCreationTime = secondPair.getFirst();
                    earliestCreationID = secondPair.getFirst();
                }
            } else {
                Set<Integer> tmpBeInclude = Data.allProjects.get(earliestCreationID).getBeIncludedProjects();
                for (int tmpID : sameProject) {
                    if (tmpID != earliestCreationID) {
                        tmpBeInclude.add(tmpID);
                    }
                }
                Data.allProjects.get(earliestCreationID).setBeIncludeProjects(tmpBeInclude);
            }
        }
        if (sameProject.size() > 1) {
            Set<Integer> tmpBeInclude = Data.allProjects.get(earliestCreationID).getBeIncludedProjects();
            for (int tmpID : sameProject) {
                if (tmpID != earliestCreationID) {
                    tmpBeInclude.add(tmpID);
                }
            }
            Data.allProjects.get(earliestCreationID).setBeIncludeProjects(tmpBeInclude);
        }
    }
}
