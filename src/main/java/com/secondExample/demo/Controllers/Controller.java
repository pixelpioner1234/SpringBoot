package com.secondExample.demo.Controllers;

import com.secondExample.demo.Models.Bird;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    public List<Bird> birds = new ArrayList<>();
    public int currentId = 1;

    @PostMapping("/add")
    public List<Bird> addToList(@RequestBody Bird bird){
        bird.setId(currentId);
        currentId++;
        birds.add(bird);
        return birds;
    }

    @GetMapping("/read")
    public List<Bird> realList(){
        return birds;
    }

    @DeleteMapping("/delete/{id}")
    public List<Bird> deleteFromList(@PathVariable int id){
        birds.removeIf(bird -> (bird.getId() == id));
        return birds;
    }

    @PutMapping("/change/{id}")
    public List<Bird> changeList(@PathVariable int id, @RequestBody Bird newBird){
        for (Bird tempBird:birds){
            if(tempBird.getId()==id){
                tempBird.setName(newBird.getName());
                tempBird.setColor(newBird.getColor());
            }
        }
        return birds;
    }

}
