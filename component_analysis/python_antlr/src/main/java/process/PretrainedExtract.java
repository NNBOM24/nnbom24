package process;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr4py.Python3Parser;

public class PretrainedExtract {

    public Set<String> getPretrainedModels(ParseTree tree, List<Token> tokens) {
        Set<String> models = new HashSet<>();
        Map<String, String> literaryTable = getLiteraryTable(tree);
        models = pretrainedModelExtract(tokens, literaryTable);
        return models;
    }

    //Get pretrain model
    private Set<String> pretrainedModelExtract(List<Token> tokens, Map<String, String> literaryTab) {
        Set<String> res = new HashSet<>();

        for (int i = 0; i < tokens.size()-4; i++) {
            Token tmpToken = tokens.get(i);
            if(tmpToken.getType() == 3) { 
                if (tmpToken.getText().equals("load")) {
                    if (i > 1 && tokens.get(i-2).getText().equals("hub")) {
                        if(i > 3 && tokens.get(i-4).getText().equals("torch"))
                            res.add(getModelName(tokens.get(i+2), literaryTab)+getModelName(tokens.get(i+4), literaryTab));
                        else
                            res.add(getModelName(tokens.get(i+2), literaryTab));
                    }
                }
                else if (tokens.get(i+2).getText().equals("serve")) {
                    res.add(getModelName(tokens.get(i+4), literaryTab));
                }
                else if (tokens.get(i-2).getText().equals("ctranslate2")) {
                    res.add(getModelName(tokens.get(i+2), literaryTab));
                }
                else if (tokens.get(i+3).getType() == 59 || tokens.get(i+3).getType() == 60) {
                    res.add(getModelName(tokens.get(i+2), literaryTab));
                }
                else if (tokens.get(i+3).getType() == 64) { 
                    res.add(getModelName(tokens.get(i+4), literaryTab));
                } 
                else if (tokens.get(i+2).getType() == 46) {
                    res.add(tokens.get(i+2).getText());
                }
            }
        }
        // if (res.contains("types_")) {System.out.println(tokens.toString());}
        return res;
    }

    private String getModelName(Token token, Map<String, String> literaryTab) {
        String res;
        if (token.getType() != 46) { //if token is not a variable
            res = token.getText();
        } else {
            res = literaryTab.get(token.getText());
        }
        return res;
    }

    private Map<String, String> getLiteraryTable(ParseTree root) {
        Map<String, String> literaryTable = new HashMap<>();
        if (root == null) return literaryTable;

        Stack<ParseTree> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            ParseTree current = stack.pop();

            if (current instanceof ParserRuleContext) {
                ParserRuleContext ctx = (ParserRuleContext) current;
                if (ctx instanceof Python3Parser.String_stmtContext) {
                    String[] tmpSplit = ctx.getText().split("=");
                    literaryTable.put(tmpSplit[0], tmpSplit[1]);
                    // System.out.println("Literal_exprContext: " + ctx.getText());
                }
            } 

            int childNodeNum = current.getChildCount();
            for (int i = childNodeNum - 1; i >= 0; i--) {
                stack.push(current.getChild(i));
            }
        }

        return literaryTable;
    }
}
