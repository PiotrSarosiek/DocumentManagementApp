package Model.Order;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

public class OrderWithPercentageDiscount extends OrderDecorator{
    public OrderWithPercentageDiscount(IOrder iOrder){
        super(iOrder);
    }
    @Override
    public double getSumPriceNoVat(){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        try {
            return DecimalFormat.getNumberInstance().parse(nf.format(super.getSumPriceNoVat()*0.923)).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return super.getSumPriceNoVat()*0.923;
    }
    @Override
    public double getSumPriceVat(){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        try {
            return DecimalFormat.getNumberInstance().parse(nf.format(super.getSumPriceVat()*0.90)).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return super.getSumPriceVat()*0.90;
    }
}
