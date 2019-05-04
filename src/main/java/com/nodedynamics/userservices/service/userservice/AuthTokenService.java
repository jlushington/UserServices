package com.nodedynamics.userservices.service.userservice;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.WebSession;

import com.google.gson.Gson;
import com.nodedynamics.userservices.common.Global;
import com.nodedynamics.userservices.model.common.LoginTokenModel;
import com.nodedynamics.userservices.model.common.ResponseModel;
import com.nodedynamics.userservices.repo.TokenTransferRepository;
import com.nodedynamics.userservices.service.BaseService;

import reactor.core.publisher.Mono;

@Service
public class AuthTokenService implements BaseService<LoginTokenModel>{
	
	Logger log = LoggerFactory.getLogger(EndUserService.class);
	
	//private WebSession Session;
	
	@Autowired
	private TokenTransferRepository repo;
	
	@Autowired
	Gson gson = new Gson();
	

	@Override
	public void Init(WebSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mono<String> Store(LoginTokenModel Model) {
		
		 Date date = new Date();
		
		LoginTokenModel LTM = LoginTokenModel.builder()
				.randomToken(Model.getRandomToken())
				.authToken(Model.getAuthToken())
				.fingerPrint(Model.getFingerPrint())
				.dateCreated(date.getTime())
				.activeDate(date.getTime()+TimeUnit.SECONDS.toMillis(Global.TokeStorageTime.T30.key))
				.loginActive(true)
				.build();
		
		repo.save(LTM);

		return Mono.just(gson.toJson(ResponseModel.builder()
				.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
				.MessageType(Global.MessageType.SUCCESS.key)
				.Message("Token Stored Successfully")
				.build()));
	}

	@Override
	public Mono<String> Update(LoginTokenModel Model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> DeleteAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> Delete(LoginTokenModel Model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> GetAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> Get(LoginTokenModel Model) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Mono<String> TokenAccess(LoginTokenModel Model){
		Date date = new Date();
		
		ResponseModel rm;
		
		Optional<LoginTokenModel> loginToken= repo.findByRandomToken(Model.getRandomToken());
		
		if(loginToken.isPresent()) {
			LoginTokenModel logintokenModel =loginToken.get();
			
			if(logintokenModel.isLoginActive()) {
				if(logintokenModel.getDateCreated()<date.getTime() && logintokenModel.getActiveDate()>date.getTime()) {
					rm = ResponseModel.builder()
							.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
							.MessageType(Global.MessageType.SUCCESS.key)
							.Message(logintokenModel.getAuthToken())
							.build();
				}else {
					rm = ResponseModel.builder()
							.MessageTypeID(Global.MessageTypeID.ERROR.key)
							.MessageType(Global.MessageType.ERROR.key)
							.Message("Token Access Not Successful")
							.build();
				}
			}else {
				rm = ResponseModel.builder()
						.MessageTypeID(Global.MessageTypeID.ERROR.key)
						.MessageType(Global.MessageType.ERROR.key)
						.Message("Token Access Not Successful")
						.build();
			}
		}else {
			rm = ResponseModel.builder()
					.MessageTypeID(Global.MessageTypeID.ERROR.key)
					.MessageType(Global.MessageType.ERROR.key)
					.Message("Token Access Not Successful")
					.build();
		}
		

		return Mono.just(gson.toJson(rm));
		
		
	}

}
