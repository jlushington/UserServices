package com.nodedynamics.userservices.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.nodedynamics.userservices.model.common.LoginTokenModel;
import com.nodedynamics.userservices.model.users.EndUserModel;

public interface TokenTransferRepository extends MongoRepository<LoginTokenModel, String>{
	
	Optional<LoginTokenModel> findByRandomToken(String randomToken);

}
