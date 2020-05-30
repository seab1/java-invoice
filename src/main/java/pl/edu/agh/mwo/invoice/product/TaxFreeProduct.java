package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class TaxFreeProduct extends Product {
    public TaxFreeProduct(String name, BigDecimal price) {
        super(name, price, BigDecimal.ZERO);
    }
    
    public TaxFreeProduct(String name, BigDecimal price, BigDecimal excise) {
        super(name, price, BigDecimal.ZERO, excise);
    }
}
