package com.healthcare.api;

import com.healthcare.model.entity.Ingredient;
import com.healthcare.service.IngredientService;
import com.healthcare.service.MealService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController(value = "IngredientAPI")
@RequestMapping(value = "/api/ingredient")
public class IngredientController {

    @Autowired
    IngredientService ingredientService;

    @Autowired
    MealService mealService;

    @ApiOperation(value = "list all ingredient", notes = "list all ingredient")
    @GetMapping("/list")
    public ResponseEntity<List<Ingredient>> list() {
        return new ResponseEntity<>(ingredientService.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "add/update an ingredient", notes = "add/update an ingredient")
    @PostMapping("/save")
    public ResponseEntity<Ingredient> add(@RequestBody Ingredient ingredient) {
      String name = ingredient.getName();
      if(name == null || name.trim().isEmpty()){
        return new ResponseEntity<>(ingredient, HttpStatus.BAD_REQUEST);
      }
      Ingredient oldIngredient = ingredientService.findByName(name);
      if(oldIngredient != null) {
        //HttpStatus status = new HttpStatus();
        return new ResponseEntity<>(ingredientService.save(ingredient), HttpStatus.CONFLICT);
      }
      return new ResponseEntity<>(ingredientService.save(ingredient), HttpStatus.OK);
    }

    @ApiOperation(value = "add/update all ingredients", notes = "add/update all ingredient")
    @PostMapping("/saveall")
    public ResponseEntity<List<Ingredient>> add(@RequestBody List<Ingredient> ingredients) {
      return new ResponseEntity<>(ingredientService.save(ingredients), HttpStatus.OK);
    }

    @ApiOperation(value = "list all meal ingredients", notes = "list meal ingredients")
    @GetMapping("/meal/{id}")
    public ResponseEntity<List<Ingredient>> userAllergies(@PathVariable("id") Long id) {
        return new ResponseEntity<>(mealService.findAllIngredients(id), HttpStatus.OK);
    }

    @ApiOperation(value = "add all  ingredients to a meal", notes = "add all ingredients to a meal")
    @PostMapping("/meal/{id}")
    public ResponseEntity<List<Ingredient>> saveUserAllergies(@PathVariable("id") Long id, @RequestBody List<Ingredient> ingredientList) {
      List<Ingredient> ingredients = ingredientService.save(ingredientList);
      return new ResponseEntity<>(mealService.saveAllIngredients(id, ingredients), HttpStatus.OK);
    }
}
