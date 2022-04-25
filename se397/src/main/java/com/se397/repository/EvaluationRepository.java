package com.se397.repository;

import com.se397.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    @Query(value = "select * from evaluation where evaluation.product_id = ?1" , nativeQuery = true)
    List<Evaluation> getEvaluationsByProductId(int id);

    /* Get rate review of product by product id */
    @Query(value = "select sum(evaluation.rate) / count(evaluation.product_id) from evaluation where product_id = ?1" , nativeQuery=true)
    Double getRateReviewByProductId(int id);

}
