package Model.Order;

public class OrderWithNomianalDiscount extends OrderDecorator{
    public OrderWithNomianalDiscount(IOrder iOrder){
        super(iOrder);
    }
    @Override
    public double getSumPriceNoVat(){
        if(super.getSumPriceNoVat()>77)
        return super.getSumPriceNoVat()-77;
        return super.getSumPriceNoVat();
    }
    @Override
    public double getSumPriceVat(){
        if(super.getSumPriceVat()>100)
            return super.getSumPriceVat()-100;
        return super.getSumPriceVat();
    }
}
