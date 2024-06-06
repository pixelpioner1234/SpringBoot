package com.secondExample.demo.Controllers;

import com.secondExample.demo.Models.City;
import com.secondExample.demo.Models.SumRequest;
import com.secondExample.demo.Models.SumResponse;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/api")
public class MyFirstController {

    @GetMapping("/cityName")
    public String getCityName(){
        return "Lida";
    }

    @GetMapping("/sum")
    public int sum(@RequestParam int a,@RequestParam int b){
        return a+b;
    }

    @GetMapping("/city")
    public City getCity() {
        return new City("Lida", 100000);
    }



    @PostMapping("/hello")
    public String printHello(@RequestBody String name){
        return "Hello, " + name ;
    }

    @PostMapping("/sum")
    public SumResponse sumPost(@RequestBody SumRequest<String> request){
        String result = request.getA() + request.getB();
        return  new SumResponse<String>(result);
    }


}
