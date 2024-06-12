package process;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr4py.Python3Lexer;
import antlr4py.Python3Parser;
import global.Data;
import global.ProjectInfo;

class parseThreeTuple {
    ParseTree tree;
    List<Token> tokens;
    List<String> ruleNamesList;
    String path;
    public parseThreeTuple(ParseTree tree, List<Token> tokens, List<String> ruleNamesList, String path) {
        this.tree = tree;
        this.tokens = tokens;
        this.ruleNamesList = ruleNamesList;
        this.path = path;
    }
}


public class Preprocess extends Thread {
    public Thread t;
    int start_project;
    int end_project;
    int threadID;
    private final CountDownLatch latch;
    public Preprocess(int start_project, int end_project, CountDownLatch latch, int threadID) {
        this.start_project = start_project;
        this.end_project = end_project;
        this.latch = latch;
        this.threadID = threadID;
        t = new Thread(this, "Preprocess: "+start_project+"-"+end_project);
    }



    @Override
    public void run(){
        for (int i = start_project; i < end_project; i++) {
            ProjectInfo tmpProject = Data.allProjects.get(i);
            setOneProjectInfo(tmpProject, threadID);
            if ((i - start_project + 1)% 100 == 0) {
                System.out.println("Thread "+threadID+" processing has processed "+ (i - start_project) + " project.");
                printMemoryCondition();
            }
        }
        System.out.println("Thread "+threadID+" over!");
        latch.countDown();
    }

    // public void setAllProjectsInfo() {
    //     for (ProjectInfo tmpProject : Data.allProjects.values()) {
    //         setOneProjectInfo(tmpProject);
    //     }
    // }

    private static void printMemoryCondition() {
        System.out.println("Memory consumption"+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024/1024); 
        System.out.println("max memory"+Runtime.getRuntime().maxMemory()/1024/1024); 
        System.out.println("total memory"+Runtime.getRuntime().totalMemory()/1024/1024); 
        System.out.println("free memory"+Runtime.getRuntime().freeMemory()/1024/1024); 
    }


    // Get three types of composition
    private void setOneProjectInfo(ProjectInfo tmpProject, int threadID) {
        List<String> pyPath = tmpProject.getPYPath();
        // store results
        Set<String> imports = new HashSet<>();
        Set<String> pretrainedModels = new HashSet<>();
        Set<Integer> modules = new HashSet<>();
        Set<String> modulesName = new HashSet<>();
        ImportExtract importExtract = new ImportExtract();
        PretrainedExtract pretrainedExtract = new PretrainedExtract();
        ModuleExtract moduleExtract = new ModuleExtract();

        // List<parseThreeTuple> parseThreeTupleSet = new LinkedList<>();


        // Parse python file.
        for(String path : pyPath) {
            // file parse
            ParseTree tree = null;
            List<Token> tokens = null;
            List<String> ruleNamesList = null;
            try {
                CharStream input = CharStreams.fromFileName(path);
                Lexer lexer = new Python3Lexer(input);
                CommonTokenStream tokenStream = new CommonTokenStream(lexer);
                tokenStream.fill();
                tokens = tokenStream.getTokens();
                Python3Parser parser = new Python3Parser(tokenStream);
                parser.removeErrorListeners();
                parser.addErrorListener(new ANTLRErrorListener() {
    
                    @Override
                    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                            int charPositionInLine, String msg, RecognitionException e) {
                        // TODO Auto-generated method stub
                        
                    }
    
                    @Override
                    public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact,
                            BitSet ambigAlts, ATNConfigSet configs) {
                        // TODO Auto-generated method stub
                        
                    }
    
                    @Override
                    public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                            BitSet conflictingAlts, ATNConfigSet configs) {
                        // TODO Auto-generated method stub
                        
                    }
    
                    @Override
                    public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex,
                            int prediction, ATNConfigSet configs) {
                        // TODO Auto-generated method stub
                        
                    }
                    
                });
                tree = parser.file_input();
                ruleNamesList = Arrays.asList(parser.getRuleNames());
                //parseThreeTupleSet.add(new parseThreeTuple(tree, tokens, ruleNamesList, path));
                moduleExtract.getNNModules(tree, ruleNamesList, tokens, modulesName, path, tmpProject.getProjectID(), threadID);
                //get import infomation
                imports.addAll(importExtract.getImports(tree));

                //get Pretrained model
                pretrainedModels.addAll(pretrainedExtract.getPretrainedModels(tree, tokens));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        tmpProject.setPYPath(null);

        // int currentModulesNum = modulesName.size();
        // Get all modules, which are extended from nn.Modules.
        // while (true) {
        //get Modules Name
        // for (parseThreeTuple parseThreeTuple : parseThreeTupleSet) {
        //     moduleExtract.setNNModulesName(parseThreeTuple.tree, parseThreeTuple.ruleNamesList, parseThreeTuple.tokens, modulesName);
        // }
        // if (modulesName.size() == currentModulesNum) {
            //     break;
            // } else {
            //     currentModulesNum = modulesName.size();
            // }
        //}

        // for(parseThreeTuple parseThreeTuple : parseThreeTupleSet) {
            
        //     // get import infomation
        //     // imports.addAll(importExtract.getImports(parseThreeTuple.tree));

        //     //get Pretrained model
        //     // pretrainedModels.addAll(pretrainedExtract.getPretrainedModels(parseThreeTuple.tree, parseThreeTuple.tokens));

        //     //get Modules
        //     // modules.addAll(moduleExtract.getNNModules(tree, ruleNamesList, tokens, modulesName, path, tmpProject.getProjectID(), threadID));
        //     moduleExtract.getNNModules(parseThreeTuple.tree, parseThreeTuple.ruleNamesList, parseThreeTuple.tokens, modulesName, parseThreeTuple.path, tmpProject.getProjectID(), threadID);
        // }

        tmpProject.setImports(imports);
        tmpProject.setPretrainedModels(pretrainedModels);
        tmpProject.setModulesHash(modules);

    }

    // private void setOneProjectsImportInfo(ProjectInfo tmpProject) {
    //     List<String> pyPath = tmpProject.getPYPath();
    //     ImportExtract importExtract = new ImportExtract();
    //     Set<String> imports = new HashSet<>();
    //     for(String path : pyPath) {
    //         imports.addAll(importExtract.getImports(path)) ;
    //     }
    //     tmpProject.setImports(imports);
    // }

    
}


