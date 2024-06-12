package process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;

import global.Data;
import global.ModuleInfo;

public class ModuleExtract {

    public void setNNModulesName(ParseTree tree, List<String> ruleNamesList, List<Token> tokens, Set<String> modulesName) {
        Queue<ParseTree> visteQueue = new LinkedList<>();
        visteQueue.offer(tree);
        int classDecHash = "classdef".hashCode();
        while(!visteQueue.isEmpty()) {
            ParseTree firstEle = visteQueue.poll();
            int firstEleHash = Trees.getNodeText(firstEle, ruleNamesList).hashCode();
            if (firstEleHash == classDecHash) {
                Interval tmpInterval = firstEle.getSourceInterval();
                int sIndex = tmpInterval.a;
                int eIndex = tmpInterval.b;
                boolean flag = false;
                if(eIndex - sIndex < 8) continue;
                if(tokens.get(sIndex + 2).getText().equals("(")) {
                    for (int i = sIndex+3; i < eIndex; i++) {
                        String str = tokens.get(i).getText();
                        if(str.equals(")")) {
                            String baseClass = tokens.get(i-1).getText();
                            if (!"Module".equals(baseClass) &&
                                !modulesName.contains(baseClass)) {
                                flag = true;
                            }
                            break;
                        }
                    }
                } else {
                    continue;
                }
                if (flag) continue;

                // if (sIndex + 5 < tokens.size()) {
                //     String moduleToken1 = tokens.get(sIndex + 3).getText();
                //     String moduleToken2 = tokens.get(sIndex + 5).getText();
                //     if (!"Module".equals(moduleToken1) &&
                //         !"Module".equals(moduleToken2) &&
                //         !modulesName.contains(moduleToken1)) {
                //         continue;
                //     }
                // }
                modulesName.add(tokens.get(sIndex + 1).getText());
            } else {
                int tmpChildrenNum = firstEle.getChildCount();
                for (int i = 0; i < tmpChildrenNum; i++) {
                    if (firstEle.getChild(i).getChildCount() != 0) {
                        visteQueue.offer(firstEle.getChild(i));
                    }
                }
            }
        }
    }


    public void getNNModules(ParseTree tree, List<String> ruleNamesList, List<Token> tokens, Set<String> modulesName, String path, int projectID, int threadID) {
        //Set<Integer> modulesHash = new HashSet<>();
        Queue<ParseTree> visteQueue = new LinkedList<>();
        visteQueue.offer(tree);
        int classDecHash = "classdef".hashCode();
        // INDENT, DEDENT and NewLine
        Set<Integer> meaninglessTypes = new HashSet<>(Arrays.asList(1, 2, 45));
        while(!visteQueue.isEmpty()) {
            ParseTree firstEle = visteQueue.poll();
            int firstEleHash = Trees.getNodeText(firstEle, ruleNamesList).hashCode();
            if (firstEleHash == classDecHash) {
                Interval tmpInterval = firstEle.getSourceInterval();
                int sIndex = tmpInterval.a;
                int eIndex = tmpInterval.b;
                short startLine = (short) tokens.get(sIndex).getLine();
                short endLine = (short) tokens.get(eIndex).getLine();

                if(eIndex - sIndex < Data.minTokenNum) continue;

                boolean flag = false;
                if(tokens.get(sIndex + 2).getText().equals("(")) {
                    for (int i = sIndex+3; i < eIndex; i++) {
                        String str = tokens.get(i).getText();
                        if(str.equals(")")) {
                            String baseClass = tokens.get(i-1).getText();
                            if (!"Module".equals(baseClass) &&
                                !modulesName.contains(baseClass)) {
                                flag = true;
                            }
                            break;
                        }
                    }
                } else {
                    continue;
                }
                if (flag) continue;

                // if (sIndex + 5 < tokens.size()) {
                //     String moduleToken1 = tokens.get(sIndex + 3).getText();
                //     String moduleToken2 = tokens.get(sIndex + 5).getText();
                //     if (!"Module".equals(moduleToken1) &&
                //         !"Module".equals(moduleToken2) &&
                //         !modulesName.contains(moduleToken1)) {
                //         continue;
                //     }
                // }


                String tmpModuleName = tokens.get(sIndex + 1).getText();
                modulesName.add(tmpModuleName);

                List<Integer> normTokens = new ArrayList<>();
                for (int i = sIndex; i <= eIndex; i++) {
                    Token tmpToken = tokens.get(i);
                    if(meaninglessTypes.contains(tmpToken.getType())) {
                        continue;
                    }
                    normTokens.add(tmpToken.getType());
                }
                // Filter out meaningless modules
                if (normTokens.size() < Data.minTokenNum) continue;
                else {
                    int tmpHash = normTokens.hashCode();
                    //modulesHash.add(tmpHash);
                    ModuleInfo tmpModuleInfo = new ModuleInfo();
                    tmpModuleInfo.setStartLine(startLine);
                    tmpModuleInfo.setEndLine(endLine);
                    tmpModuleInfo.setModuleHash(tmpHash);
                    tmpModuleInfo.setModulePath(path.substring(Data.basePathLength));
                    tmpModuleInfo.setModuleName(tmpModuleName);
                    tmpModuleInfo.setModuleProjectID(projectID);
                    Data.allModulesList.get(threadID).put(tmpModuleInfo.getModuleID(), tmpModuleInfo);
                }
                
            } else {
                int tmpChildrenNum = firstEle.getChildCount();
                for (int i = 0; i < tmpChildrenNum; i++) {
                    if (firstEle.getChild(i).getChildCount() != 0) {
                        visteQueue.offer(firstEle.getChild(i));
                    }
                }
            }
        }

        //return modulesHash;
        return;
    }

}
