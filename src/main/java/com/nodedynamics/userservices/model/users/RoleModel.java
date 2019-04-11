package com.nodedynamics.userservices.model.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.nodedynamics.userservices.model.CoreModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Document(collection="role")
public class RoleModel extends CoreModel{
	
	@Id
	private String iD;
	
	private String name;

}
