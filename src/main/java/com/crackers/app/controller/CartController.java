package com.crackers.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crackers.app.dao.CartDao;
import com.crackers.app.model.Cart;

@Controller
@SessionAttributes("emailid")
public class CartController 
{
	
	@Autowired
	CartDao cartDao;
	
	
	
	@RequestMapping(value = "/AddtoCart", method = RequestMethod.GET)
	public String addCart(@RequestParam int id,@RequestParam int quantity, ModelMap model, RedirectAttributes redirectAttributes)
	{
		String val=(String) model.get("emailid");
		
		if(!(val==null))
		{
			int result = cartDao.save(id,quantity,val);
			if(result==0) 
			{
				redirectAttributes.addFlashAttribute("addCartStatus", false);
			}
			else 
			{
				redirectAttributes.addFlashAttribute("addCartStatus", true);
			}
			return "redirect:/ShowMenuItemListCustomer";
		}
		else
		{
			return "Login";
		}
	}
	
	
	@RequestMapping(value = "/ShowCart", method = RequestMethod.GET)
	public String showCartPage(ModelMap model) 
	{
			String val=(String) model.get("emailid");
			List<Cart> cartItems = cartDao.getCartItems(val);
		
			model.addAttribute("cartItems", cartItems);
			double total=0;
			for(Cart cart:cartItems)
			{
				total+=cart.getCartCrackersPrice();
			}
			model.addAttribute("total", total);
			return "cart";
	}	
	
	
	@RequestMapping(value = "/RemoveCartItem", method = RequestMethod.GET)
	public String deleteCart(@RequestParam int id, ModelMap model, RedirectAttributes redirectAttributes) 
	{
		int result = cartDao.delete(id);
		if(result==0)
		{
			redirectAttributes.addFlashAttribute("removeCartItemStatus", false);
		}
		else
		{
			redirectAttributes.addFlashAttribute("removeCartItemStatus", true);
		}
		return "redirect:/ShowCart";
	}

	
	@RequestMapping(value = "/TruncateCartItem", method = RequestMethod.GET)
	public String truncateCartPage(ModelMap model) 
	{
			String val=(String) model.get("emailid");
			cartDao.truncate(val);
			return "cart-empty";
	}	
}