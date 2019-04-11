package com.nodedynamics.userservices.service.userservice;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.WebSession;

import com.google.gson.Gson;
import com.nodedynamics.userservices.common.Global;
import com.nodedynamics.userservices.common.PasswordUtil;
import com.nodedynamics.userservices.model.common.ResponseModel;
import com.nodedynamics.userservices.model.users.EndUserModel;
import com.nodedynamics.userservices.repo.UserRepository;
import com.nodedynamics.userservices.service.BaseService;
import reactor.core.publisher.Mono;

import com.nodedynamics.userservices.security.JwtTokenProvider;
import com.nodedynamics.userservices.security.UserPrincipal;

@Service
public class AuthService implements BaseService<EndUserModel>, UserDetailsService {
	
	Logger log = LoggerFactory.getLogger(AuthService.class);
	
	private WebSession Session;
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	Gson gson = new Gson();
	
	 @Autowired
	 PasswordEncoder passwordEncoder;
	 
	    @Autowired
	    JwtTokenProvider tokenProvider;
	 
	    @Autowired
	    AuthenticationManager authenticationManager;
	 

	@Override
	public Mono<String> Store(EndUserModel Model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> Update(EndUserModel Model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> DeleteAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> Delete(EndUserModel Model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> GetAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> Get(EndUserModel Model) {

		ResponseModel response;
		
		//CHECK IF USER EXIST
		Optional<EndUserModel> rtn=repo.findByEmail(Model.getEmail());
		
		if(rtn.isPresent()) {
			response=ResponseModel.builder()
					.MessageTypeID(Global.MessageTypeID.ERROR.key)
					.MessageType(Global.MessageType.ERROR.key)
					.Message("User Exist")
					.build();
		}else {
			response=ResponseModel.builder()
					.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
					.MessageType(Global.MessageType.SUCCESS.key)
					.Message("User Does Exist")
					.build();
		}
		
		return Mono.just(gson.toJson(response));
	}
	
	public Mono<String>AuthUser(EndUserModel Model){
		//GET DATA FROM DB
		Optional<EndUserModel> rtnModelcheck=repo.findByEmail(Model.getEmail());
		
		Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		Model.getEmail(),
                		Model.getPassword()
                )
        );
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = tokenProvider.generateToken(authentication);
		
		return Mono.just(gson.toJson(ResponseModel.builder()
				.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
				.MessageType(Global.MessageType.SUCCESS.key)
				.Message(jwt)
				.build()));
		
		/*
		//GET PASSWORD FROM DB
		if(rtnModelcheck.isPresent()) {
			EndUserModel rtnModel= rtnModelcheck.get();
			
			String DBPassword=rtnModel.getPassword();
				
			//GENERATE PASSWORD WITH SALT FROM DB
			//String GenPassword=PasswordUtil.PasswordGen.EncPassword(Model.getPassword(), rtnModel.getSalt());
			String GenPassword=passwordEncoder.encode(Model.getPassword());
			
	
			//CHECK IF PASSWORD MATCH
			if(GenPassword.contains(DBPassword)) {
				
				log.info("AuthService->AuthUser->Success");
					
				//STORE MODEL IN REDIS
				//SAVE SESSION
				
				//Session.getAttributes().put(Global.VAR.SESSION.key, rtnModel.getID());
				
				//log.info("AuthService->AuthUser->session.getAttribute(Global.VAR.SESSION.key): "+ Session.getAttribute(Global.VAR.SESSION.key));
					
				return Mono.just(gson.toJson(ResponseModel.builder()
						.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
						.MessageType(Global.MessageType.SUCCESS.key)
						.Message(rtnModel.getID())
						.build()));
					
			}else {
				log.info("AuthService->AuthUser->Fail");
				
				return Mono.just(gson.toJson(ResponseModel.builder()
						.MessageTypeID(Global.MessageTypeID.ERROR.key)
						.MessageType(Global.MessageType.ERROR.key)
						.Message("USER ERROR- PASSWORD DOES NOT MATCH")
						.build()));
			}
			//END OF IF
		}else {
			return Mono.just(gson.toJson(ResponseModel.builder()
					.MessageTypeID(Global.MessageTypeID.ERROR.key)
					.MessageType(Global.MessageType.ERROR.key)
					.Message("User Input Error")
					.build()));
		}
		*/
		
	}
	
	 public UserPrincipal loadUserById(String id) {
		 log.info("AuthService->loadUserById");
		 EndUserModel user=repo.findById(id).get();
		 
		return UserPrincipal.create(user);
	 }

	@Override
	public void Init(WebSession session) {
	this.Session=session;
			
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		EndUserModel user=repo.findByEmail(username).get();
		log.info("AuthService->loadUserByUsername");
		// TODO Auto-generated method stub
		return UserPrincipal.create(user);
	}
	

}
