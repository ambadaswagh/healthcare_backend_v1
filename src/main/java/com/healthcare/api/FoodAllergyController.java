package com.healthcare.api;

import com.healthcare.model.entity.FoodAllergy;
import com.healthcare.service.FoodAllergyService;
import com.healthcare.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController(value = "FoodAllergyAPI")
@RequestMapping(value = "/api/foodallergy")
public class FoodAllergyController {

    @Autowired
    FoodAllergyService foodAllergyService;

    @Autowired
    UserService userService;

    @ApiOperation(value = "list all food allergies", notes = "list all food allergies")
    @GetMapping("/list")
    public ResponseEntity<List<FoodAllergy>> list() {
        return new ResponseEntity<>(foodAllergyService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "list all food allergies for user", notes = "list all food allergies for user")
    @GetMapping("/list/{id}")
    public ResponseEntity<List<FoodAllergy>> listForUser(@PathVariable long id) {
      return new ResponseEntity<>(foodAllergyService.getAlleryListForUser(id), HttpStatus.OK);
    }

    @ApiOperation(value = "list all food allergies for new user", notes = "list all food allergies for  new user")
    @GetMapping("/list/user")
    public ResponseEntity<List<FoodAllergy>> listForNewUser() {
      return new ResponseEntity<>(foodAllergyService.findByAddedByUser(false), HttpStatus.OK);
    }

    @ApiOperation(value = "get allergy by id", notes = "get allergy by id")
    @GetMapping("/{id}")
    public ResponseEntity<FoodAllergy> getAllery(@PathVariable long id) {
     return new ResponseEntity<>(foodAllergyService.findById(id), HttpStatus.OK);
    }

    @ApiOperation(value = "add/update a food allergies", notes = "add/update a food allergies")
    @PostMapping    ("/save")
    public ResponseEntity<FoodAllergy> add(@RequestBody FoodAllergy foodAllergy) {
        return new ResponseEntity<>(foodAllergyService.save(foodAllergy), HttpStatus.OK);
    }

    @ApiOperation(value = "list all user food allergies", notes = "list user food allergies")
    @GetMapping("/user/{id}")
    public ResponseEntity<List<FoodAllergy>> userAllergies(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.findAllAllergies(id), HttpStatus.OK);
    }

    @ApiOperation(value = "add all food allergies to an user", notes = "add all food allergies to an user")
    @PostMapping("/user/{id}")
    public ResponseEntity<List<FoodAllergy>> saveUserAllergies(@PathVariable("id") Long id, @RequestBody List<FoodAllergy> allergies) {
        return new ResponseEntity<>(userService.saveAllAllergies(id, allergies), HttpStatus.OK);
    }
}
