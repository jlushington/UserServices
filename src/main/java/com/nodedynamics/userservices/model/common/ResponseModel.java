package com.nodedynamics.userservices.model.common;

import com.nodedynamics.userservices.model.CoreModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
public class ResponseModel extends CoreModel{
	
	private String MessageType;
	private int MessageTypeID;
	private String Message;

}
