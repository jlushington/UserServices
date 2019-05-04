package com.nodedynamics.userservices.model.common;

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
@Document(collection="logintoken")
public class LoginTokenModel extends CoreModel{
	
	@Id
	private String iD;
	private String randomToken; //this a random token that is generate and passed in
	private String authToken;
	private String fingerPrint;//browser fingerprint
	private Long dateCreated;//this is the date that this is created
	private Long activeDate;//this is the time this is active till default 30 second
	private boolean loginActive;

}
