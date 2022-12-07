package com.healthcare.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.api.model.MealDTO;
import com.healthcare.api.model.MealMobileDTO;
import com.healthcare.model.entity.FoodAllergy;
import com.healthcare.model.entity.Ingredient;
import com.healthcare.model.entity.User;
import com.healthcare.repository.DocumentRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.service.IngredientService;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.healthcare.model.entity.Meal;
import com.healthcare.model.enums.EntityStatusEnum;
import com.healthcare.repository.MealRepository;
import com.healthcare.service.MealService;

import io.jsonwebtoken.lang.Collections;

/**
 * Meal service
 */
@Service
@Transactional
public class MealServiceImpl extends BasicService<Meal, MealRepository>
implements MealService {

  PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();

  private static final String REDIS_KEY = Meal.class.getSimpleName();

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private MealRepository mealRepository;
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  IngredientService ingredientService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  private DocumentRepository documentRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  public MealServiceImpl(MealRepository mealRepository, RedisTemplate<String, String> redisTemplate) {
    this.mealRepository = mealRepository;
    this.redisTemplate = redisTemplate;
  }

  @Override
  @Transactional
  public List<Meal> bulkUpdate(List<Meal> meals) {
    return meals.stream().map(meal -> {
      meal.setUpdatedAt(new Timestamp(new Date().getTime()));
      Meal saved = mealRepository.save(meal);
      return saveInRedis(saved);
    }).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public Meal save(Meal meal) {
	if(meal.getId() != null){
		meal.setUpdatedAt(new Timestamp(new Date().getTime()));
  	} else {
  		meal.setCreatedAt(new Timestamp(new Date().getTime()));
  		meal.setUpdatedAt(new Timestamp(new Date().getTime()));
  	}
    if (meal.getMealPhoto() != null) {
      meal.setMealPhoto(documentRepository.save(meal.getMealPhoto()));
    }
    Meal savedMeal = mealRepository.save(meal);
    return saveInRedis(savedMeal);
  }

  @Override
  @Transactional
  public Meal findById(Long id) {
    String idStr = String.valueOf(id);
    try {
      if (redisTemplate.opsForHash().hasKey(REDIS_KEY, idStr)) {
        return getMeal(id);
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    }
    return saveInRedis(mealRepository.findOne(id));
  }

  @Override
  @Transactional
  public Long deleteById(Long id) {
    mealRepository.delete(id);

    return redisTemplate.opsForHash().delete(REDIS_KEY, id);
  }

  /**
   * find all meals
   *
   * @return List<Meal>
   */
  @Override
  @Transactional
  public List<Meal> findAll() {
    Map<Object, Object> mealMap = redisTemplate.opsForHash().entries(REDIS_KEY);
    List<Meal> mealList = Collections.arrayToList(mealMap.values().toArray());
    if (mealMap.isEmpty())
      mealList = mealRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
    return mealList;
  }

  @Override
  @Transactional
  public Long disableById(Long id) {
    Meal meal = null;
    if (redisTemplate.opsForHash().hasKey(REDIS_KEY, id))
      meal = (Meal) redisTemplate.opsForHash().get(REDIS_KEY, id);
    else
      meal = mealRepository.findOne(id);
    if (meal != null && meal.getId() != null) {
      meal.setStatus(EntityStatusEnum.DISABLE.getValue());
      mealRepository.save(meal);
      redisTemplate.opsForHash().put(REDIS_KEY, meal.getId(), meal);
      return meal.getId();
    }
    return null;
  }

  @Override
  public List<Ingredient> findAllIngredients(Long id) {
    Meal meal = findById(id);
    return meal.getIngredientList();
  }

  @Override
  public List<Ingredient> saveAllIngredients(Long id, List<Ingredient> ingredients) {
    Meal meal = findById(id);
    meal.setIngredientList(ingredients);
    return save(meal).getIngredientList();
  }

  @Override
  public Page<Meal> findAllByAgency(Long agencyId, Pageable pageable) {
    return mealRepository.findAllByAgency(agencyId, pageable);
  }

  public MealMobileDTO findAllByAgency(Long agencyId, Long userId) {
    List<Meal> meals = mealRepository.findAllByAgency(agencyId);
    MealMobileDTO  mealMobileDTO = new MealMobileDTO();
    for(Meal meal : meals) {
      if("breakfast".equalsIgnoreCase(meal.getMealClass())) {
        MealDTO mealDTO = getMealDTO(meal, userId);
        mealMobileDTO.getBreakfast().add(mealDTO);
      } else if("lunch".equalsIgnoreCase(meal.getMealClass())) {
        MealDTO mealDTO = getMealDTO(meal, userId);
        mealMobileDTO.getLunch().add(mealDTO);
      } else if("dinner".equalsIgnoreCase(meal.getMealClass())) {
        MealDTO mealDTO = getMealDTO(meal, userId);
        mealMobileDTO.getDinner().add(mealDTO);
      }
    }
    return mealMobileDTO;
  }

  private MealDTO getMealDTO(Meal meal, Long userId) {
    MealDTO mealDTO = new MealDTO();
    try {
      propertyUtilsBean.copyProperties(mealDTO, meal);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    User user = userRepository.findUserById(userId);
    List<FoodAllergy> userFoodAllergies = user.getFoodAllergies();
    for(Ingredient ingredient : meal.getIngredientList()) {
      ingredient.getFoodAllergies().retainAll(userFoodAllergies);
      if(ingredient.getFoodAllergies().size() > 0) {
        mealDTO.setAllergyConflicts(1);
      }
    }
    return mealDTO;
  }

  @Override
  public Page<Meal> findAllByAgencies(List<Long> agencies, Pageable pageable) {
    return mealRepository.findAllByAgencies(agencies, pageable);
  }

  @Override
  @Transactional
  public Meal getMeal(Long id) {
    String idStr = String.valueOf(id);
    String result = (String) redisTemplate.opsForHash().get(REDIS_KEY, idStr);
    try {
      Meal meal = objectMapper.readValue(result, Meal.class);
      // update objects to newer version in-case the fetched one is out dated
      List<Ingredient> ingredientList = new ArrayList<>();
      for (Ingredient ingredient : meal.getIngredientList()) {
        ingredient = ingredientService.findById(ingredient.getId());
        ingredientList.add(ingredient);
      }
      meal.setIngredientList(ingredientList);
      return meal;
    } catch (IOException ignored) {
    }
    return mealRepository.findOne(id);
  }

  private Meal saveInRedis(Meal meal) {
    try {
      String res = objectMapper.writeValueAsString(meal);
      redisTemplate.opsForHash().put(REDIS_KEY, String.valueOf(meal.getId()), res);
    } catch (JsonProcessingException e) {
      logger.error(e.getMessage());
    }
    return meal;
  }

}
