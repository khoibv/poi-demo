package vn.tutn;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class JavaParserApp {

  public static void main(String[] args) {

    JavaParserApp app = new JavaParserApp();
    app.demo();


  }

  private void demo() {

    String sourceCode = ""
        + "class A {"
        + ""
        + "   public void testMethod1() {"
        + "      int x = 1;"
        + "   }"
        + ""
        + "   public void testMethod2() {"
        + "      String y = \"hello world\";"
        + "   }"
        + ""
        + "}";

    CompilationUnit compilationUnit = StaticJavaParser.parse(sourceCode);
    compilationUnit
        .findAll(MethodDeclaration.class)
        .stream()
        .filter(method -> method.getBody().toString().contains("hello world"))
        .forEach(System.out::println);

  }

}
