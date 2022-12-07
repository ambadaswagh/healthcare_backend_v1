package com.healthcare.service.impl;

import com.healthcare.model.entity.EmployeeClocking;
import com.healthcare.model.entity.FoodAllergy;
import com.healthcare.model.entity.HomeVisit;
import com.healthcare.model.entity.User;
import com.healthcare.repository.FoodAllergyRepository;
import com.healthcare.service.FoodAllergyService;
import com.healthcare.service.UserService;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class FoodAllergyServiceImpl extends BasicService<FoodAllergy, FoodAllergyRepository> implements FoodAllergyService {

    @Autowired
    FoodAllergyRepository foodAllergyRepository;

    @Autowired
    private RedisTemplate<String, FoodAllergy> redisTemplate;

    @Autowired
    private UserService userService;

    private static final String KEY = FoodAllergy.class.getSimpleName();

    @Override
    public FoodAllergy save(FoodAllergy entity) {
        entity = foodAllergyRepository.save(entity);
        redisTemplate.opsForHash().put(KEY, entity.getId(), entity);
        return entity;
    }

    @Override @Transactional
    public FoodAllergy findById(Long id) {
        if (redisTemplate.opsForHash().hasKey(KEY, id)) {
            return (FoodAllergy) redisTemplate.opsForHash().get(KEY, id);
        }
        FoodAllergy foodAllergy = foodAllergyRepository.findOne(id);
        redisTemplate.opsForHash().put(KEY, foodAllergy.getId(), foodAllergy);
        return foodAllergy;
    }

    /**
     * Delete the entity
     *
     * @param id
     * @return the Redis id deleted
     */
    @Override
    public Long deleteById(Long id) {
        foodAllergyRepository.delete(id);
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
    public List<FoodAllergy> findAll() {
        Map<Object, Object> foodAllergyMap = redisTemplate.opsForHash().entries(KEY);
        List<FoodAllergy> foodAllergyList = Collections.arrayToList(foodAllergyMap.values().toArray());
        if (foodAllergyMap.isEmpty()) {
          foodAllergyList = foodAllergyRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
          if(foodAllergyList != null && !foodAllergyList.isEmpty()){
            redisTemplate.opsForHash().putAll(KEY, foodAllergyList.stream().collect(Collectors.toMap(f -> f.getId(), f -> f)));
          }
        }
        return foodAllergyList;
    }

    @Override @Transactional
    public List<FoodAllergy> getAlleryListForUser(Long id){
      User user = userService.findById(id);
      List<FoodAllergy> foodAllergyList = findByAddedByUser(false);
      for(FoodAllergy foodAllergy : user.getFoodAllergies()){
        if(!foodAllergyList.contains(foodAllergy)){
          foodAllergyList.add(foodAllergy);
        }
      }
      return foodAllergyList;
    }

  @Override @Transactional
  public List<FoodAllergy> findByAddedByUser(Boolean addedByUser) {
    return  foodAllergyRepository.findByAddedByUser(addedByUser);
  }
}
