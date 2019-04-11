package com.nodedynamics.userservices.service.userservice;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class EndUserService implements BaseService<EndUserModel> {
	
	Logger log = LoggerFactory.getLogger(EndUserService.class);
	
	//private WebSession Session;
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	Gson gson = new Gson();
	
	 @Autowired
	 PasswordEncoder passwordEncoder;

	@Override
	public Mono<String> Store(EndUserModel Model) {
		
		//RETURN RESPONSE MODEL
		String ReturnMessage = null;
		
		//CHECK IF EMAIL EXIST
		//Optional<EndUserModel> RTNModel=repo.findByEmail(Model.getEmail());
		
		//if(RTNModel.isPresent()) {
			
			log.info("EndUserService->Store->isPresent");
			//SALT
			String Salt= PasswordUtil.PasswordGen.genSalt();
			if(!(Model.getPasswordConfirm().isEmpty())) {
				log.info("EndUserService->Store->isEmpty");
				
				if(Model.getPassword().contains(Model.getPasswordConfirm())){
					log.info("EndUserService->Store->getPasswordConfirm");
					
					EndUserModel UBM = EndUserModel.builder()
							.username(Model.getUsername())
							.password(passwordEncoder.encode(Model.getPassword()))
							.email(Model.getEmail())
							//.salt(Salt)
							.roles(Model.getRoles())
							.state(Global.UserState.PENDING.key)
							.userTypeID(Global.UserType.ENDUSER.key)
							.build();
					
					//SAVE MODEL
					repo.save(UBM);
					ReturnMessage=gson.toJson(ResponseModel.builder()
							.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
							.MessageType(Global.MessageType.SUCCESS.key)
							.Message("User Successfully Added")
							.build());
					
				}else {
					ReturnMessage= gson.toJson(ResponseModel.builder()
							.MessageTypeID(Global.MessageTypeID.ERROR.key)
							.MessageType(Global.MessageType.ERROR.key)
							.Message("User Input Error 01")
							.build());
				}
				
			}else {
				ReturnMessage= gson.toJson(ResponseModel.builder()
						.MessageTypeID(Global.MessageTypeID.ERROR.key)
						.MessageType(Global.MessageType.ERROR.key)
						.Message("User Input Error 02")
						.build());
			}
			
		//}
		/*
		else {
			ReturnMessage= gson.toJson(ResponseModel.builder()
					.MessageTypeID(Global.MessageTypeID.ERROR.key)
					.MessageType(Global.MessageType.ERROR.key)
					.Message("User Input Error 03")
					.build());
			
		}
		*/
		
		
				return Mono.just(ReturnMessage);
		
		
	}

	@Override
	public Mono<String> Update(EndUserModel Model) {
		
		//RETURN RESPONSE MODEL
		String ReturnMessage = null;
				
		
		//pull user Information
		Optional<EndUserModel> EUR=repo.findById(Model.getID());
		
		if(EUR.isPresent()) {			
			if(Model.getPasswordConfirm() == null) {
				EndUserModel MergeEndUserModel= EndUserModel.builder()
						.iD(Model.getID())
						.username(Model.getUsername())
						.password(EUR.get().getPassword())
						.email(Model.getEmail())
						.salt(EUR.get().getSalt())
						.state(EUR.get().getState())
						 .firstName(Model.getFirstName())
						 .lastName(Model.getLastName())
						 .dob(Model.getDob())
						 .gender(Model.getGender())
						 .telNum(Model.getTelNum())
						 .build();
				//SAVE MODEL
				repo.save(MergeEndUserModel);
				ReturnMessage=gson.toJson(ResponseModel.builder()
						.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
						.MessageType(Global.MessageType.SUCCESS.key)
						.Message("User Successfully Updated")
						.build());
				
			}else {
				//SAVE MODEL
				repo.save(Model);
				
				ReturnMessage=gson.toJson(ResponseModel.builder()
						.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
						.MessageType(Global.MessageType.SUCCESS.key)
						.Message("User Successfully Updated")
						.build());
				
			}
			
			
			
		}else {
			ReturnMessage= gson.toJson(ResponseModel.builder()
					.MessageTypeID(Global.MessageTypeID.ERROR.key)
					.MessageType(Global.MessageType.ERROR.key)
					.Message("User Input Error")
					.build());
			
		}
		return Mono.just(ReturnMessage);
		
		/*
		
		if(EUR.isPresent()) {
			if(Model.getPasswordConfirm().isEmpty()) {
				
				EndUserModel MergeEndUserModel;
				
				//CHECK IMAGE
				if(Model.getPic().getImageLoc() ==null)
				{
					
					if(EUR.get().getPic() == null) {
					//MERGE DATA
					 MergeEndUserModel = EndUserModel.builder()
							.iD(Model.getID())
							 .firstName(Model.getFirstName())
							 .lastName(Model.getLastName())
							 .gender(Model.getGender())
							 .dob(Model.getDob())
							 .telNum(Model.getTelNum())
							 .email(Model.getEmail())
							 .username(Model.getUsername())
							 .password(EUR.get().getPassword())
							 .salt(EUR.get().getSalt())
							 .state(EUR.get().getState())
							 .userTypeID(EUR.get().getUserTypeID())
							 .build();
					}else {
						 MergeEndUserModel = EndUserModel.builder()
									.iD(Model.getID())
									 .firstName(Model.getFirstName())
									 .lastName(Model.getLastName())
									 .gender(Model.getGender())
									 .dob(Model.getDob())
									 .telNum(Model.getTelNum())
									 .email(Model.getEmail())
									 .username(Model.getUsername())
									 .password(EUR.get().getPassword())
									 .salt(EUR.get().getSalt())
									 .state(EUR.get().getState())
									 .userTypeID(EUR.get().getUserTypeID())
									 .pic(EUR.get().getPic())
									 .build();
						
					}
					
				}else
				{
					//UPLOAD IMAGE
					
					ImageManager imageManager=new ImageManager();
					imageManager.Connect();
					
					 MergeEndUserModel = EndUserModel.builder()
								.iD(Model.getID())
								 .firstName(Model.getFirstName())
								 .lastName(Model.getLastName())
								 .gender(Model.getGender())
								 .dob(Model.getDob())
								 .telNum(Model.getTelNum())
								 .email(Model.getEmail())
								 .username(Model.getUsername())
								 .password(EUR.get().getPassword())
								 .salt(EUR.get().getSalt())
								 .state(EUR.get().getState())
								 .userTypeID(EUR.get().getUserTypeID())
								 .pic(imageManager.Commit(Model.getPic()))
								 .build();
					
				}
				
				repo.
				save(MergeEndUserModel);
				return Mono.just(gson.toJson(ResponseModel.builder()
						.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
						.MessageType(Global.MessageType.SUCCESS.key)
						.Message("User Info Update Successfully")
						.build()));
						 
			}else {
				if(Model.getPassword().contains(Model.getPasswordConfirm())){
					EndUserModel MergeEndUserModel;
					String Salt= PasswordUtil.PasswordGen.genSalt();
					
					if(Model.getPic().getImageLoc() ==null)
					{
						if(EUR.get().getPic() == null) {
							
							MergeEndUserModel = EndUserModel.builder()
									.iD(Model.getID())
									 .firstName(Model.getFirstName())
									 .lastName(Model.getLastName())
									 .gender(Model.getGender())
									 .dob(Model.getDob())
									 .telNum(Model.getTelNum())
									 .email(Model.getEmail())
									 .username(Model.getUsername())
									 .password(PasswordUtil.PasswordGen.EncPassword(Model.getPassword(), Salt))
									 .salt(Salt)
									 .state(EUR.get().getState())
									 .userTypeID(EUR.get().getUserTypeID())
									 .build();
							
						}else
						{
							 MergeEndUserModel = EndUserModel.builder()
										.iD(Model.getID())
										 .firstName(Model.getFirstName())
										 .lastName(Model.getLastName())
										 .gender(Model.getGender())
										 .dob(Model.getDob())
										 .telNum(Model.getTelNum())
										 .email(Model.getEmail())
										 .username(Model.getUsername())
										 .password(EUR.get().getPassword())
										 .salt(EUR.get().getSalt())
										 .state(EUR.get().getState())
										 .userTypeID(EUR.get().getUserTypeID())
										 .pic(EUR.get().getPic())
										 .build();
							
						}
					
						
					}else
					{
						ImageManager imageManager=new ImageManager();
						imageManager.Connect();
						
						MergeEndUserModel = EndUserModel.builder()
								.iD(Model.getID())
								 .firstName(Model.getFirstName())
								 .lastName(Model.getLastName())
								 .gender(Model.getGender())
								 .dob(Model.getDob())
								 .telNum(Model.getTelNum())
								 .email(Model.getEmail())
								 .username(Model.getUsername())
								 .password(PasswordUtil.PasswordGen.EncPassword(Model.getPassword(), Salt))
								 .salt(Salt)
								 .state(EUR.get().getState())
								 .userTypeID(EUR.get().getUserTypeID())
								 .pic(imageManager.Commit(Model.getPic()))
								 .build();
						
					}
					repo.save(MergeEndUserModel);
					return Mono.just(gson.toJson(ResponseModel.builder()
							.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
							.MessageType(Global.MessageType.SUCCESS.key)
							.Message("User Info Update Successfully")
							.build()));
					
				}else {
					return Mono.just(gson.toJson(ResponseModel.builder()
							.MessageTypeID(Global.MessageTypeID.ERROR.key)
							.MessageType(Global.MessageType.ERROR.key)
							.Message("Password Update Does Not Match")
							.build()));
				}
				
			}
		}else {
			
			return Mono.just(gson.toJson(ResponseModel.builder()
					.MessageTypeID(Global.MessageTypeID.ERROR.key)
					.MessageType(Global.MessageType.ERROR.key)
					.Message("User Could Not Be Found")
					.build()));
			
		}
		*/
	}

	@Override
	public Mono<String> Delete(EndUserModel Model) {
		repo.deleteById(Model.getID());
		return Mono.just(gson.toJson(ResponseModel.builder()
				.MessageTypeID(Global.MessageTypeID.SUCCESS.key)
				.MessageType(Global.MessageType.SUCCESS.key)
				.Message("User Successfully Deleted")
				.build()));
	}


	@Override
	public Mono<String> DeleteAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Mono<String> GetAll() {
		//return Mono.just("this is get all");
		//return Mono.just(gson.toJson(repo.findAll()));
		
		//SHOULD NOT RETURN DATA AS THIS IS A SECURITY VALIDATION
		return Mono.just(gson.toJson(ResponseModel.builder()
				.MessageTypeID(Global.MessageTypeID.ERROR.key)
				.MessageType(Global.MessageType.ERROR.key)
				.Message("GET ALL FUNCTION IS NOT ALLOWED")
				.build()));
	}

	@Override
	public Mono<String> Get(EndUserModel Model) {
		log.info("EndUserService->Get->Model.getID(): "+ Model.getID());
		
		
		
		if(Model.getID() !=null) {
			
			
			return Mono.just(gson.toJson(repo.findById(Model.getID())));
			
		}else if(Model.getEmail()  !=null) {
			return Mono.just(gson.toJson(repo.findByEmail(Model.getEmail())));
		
		}else {
			return Mono.just(gson.toJson(ResponseModel.builder()
					.MessageTypeID(Global.MessageTypeID.ERROR.key)
					.MessageType(Global.MessageType.ERROR.key)
					.Message("Data Not Valid")
					.build()));
		}
		
	}

	@Override
	public void Init(WebSession session) {
		//this.Session=session;
		
	}
}
