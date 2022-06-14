package com.qub.customproxyrouter.operator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {

    Optional<Operator> findOperatorsByName(String name);

    Optional<Operator> findOperatorsByUrls(String url);

}
