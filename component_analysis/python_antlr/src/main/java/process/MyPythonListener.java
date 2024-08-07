package process;

import org.antlr.v4.runtime.RuleContext;

import antlr4py.Python3Parser;
import antlr4py.Python3ParserBaseListener;




public class MyPythonListener extends Python3ParserBaseListener {
    @Override
    public void enterClassdef(Python3Parser.ClassdefContext ctx) {
        String className = ctx.getText();
        String superClass = null;

        if (ctx.arglist() != null && ctx.arglist().argument().size() > 0) {
            superClass = ((RuleContext) ctx.arglist().argument(0).test()).getText();
        }

        System.out.println("Found class: " + className);

        if (superClass != null) {
            System.out.println("  Superclass: " + superClass);
        }
    }
}