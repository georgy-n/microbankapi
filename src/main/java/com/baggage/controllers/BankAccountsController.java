package com.baggage.controllers;

import com.baggage.entity.httpRequests.CreateBankAccountRequest;
import com.baggage.entity.CustomResponse;
import com.baggage.entity.dao.BankAccountDao;
import com.baggage.entity.dao.ClientDao;
import com.baggage.service.BankAccountsService;
import com.baggage.service.ClientService;
import com.baggage.utils.CustomError;
import com.baggage.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.baggage.utils.Constants.*;

@RestController
@RequestMapping("/api/bankAccounts")
public class BankAccountsController {

    private final ClientService clientService;
    private final BankAccountsService bankAccountsService;
    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public BankAccountsController(ClientService clientService, BankAccountsService bankAccountsService) {
        this.clientService = clientService;
        this.bankAccountsService = bankAccountsService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<?> getAllBankAccounts(
                                    @RequestHeader(value = AUTH_HEADER_NAME) String authHeader) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> client = clientService.findByUsername(userName);
            if (client.isPresent()) {
                return new ResponseEntity<>(new CustomResponse<>(OK,
                        Optional.empty(),
                        Optional.of(bankAccountsService.findAllBankAccountsByUserId(client.get().getId()))
                ), HttpStatus.OK);
            } else throw new CustomError("cant find user by session");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("Getting all bank account is failed"),
                    Optional.empty()), HttpStatus.OK);
        }

    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody CreateBankAccountRequest createBankAccountRequest,
                                    @RequestHeader(value = AUTH_HEADER_NAME) String authHeader) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> client = clientService.findByUsername(userName);
            Long number = new Random().nextLong();
            if (client.isPresent()) {
                BankAccountDao bankAccountDao = new BankAccountDao(
                        number,
                        createBankAccountRequest.getName(),
                        client.get().getId(),
                        createBankAccountRequest.getBalance(),
                        createBankAccountRequest.getCurrency());

                return new ResponseEntity<>(new CustomResponse<>(OK,
                        Optional.empty(),
                        Optional.of(bankAccountsService.save(bankAccountDao))
                ), HttpStatus.OK);
            } else throw new CustomError("cant find user by session");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("Creation bank account is failed"),
                    Optional.empty()), HttpStatus.OK);
        }

    }

    @GetMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserById(@RequestHeader(value = AUTH_HEADER_NAME) String authHeader,
                                         @RequestParam(name = "account") Long bankAccountNumber) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> client = clientService.findByUsername(userName);
            if (client.isPresent()) {
                List<BankAccountDao> bankAccountDao = bankAccountsService.findAllBankAccountsByUserId(client.get().getId());
                Optional<BankAccountDao> maybeBankAccount = bankAccountDao.stream().filter((ba) -> ba.getNumber().equals(bankAccountNumber)).findFirst();
                if (maybeBankAccount.isPresent()) {
                    bankAccountsService.deleteBankAccountById(maybeBankAccount.get().getId());
                    return new ResponseEntity<>(
                            new CustomResponse<>(
                                    "OK",
                                    Optional.empty(),
                                    Optional.empty()),
                            HttpStatus.OK);
                } else throw new CustomError("bank account not found");
            } else
                throw new CustomError("user not found by session");

        } catch (Exception e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>("INTERNAL_ERROR",
                    Optional.of("delete bank account is failed"),
                    Optional.empty()), HttpStatus.OK);
        }
    }
}
