package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.Product;

public class ProductTest {
    @Test
    public void testProductNameIsCorrect() {
        Product product = new OtherProduct("Buty", new BigDecimal("100.0"));
        Assert.assertEquals("Buty", product.getName());
    }

    @Test
    public void testProductPriceAndTaxWithDefaultTax() {
        Product product = new OtherProduct("Ogorki", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.23"), Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testProductPriceAndTaxWithDairyProduct() {
        Product product = new DairyProduct("Szarlotka", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
        Assert.assertThat(new BigDecimal("0.08"), Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testPriceWithTax() {
        Product product = new DairyProduct("Oscypek", new BigDecimal("100.0"));
        Assert.assertThat(new BigDecimal("108"), Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullName() {
        new OtherProduct(null, new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithEmptyName() {
        new TaxFreeProduct("", new BigDecimal("100.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNullPrice() {
        new DairyProduct("Banany", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProductWithNegativePrice() {
        new TaxFreeProduct("Mandarynki", new BigDecimal("-1.00"));
    }
    
    @Test
    public void testExciseWithOtherProduct()
    {
        Product product = new OtherProduct("Pomidory w puszce", new BigDecimal("2"));
        
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(product.getExcise()));
    }

    @Test
    public void testExciseWithTaxFreeProduct()
    {    
        Product product = new TaxFreeProduct("Powietrze w puszce", new BigDecimal("100"));
        
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(product.getExcise()));
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(product.getTaxPercent()));
    }

    @Test
    public void testExciseTaxPercentAndPriceWithAlcoholProduct()
    {
        Product product = new AlcoholProduct("Woda ognista", new BigDecimal("18"));
        
        Assert.assertThat(new BigDecimal("5.56"), Matchers.comparesEqualTo(product.getExcise()));
        Assert.assertThat(new BigDecimal("0.23"), Matchers.comparesEqualTo(product.getTaxPercent()));
        Assert.assertThat(new BigDecimal("18"), Matchers.comparesEqualTo(product.getPrice()));
    }

    @Test
    public void testExciseAndTaxPercentWithFuelProduct()
    {
        Product product = new FuelProduct("Benzyna", new BigDecimal("100"));
        
        Assert.assertThat(new BigDecimal("5.56"), Matchers.comparesEqualTo(product.getExcise()));
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(product.getTaxPercent()));
        Assert.assertThat(new BigDecimal("100"), Matchers.comparesEqualTo(product.getPrice()));
    }

    @Test
    public void testPriceWithAlcoholProduct()
    {
        final BigDecimal priceWithTax = new BigDecimal("20").add(new BigDecimal("20").multiply(new BigDecimal("0.23"))).add(new BigDecimal("5.56"));

        Product product = new AlcoholProduct("Piwo", new BigDecimal("20"));
        Assert.assertThat(priceWithTax, Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test
    public void testPriceWithFuelProduct()
    {
        final BigDecimal priceWithTax = new BigDecimal("1200").add(new BigDecimal("1200").multiply(BigDecimal.ZERO)).add(new BigDecimal("5.56"));
        Product product = new FuelProduct("Paliwo lotnicze", new BigDecimal("1200"));
        
        Assert.assertThat(priceWithTax, Matchers.comparesEqualTo(product.getPriceWithTax()));
    }

    @Test
    public void testSameProductEqualityForDifferentPrices()
    {
        Product product1 = new OtherProduct("Produkt", new BigDecimal("100"));
        Product product2 = new OtherProduct("Produkt", new BigDecimal("200"));

        Assert.assertFalse(product1.equals(product2));
    }

    @Test
    public void testSameProductEqualityForDifferentTaxValues()
    {
        Product product1 = new OtherProduct("Produkt", new BigDecimal("100"));
        Product product2 = new DairyProduct("Produkt", new BigDecimal("100"));

        Assert.assertFalse(product1.equals(product2));
    }

    @Test
    public void testSameProductEqualityForDifferentExciseValues()
    {
        Product product1 = new TaxFreeProduct("Produkt", new BigDecimal("100"));
        Product product2 = new FuelProduct("Produkt", new BigDecimal("100"));

        Assert.assertFalse(product1.equals(product2));
    }
}
