package com.nodedynamics.userservices.repo;


import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.nodedynamics.userservices.model.users.EndUserModel;

public interface UserRepository  extends MongoRepository<EndUserModel, String>{
	
	Optional<EndUserModel> findByEmail(String Email);
	EndUserModel findByUsername(String Username);

}
