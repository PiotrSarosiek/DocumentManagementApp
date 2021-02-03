package Model.Report;

import Model.Product.Product;
import java.text.NumberFormat;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Document;

public class PDFBuilder implements FinanceReportBuilder {

    private Document document;
    private PdfPTable table;
    private PdfWriter writer;
    public PDFBuilder() {
        document = new Document();
        try{
            LocalDateTime local = LocalDateTime.now();
            String fileName = "D:/Docuemnts_for_ztp/"+LocalDate.now()+'_'+local.getHour()+'.'+local.getMinute()+'.'+local.getSecond()+".pdf";
            writer = PdfWriter.getInstance(document, (OutputStream) new FileOutputStream(fileName));
            document.open();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTableHeaders(String[] tab){
        (table = new PdfPTable(tab.length)).setSpacingBefore(10.0f);
        table.setSpacingAfter(10.0f);
        for(String tab2 : tab){
            PdfPCell cell = new PdfPCell((Phrase)new Paragraph(tab2));
            table.addCell((cell));
        }
    }

    @Override
    public void addDataToRow(Product product){
        PdfPCell name = new PdfPCell((Phrase)new Paragraph(product.getName()));
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        String quantityString = nf.format(product.getQuantity());
        String priceVat = nf.format(product.getPriceNoVat());
        PdfPCell amount = new PdfPCell((Phrase)new Paragraph(quantityString));
        PdfPCell netPrice = new PdfPCell((Phrase)new Paragraph(priceVat));
        table.addCell(name);
        table.addCell(amount);
        table.addCell(netPrice);
    }

    @Override
    public void addStringToFile(String string){
        try{
            document.add((Element)new Paragraph(string));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveFile(){
        document.close();
        writer.close();
    }

    @Override
    public void endTable() {
        try{
            document.add((Element)table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
