package com.nodedynamics.userservices.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.nodedynamics.userservices.model.users.EndUserModel;
import com.nodedynamics.userservices.security.JwtTokenProvider;
import com.nodedynamics.userservices.service.userservice.AuthService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")

//@Controller
//@Component
public class AuthController {
	
	Logger log = LoggerFactory.getLogger(AuthService.class);
	
	
	 @Autowired
	 AuthenticationManager authenticationManager;
	 
	 @Autowired
	 JwtTokenProvider tokenProvider;
	 
	@Autowired
	Gson gson;
	
	@Autowired
	AuthService service;
	
	//AUTH USERS
	@CrossOrigin(origins = "*") //TODO: NEED TO REMOVE AND INIT PROPER CORS
	@PostMapping(value = "/authcheck", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> AuthCheck(@RequestBody String request){
		log.info(request);
		return service.Get(gson.fromJson(request, EndUserModel.class));
	}
	
	
	//AUTH USERS
	@CrossOrigin(origins = "*") //TODO: NEED TO REMOVE AND INIT PROPER CORS
	@PostMapping(value = "/authaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> Auth(@RequestBody String request){
		log.info("this is data222");
		log.info(request);
		
		EndUserModel loginRequest=gson.fromJson(request, EndUserModel.class);
		
		
		 Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        loginRequest.getEmail(),
	                        loginRequest.getPassword()
	                )
	        );
	
		 
		 SecurityContextHolder.getContext().setAuthentication(authentication);
		 
		 String jwt = tokenProvider.generateToken(authentication);
		 
		 
		 log.info(jwt);
		 
		 

		return service.AuthUser(gson.fromJson(request, EndUserModel.class));
	}
	
	@PostMapping(value = "/getauthsession", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> GetAuthSession(@RequestBody String request){

		gson.newBuilder().create();
		return Mono.just("{this}");
	}

	
	

}
