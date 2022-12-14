package com.example.customer.controller;

import com.example.library.model.Customer;
import com.example.library.model.Order;
import com.example.library.model.ShoppingCart;
import com.example.library.serivce.CustomerService;
import com.example.library.serivce.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/check-out")
    public String checkout(Model model, Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);

//
        if(customer.getPhoneNumber() == null){

            model.addAttribute("customer", customer);
            model.addAttribute("error", "You must fill the information after checkout!");
            return "account";
        }

//        if(username != null){
//
//            model.addAttribute("customer", customer);
//            model.addAttribute("error", "You must fill the information after checkout!");
//            return "account";
//        }else{
            model.addAttribute("customer", customer);
            ShoppingCart cart = customer.getShoppingCart();
            model.addAttribute("cart", cart);

        return "checkout";
    }


    @GetMapping("/order")
    public String order(Principal principal , Model model, HttpSession session)

    {
        if(principal == null)
        {
            return "redirect:/login";
        }
        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);






        List<Order> orderList = customer.getOrders();
        if(customer.getPhoneNumber() == null || customer.getAddress() == null || customer.getCity() == null || customer.getCountry() == null){

            model.addAttribute("customer", customer);
            model.addAttribute("error", "You must fill the information after checkout!");
            return "account";
        }

        if(orderList.isEmpty()){

            model.addAttribute("check", "No Order in your cart");
            return "order";

        }
        ShoppingCart cart = customer.getShoppingCart();
        session.setAttribute("totalItems", cart.getTotalItems());
        session.setAttribute("totalPrice", cart.getTotalPrices());
        model.addAttribute("orders",orderList);
        return "order";
    };

    @GetMapping("/save-order")
    public  String saveOrder(Principal principal,HttpSession session){
        if(principal == null)
        {
            return "redirect:/login";
        }






        String username = principal.getName();
        Customer customer = customerService.findByUsername(username);
        ShoppingCart cart = customer.getShoppingCart();
        session.setAttribute("totalItems", cart.getTotalItems());
        session.setAttribute("totalPrice", cart.getTotalPrices());
        orderService.saveOrder(cart);
        return "OrderSucessfully";

    }

}