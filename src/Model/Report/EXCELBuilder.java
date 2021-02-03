package Model.Report;

import Model.Product.Product;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EXCELBuilder implements FinanceReportBuilder {

    private Sheet sheet;
    private int actuallyRow;
    private Workbook workbook;

    public EXCELBuilder() {
        workbook = (Workbook)new XSSFWorkbook();
        sheet = workbook.createSheet("Raport");
        actuallyRow = 0;
    }

    @Override
    public void saveFile() {
        try {
            LocalDateTime local = LocalDateTime.now();
            String fileName = "D:/Docuemnts_for_ztp/"+LocalDate.now()+'_'+local.getHour()+'.'+local.getMinute()+'.'+local.getSecond()+".xlsx";
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write((OutputStream)fos);
            fos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endTable() {
        ++actuallyRow;
    }

    @Override
    public void addStringToFile(String string) {
        sheet.createRow(actuallyRow);
        sheet.getRow(actuallyRow).createCell(0).setCellValue(string);
        ++actuallyRow;
    }

    @Override
    public void addTableHeaders(String[] table) {
        sheet.createRow(actuallyRow);
        int i = 0;
        for (String header : table) {
            sheet.getRow(actuallyRow).createCell(i).setCellValue(header);
            ++i;
        }
        ++actuallyRow;
    }

    @Override
    public void addDataToRow(final Product product) {
        sheet.createRow(actuallyRow);
        sheet.getRow(actuallyRow).createCell(0).setCellValue(product.getName());
        sheet.getRow(actuallyRow).createCell(1).setCellValue(product.getQuantity());
        sheet.getRow(actuallyRow).createCell(2).setCellValue((double)product.getPriceNoVat());
        ++actuallyRow;
    }
}
