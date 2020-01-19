package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product
{
	private final String name;
	private final BigDecimal price;
	private final BigDecimal taxPercent;

	protected Product(String name, BigDecimal price, BigDecimal tax)
	{
		if(name == null || name.isEmpty()) throw new IllegalArgumentException();
		else this.name = name;
		if(price == null || price.compareTo(new BigDecimal("0")) < 0) throw new IllegalArgumentException();
		else this.price = price;
		this.taxPercent = tax;
	}

	public String getName()
	{
		return this.name;
	}

	public BigDecimal getPrice()
	{
		return this.price;
	}

	public BigDecimal getTaxPercent()
	{
		return this.taxPercent;
	}

	public BigDecimal getPriceWithTax()
	{
		return this.price.add(this.price.multiply(this.taxPercent));
	}
}
