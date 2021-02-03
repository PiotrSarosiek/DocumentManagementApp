package Model.Report;

import DB.VirtualDataBase;
import Model.Document.Document;
import Model.Product.Product;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class DirectorReport {
    public FinanceReportBuilder reportBuilder;
    public LocalDate reportDate;
    public List<Product> products;
    public List<Product> productsAfterSum;
    public List<Document> documents;
    public double totalPrice;

    public DirectorReport(FinanceReportBuilder reportBuilder) {
        reportDate = LocalDate.now();
        this.reportBuilder = reportBuilder;
        documents=VirtualDataBase.getINSTANCE().getDocuments();
        sumQuantity();
    }

    public void sumQuantity(){
        products=new LinkedList<>();
        productsAfterSum=new LinkedList<>();
        Product tempProduct;
        for(Document document: documents){
            if(document.getDate().isEqual(LocalDate.now())){
                products.addAll(document.getOrder().getProducts());
                totalPrice+=document.getOrder().getSumPriceVatAfterDiscount();
            }
        }
        for(Product product:products){
            if(productsAfterSum.stream().filter(m -> m.getName().equals(product.getName())).findAny().orElse(null)==null) {
                tempProduct = products.stream().filter(m -> m.getName().equals(product.getName())).findAny().orElse(null);
                productsAfterSum.add(tempProduct);
            }
            else {
                tempProduct = products.stream().filter(m -> m.getName().equals(product.getName())).findAny().orElse(null);
                tempProduct.setQuantity(tempProduct.getQuantity()+product.getQuantity());
            }
        }
    }

    public void construct() {
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(5);
                reportBuilder.addStringToFile("Report from: "+reportDate);
                reportBuilder.addStringToFile("");
                reportBuilder.addStringToFile("Total price: "+ totalPrice);
                reportBuilder.addStringToFile("");
                reportBuilder.addStringToFile("List of products sold:");
                reportBuilder.addStringToFile("");
                String[] headers = { "Name", "Quantity", "Price" };
                reportBuilder.addTableHeaders(headers);
                for (int i=0;i<productsAfterSum.size();i++) {
                    reportBuilder.addDataToRow(productsAfterSum.get(i));
                }
                reportBuilder.endTable();
            reportBuilder.saveFile();
        }
    }

