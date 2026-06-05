package com.FoodlyBusinessService.infrastructure.persistence;

import com.FoodlyBusinessService.domain.model.Huarique;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HuariqueRepository extends MongoRepository<Huarique, String> {
}
