package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice
{
	private Collection<Product> products;

	public void addProduct(Product product)
	{
		private Collection<Product> products = new ArrayList<>();

	public void addProduct(Product product)
	{
		if(product != null) products.add(product);
	}

	public void addProduct(Product product, Integer quantity)
	{
		if(quantity == null || quantity <= 0) throw new IllegalArgumentException();
		else for(int i = 0; i < quantity; i++)
		{
			this.addProduct(product);
		}
	}

	public BigDecimal getSubtotal()
	{
		BigDecimal sum = BigDecimal.ZERO;

		if(products.size() > 0 && products != null)
		{
			for(Product product : products)
			{
				sum.add(product.getPrice());
			}
		}

		for(Product product : this.products)
		{
			sum = sum.add(product.getPrice());
		}

		return sum;
	}

	public BigDecimal getTax()
	{
		BigDecimal sumWithTax = BigDecimal.ZERO

		for(Product product : this.products)
		{
			sumWithTax = sumWithTax.add(product.getPrice().multiply(product.getTaxPercent()));
		}

		return sumWithTax;
	}

	public BigDecimal getTotal()
	{
		return getSubtotal().add(getTax());
	}
}
