package com.baggage.controllers;

import com.baggage.entity.httpRequests.AuthenticationRequest;
import com.baggage.entity.dao.ClientDao;
import com.baggage.entity.CustomResponse;
import com.baggage.entity.httpRequests.RegistationRequest;
import com.baggage.service.ClientService;

import com.baggage.service.CustomPasswordEncoder;
import com.baggage.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.baggage.utils.Constants.*;

@RestController
public class UserController {

    private final ClientService clientService;
    private final UserDetailsService customUserDetailsService;
    private final CustomPasswordEncoder passwordEncoder;

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(ClientService clientService,
                          @Qualifier("customUserDetailsService") UserDetailsService customUserDetailsService,
                          CustomPasswordEncoder cutomPasswordEncoder) {
        this.clientService = clientService;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = cutomPasswordEncoder;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            String username = authenticationRequest.getUsername();
            String password = authenticationRequest.getPassword();
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            log.info("password " + userDetails.getPassword());
            log.info("password from req" + passwordEncoder.encode(password));

            if(userDetails.getPassword().equals(passwordEncoder.encode(password))) {
                return new ResponseEntity<>(new CustomResponse<>(OK,
                        Optional.empty(),
                        Optional.of(TokenUtil.createToken(userDetails))
                        ), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                        Optional.of("login or password is wrong"),
                        Optional.empty()), HttpStatus.OK);
            }

        } catch (Exception e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("login or password is wrong"),
                    Optional.empty()), HttpStatus.OK);
        }

    }

    @GetMapping(value = "/api/userInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@RequestHeader(value = AUTH_HEADER_NAME) String authHeader) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> client = clientService.findByUsername(userName);
            if (client.isPresent())
                return new ResponseEntity<>(
                        new CustomResponse<>("OK", Optional.empty(), client), HttpStatus.OK
                );
            else
                return new ResponseEntity<>(
                        new CustomResponse<>(
                                "INTERNAL_ERROR",
                                Optional.of("client not found"),
                                Optional.empty()), HttpStatus.OK);

        } catch (Exception e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>("INTERNAL_ERROR",
                    Optional.of(e.getMessage()),
                    Optional.empty()), HttpStatus.OK);
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody RegistationRequest registationRequest) {
        try {
            ClientDao clientDao = new ClientDao(
                    registationRequest.getUsername(),
                    registationRequest.getFirstName(),
                    registationRequest.getLastName(),
                    passwordEncoder.encode(registationRequest.getPassword()),
                    "USER");
            log.info("stored password "+ passwordEncoder.encode(registationRequest.getPassword()));

            return new ResponseEntity<>(
                    new CustomResponse<>("OK",
                    Optional.empty(),
                    Optional.of(clientService.save(clientDao))), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomResponse<>("INTERNAL_ERROR",
                    Optional.of("Try later"),
                    Optional.empty()), HttpStatus.OK);
        }
    }
}
