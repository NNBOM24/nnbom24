package process;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr4py.Python3Parser;

public class ImportExtract {

    public Set<String> getImports(ParseTree tree) {
        Set<String> allImports = new HashSet<>();   
        allImports = depthFirstOrder(tree);
        return allImports;
    }

    private Set<String> depthFirstOrder(ParseTree root) {
        Set<String> allImports = new HashSet<>();
        if (root == null) return allImports;
        Stack<ParseTree> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            ParseTree current = stack.pop();
            if (current instanceof ParserRuleContext) {
                ParserRuleContext ctx = (ParserRuleContext) current;
                if (ctx instanceof Python3Parser.Dotted_nameContext) {
                    //System.out.println("Entering rule: " + ctx.getText());
                    String baseImport = ctx.getText().split("\\.")[0];
                    allImports.add(baseImport);
                }
            } 
            int childNodeNum = current.getChildCount();
            for (int i = childNodeNum - 1; i >= 0; i--) {
                stack.push(current.getChild(i));
            }
        }
        return allImports;
    }


}
