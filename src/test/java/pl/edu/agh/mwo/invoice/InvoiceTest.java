package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;
import pl.edu.agh.mwo.invoice.product.AlcoholProduct;
import pl.edu.agh.mwo.invoice.product.FuelProduct;

public class InvoiceTest {
    private Invoice invoice;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test
    public void testInvoiceHasNumberGreaterThan0() {
        int number = invoice.getNumber();
        Assert.assertThat(number, Matchers.greaterThan(0));
    }

    @Test
    public void testTwoInvoicesHaveDifferentNumbers() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertNotEquals(number1, number2);
    }

    @Test
    public void testInvoiceDoesNotChangeItsNumber() {
        Assert.assertEquals(invoice.getNumber(), invoice.getNumber());
    }

    @Test
    public void testTheFirstInvoiceNumberIsLowerThanTheSecond() {
        int number1 = new Invoice().getNumber();
        int number2 = new Invoice().getNumber();
        Assert.assertThat(number1, Matchers.lessThan(number2));
    }
    
    @Test
    public void testInvoicePrinterDisplaysCorrectNumberOfLines()
    {
    	invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        
        String invoiceDisplay = invoice.displayMe();
        int numberOfLines = invoiceDisplay.split("\n").length;
        Assert.assertEquals(numberOfLines, invoice.getProducts().size() + 2);
    }
    
    @Test
    public void testInvoicePrinterDisplaysCorrectLayout()
    {
    	invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        
        String[] invoiceDisplay = invoice.displayMe().split("\n");
        String currentLine = "Faktura numer: " + invoice.getNumber();        
        Assert.assertEquals(invoiceDisplay[0], currentLine);
        
        currentLine = "Liczba pozycji: " + invoice.getProducts().size();
    	Assert.assertEquals(invoiceDisplay[invoiceDisplay.length - 1], currentLine);
    	
    	int iterator = 1;
    	for(Map.Entry<Product, Integer> entry : invoice.getProducts().entrySet())
    	{
    		currentLine = "Produkt: " + entry.getKey().getName() + ", Ilosc: " + entry.getValue() + ", Cena: " + entry.getKey().getPrice();
    		Assert.assertEquals(invoiceDisplay[iterator], currentLine);
    		iterator++;
    	}
    	
    	Assert.assertTrue(iterator == invoiceDisplay.length - 1);
    }
    
    @Test
    public void testProductAmountIncreasesWhenAddedSingly()
    {
        Product product = new TaxFreeProduct("Owczy ser", new BigDecimal("30")); 
        
        Integer additions = 3;
        for (int i = 0; i < additions; i++)
        {
            invoice.addProduct(product);
        }
        
        Assert.assertTrue(invoice.getProducts().containsKey(product));
        Assert.assertEquals(additions, invoice.getProducts().get(product));      
    }

    @Test
    public void testProductAmountIncreasesWhenAddedWithQuantity()
    {
    	Product product = new TaxFreeProduct("Owczy ser", new BigDecimal("30"));  

        Integer quantity1 = 3;
        invoice.addProduct(product, quantity1);
        
        Integer quantity2 = 5;
        invoice.addProduct(product, quantity2);

        Assert.assertTrue(invoice.getProducts().containsKey(product));
        Assert.assertEquals(Integer.valueOf(quantity1 + quantity2), invoice.getProducts().get(product));      
    }

    @Test
    public void testNewPositionIsCreatedWhenAddingSameProductWithDifferentPriceSingly()
    {
    	Product product1 = new TaxFreeProduct("Owczy ser", new BigDecimal("30"));
    	Product product2 = new TaxFreeProduct("Owczy ser", new BigDecimal("40"));

        invoice.addProduct(product1);
        invoice.addProduct(product2);
        
        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product2)); 

    }

    @Test
    public void testNewPositionIsCreatedWhenAddingSameProductWithDifferentPriceWithQuantity()
    {
    	Product product1 = new TaxFreeProduct("Owczy ser", new BigDecimal("30"));
    	Product product2 = new TaxFreeProduct("Owczy ser", new BigDecimal("40"));

        Integer quantity1 = 3;
        invoice.addProduct(product1, quantity1);
        
        Integer quantity2 = 5;
        invoice.addProduct(product2, quantity2);

        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(quantity1, invoice.getProducts().get(product1));    
        Assert.assertEquals(quantity2, invoice.getProducts().get(product2));   

    }
    
    @Test
    public void testNewPositionIsCreatedWhenAddingSameProductWithDifferentTaxSingly()
    {
        Product product1 = new DairyProduct("Mleko", new BigDecimal("8"));
        Product product2 = new OtherProduct("Mleko", new BigDecimal("8"));

        invoice.addProduct(product1);
        invoice.addProduct(product2);

        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product2)); 

    }

    @Test
    public void testNewPositionIsCreatedWhenAddingSameProductWithDifferentTaxWithQuantity()
    {
        Product product1 = new DairyProduct("Mleko", new BigDecimal("8"));
        Product product2 = new OtherProduct("Mleko", new BigDecimal("8"));

        Integer quantity1 = 3;
        invoice.addProduct(product1, quantity1);
        
        Integer quantity2 = 5;
        invoice.addProduct(product2, quantity2);

        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(quantity1, invoice.getProducts().get(product1));
        Assert.assertEquals(quantity2, invoice.getProducts().get(product2));
    }

    @Test
    public void testNewPositionIsCreatedWhenAddingSameProductWithDifferentExciseSingly()
    {
        Product product1 = new AlcoholProduct("Spirytus", new BigDecimal("30"));
        Product product2 = new OtherProduct("Spirytus", new BigDecimal("30"));

        invoice.addProduct(product1);
        invoice.addProduct(product2);

        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product1));      
        Assert.assertEquals(Integer.valueOf(1), invoice.getProducts().get(product2));         
    }

    @Test
    public void testNewPositionIsCreatedWhenAddingSameProductWithDifferentExciseWithQuantity()
    {
        Product product1 = new AlcoholProduct("Spirytus", new BigDecimal("30"));
        Product product2 = new OtherProduct("Spirytus", new BigDecimal("30"));

        Integer quantity1 = 3;
        invoice.addProduct(product1, quantity1);
        
        Integer quantity2 = 5;
        invoice.addProduct(product2, quantity2);

        Assert.assertTrue(invoice.getProducts().containsKey(product1));
        Assert.assertTrue(invoice.getProducts().containsKey(product2));
        Assert.assertEquals(2, invoice.getProducts().size());
        Assert.assertEquals(quantity1, invoice.getProducts().get(product1));    
        Assert.assertEquals(quantity2, invoice.getProducts().get(product2));          
    }
}
