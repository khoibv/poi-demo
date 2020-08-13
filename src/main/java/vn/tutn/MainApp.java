package vn.tutn;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainApp {

  public static void main(String[] args) {
    String fileName = "E:\\Data\\Desktop\\Sample.xlsx";

    System.out.println("Start");

    try (
        FileInputStream excelFile = new FileInputStream(fileName);
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {

      for (Sheet sheet : workbook) {
        System.out.println(sheet.getSheetName());
      }

      System.out.println("Completed");
    } catch (IOException ioe) {
      System.out.println(ioe);
    }
  }
}
