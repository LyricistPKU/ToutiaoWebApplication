//package com.lyubinbin.controller;
//
//import com.lyubinbin.service.ToutiaoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.view.RedirectView;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.util.Arrays;
//import java.util.Enumeration;
//import java.util.List;
//
///**
// * index controller, just for test
// * Created by Lyu binbin on 2016/7/25.
// */
//@Controller
//public class IndexController {
//    @Autowired
//    ToutiaoService toutiaoService;
//
//    @RequestMapping(path = {"/", "/index"})
//    @ResponseBody
//    public String index(){
//        return "Hello World";
//    }
//
//    @RequestMapping(value = {"/home"})
//    public String home(){
//        return "home";
//    }
//
//    // velocity template practice
//    @RequestMapping(value = {"/hello"})
//    public String hello(Model model){
//        model.addAttribute("name", "Lyu Binbin");
//        List<String> colors = Arrays.asList(new String[] {"RED", "WHITE", "BLUE"});
//        model.addAttribute("colors", colors);
//        return "hello";
//    }
//
//    // print http request information
//    @RequestMapping(value = {"/request"})
//    @ResponseBody
//    public String request(HttpServletRequest request, HttpServletResponse response, HttpSession session){
//        StringBuilder sb = new StringBuilder();
//        Enumeration<String> headNames = request.getHeaderNames();
//        while(headNames.hasMoreElements()){
//            String name = headNames.nextElement();
//            sb.append(name + ":" + request.getHeader(name) + "<br>");
//        }
//        for(Cookie cookie : request.getCookies()){
//            sb.append("Cookie:" + cookie.getName() + ":" + cookie.getValue() + "<br>");
//        }
//        sb.append("getMethod:" + request.getMethod() + "<br>");
//        return sb.toString();
//    }
//
//    // response a cookie just for test
//    @RequestMapping(value = {"/response"})
//    @ResponseBody
//    public String response(@CookieValue(value = "toutiaoId", defaultValue = "a") String toutiaoId,
//                           @RequestParam(value = "key", defaultValue = "key") String key,
//                           @RequestParam(value = "value", defaultValue = "value") String value,
//                           HttpServletResponse response){
//        response.addCookie(new Cookie(key, value));
//        response.addHeader(key, value);
//        return "Id from cookie: " + toutiaoId;
//    }
//
//    @RequestMapping("/redirect/{code}")
//    public RedirectView redirect(@PathVariable("code") int code) {
//        RedirectView red = new RedirectView("/", true);
//        if(code == 301){
//            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
//        }
//        return red;
//    }
//
//    // self-defined  exception handler
//    @ExceptionHandler()
//    @ResponseBody
//    public String error(Exception e){
//        return "Error: " + e.getMessage();
//    }
//}
