package vn.tutn;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaParserApp {

  public static void main(String[] args) {

    JavaParserApp app = new JavaParserApp();
    app.demo();

  }

  private void demo() {

    final String FINDING_SERVICE = "CL1015001_01_SERVICE";

    try {
      // Path path = Paths.get("E:\\Data\\Desktop\\TechExpert\\tmp\\poi-demo\\src\\main\\resources\\CL1015001_00_000Controller.java");
      Path path = Paths.get(
          this.getClass().getClassLoader().getResource("CL1015001_00_000Controller.java").toURI());

      // Read source code
      String sourceCode = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

      // Parse
      CompilationUnit compilationUnit = StaticJavaParser.parse(sourceCode);

      // Analyse
      compilationUnit
          .findAll(MethodDeclaration.class)
          .stream()
          .filter(method -> method.getBody().toString().contains(FINDING_SERVICE))
          .forEach(method -> System.out.println(method.getName()));

    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

}
