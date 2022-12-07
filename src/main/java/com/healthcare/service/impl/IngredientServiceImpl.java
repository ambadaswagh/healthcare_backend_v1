package com.healthcare.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.model.entity.FoodAllergy;
import com.healthcare.model.entity.Ingredient;
import com.healthcare.repository.IngredientRepository;
import com.healthcare.service.FoodAllergyService;
import com.healthcare.service.IngredientService;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class IngredientServiceImpl extends BasicService<Ingredient, IngredientRepository> implements IngredientService {

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FoodAllergyService foodAllergyService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String KEY = Ingredient.class.getSimpleName();

    @Override
    public Ingredient save(Ingredient entity) {
      entity = ingredientRepository.save(entity);
      try {
        String res = objectMapper.writeValueAsString(entity);
        redisTemplate.opsForHash().put(KEY, String.valueOf(entity.getId()), res);
      } catch (JsonProcessingException e) {
        logger.error(e.getMessage());
      }
      return entity;
    }

    @Override @Transactional
    public Ingredient findById(Long id) {
      String idStr = String.valueOf(id);
      try
      {
        if (redisTemplate.opsForHash().hasKey(KEY, idStr)) {
          String result = (String) redisTemplate.opsForHash().get(KEY, idStr);
          Ingredient ingredient = objectMapper.readValue(result, Ingredient.class);
          List<FoodAllergy> foodAllergies = new ArrayList<>();
          for(FoodAllergy foodAllergy : ingredient.getFoodAllergies()){
             foodAllergy = foodAllergyService.findById(foodAllergy.getId());
             foodAllergies.add(foodAllergy);
          }
          ingredient.setFoodAllergies(foodAllergies);
          return ingredient;
        }
      }catch (Exception ex){
        logger.error(ex.getMessage());
      }
        return saveInRedis(ingredientRepository.findOne(id));
    }

    /**
     * Delete the entity
     *
     * @param id
     * @return the Redis id deleted
     */
    @Override
    public Long deleteById(Long id) {
        ingredientRepository.delete(id);
        return redisTemplate.opsForHash().delete(KEY, id);
    }

    /**
     * Disable the entity
     *
     * @param id
     * @return the Redis id being disable
     */
    @Override
    public Long disableById(Long id) {
        //TODO
        return null;
    }

    @Override @Transactional
    public List<Ingredient> findAll() {
        Map<Object, Object> ingredientMap = redisTemplate.opsForHash().entries(KEY);
        List<Ingredient> ingredients = Collections.arrayToList(ingredientMap.values().toArray());
        if (ingredientMap.isEmpty())
            ingredients = ingredientRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        return ingredients;
    }

    @Override
    public List<Ingredient> save(List<Ingredient> ingredients) {
      List<Ingredient> resIngredients = new ArrayList<>();
      for (Ingredient ingredient : ingredients) {
        Ingredient oldIngredient = ingredientRepository.findByName(ingredient.getName());
        if(oldIngredient == null) {
          oldIngredient = new Ingredient();
          oldIngredient.setName(ingredient.getName());
        }
        oldIngredient.setFoodAllergies(ingredient.getFoodAllergies());
        oldIngredient = save(oldIngredient);
        resIngredients.add(oldIngredient);
      }
      return resIngredients;
    }

  @Override
  public Ingredient findByName(String name) {
    return findByName(name);
  }

  private Ingredient saveInRedis(Ingredient ingredient) {
    try {
      String res = objectMapper.writeValueAsString(ingredient);
      redisTemplate.opsForHash().put(KEY, String.valueOf(ingredient.getId()), res);
    } catch (JsonProcessingException e) {
      logger.error(e.getMessage());
    }
    return ingredient;
  }
}
