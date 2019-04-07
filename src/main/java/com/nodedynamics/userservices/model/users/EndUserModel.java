package com.nodedynamics.userservices.model.users;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.nodedynamics.userservices.model.CoreModel;
import com.nodedynamics.userservices.model.common.ImageModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Document(collection="enduser")
public class EndUserModel extends CoreModel{

	@Id
	private String iD;
	private String username;
	private String password;
	private String passwordConfirm;
	
	@Indexed(unique = true)
	private String email;
	private String salt;//generate from  Service
	private Integer userTypeID;
	private String verificationCode;
	private Integer state;
	private String firstName;
	private String lastName;
	private String dob;
	private String telNum;
	private String fax;
	private String mobile;
	private String address1;
	private String address2;
	private String city;
	private String provinceState;
	private String country;
	private String postalZip;
	private ImageModel pic;
	private String gender;
	private LocalDateTime dateCreated;
	

}
