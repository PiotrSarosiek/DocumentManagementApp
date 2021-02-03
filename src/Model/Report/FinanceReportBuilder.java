package Model.Report;

import Model.Product.Product;

public interface FinanceReportBuilder {

      void addTableHeaders(String[] table);

        void addDataToRow(Product product);

        void addStringToFile(String string);

        void saveFile();

        void endTable();
}
