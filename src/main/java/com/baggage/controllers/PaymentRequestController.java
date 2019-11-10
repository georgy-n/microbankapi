package com.baggage.controllers;

import com.baggage.entity.CustomResponse;
import com.baggage.entity.dao.BankAccountDao;
import com.baggage.entity.dao.ClientDao;
import com.baggage.entity.dao.PaymentRequestDao;
import com.baggage.entity.httpRequests.CreatePaymentRequest;
import com.baggage.service.BankAccountsService;
import com.baggage.service.ClientService;
import com.baggage.service.FriendsService;
import com.baggage.service.PaymentRequestService;
import com.baggage.utils.CustomError;
import com.baggage.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.baggage.utils.Constants.*;

@RestController
@RequestMapping("/api/paymentRequest")
public class PaymentRequestController {

    private final ClientService clientService;
    private final BankAccountsService bankAccountsService;
    private final PaymentRequestService paymentRequestService;
    private final FriendsService friendsService;
    private final Object sync = new Object();

    private final Logger log = LoggerFactory.getLogger(PaymentRequestController.class);

    @Autowired
    public PaymentRequestController(ClientService clientService, BankAccountsService bankAccountsService, PaymentRequestService paymentRequestService, FriendsService friendsService) {
        this.clientService = clientService;
        this.bankAccountsService = bankAccountsService;
        this.paymentRequestService = paymentRequestService;
        this.friendsService = friendsService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody CreatePaymentRequest createPaymentRequest,
                                    @RequestHeader(value = AUTH_HEADER_NAME) String authHeader) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);

            Optional<ClientDao> owner = clientService.findByUsername(userName);

            Optional<BankAccountDao> ownerBankAccount = bankAccountsService
                    .findByBankAccountId(createPaymentRequest.getBankAccountFrom());

            Optional<ClientDao> recipient = bankAccountsService
                    .findByBankAccountId(createPaymentRequest.getBankAccountTo())
                    .flatMap((BankAccountDao ba) -> clientService.findById(ba.getOwnerId()));

            Optional<BankAccountDao> recipientBankAccount = recipient
                    .flatMap(c -> bankAccountsService
                            .findAllBankAccountsByUserId(c.getId())
                            .stream()
                            .filter(ba -> ba.getId().equals(createPaymentRequest.getBankAccountTo()))
                            .findFirst());

            if (owner.isPresent()
                    && recipient.isPresent()
                    && ownerBankAccount.isPresent()
                    && recipientBankAccount.isPresent()) {
                if (friendsService.hasFriendship(owner.get().getId(), recipient.get().getId())) {
                    PaymentRequestDao paymentRequestDao = new PaymentRequestDao(
                            createPaymentRequest.getBankAccountFrom(),
                            createPaymentRequest.getBankAccountTo(),
                            createPaymentRequest.getAmount(),
                            createPaymentRequest.getCurrency(),
                            WAITING);

                    return new ResponseEntity<>(new CustomResponse<>(OK,
                            Optional.empty(),
                            Optional.of(paymentRequestService.savePaymentRequest(paymentRequestDao))
                    ), HttpStatus.OK);
                } else throw new CustomError("not friend");
            } else throw new CustomError("cant find user by session");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("Creation payment request is failed"),
                    Optional.empty()), HttpStatus.OK);
        }

    }

    @GetMapping(value = "/decline", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> decline(@RequestHeader(value = AUTH_HEADER_NAME) String authHeader,
                                     @RequestParam(name = "paymentRequest") Integer paymentRequestId) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> client = clientService.findByUsername(userName);
            Optional<PaymentRequestDao> paymentRequestDao = paymentRequestService.findByPaymentRequestId(paymentRequestId);
            Optional<BankAccountDao> ownerBankAccount = client
                    .flatMap(c -> paymentRequestDao.flatMap(pr -> bankAccountsService
                            .findAllBankAccountsByUserId(c.getId())
                            .stream()
                            .filter(ba -> ba.getId().equals(pr.getIdBankAccountFrom()))
                            .findFirst()));

            if (client.isPresent() && paymentRequestDao.isPresent() && ownerBankAccount.isPresent()) {
                PaymentRequestDao prd = paymentRequestDao.get();
                prd.setStatus(DECLINED);
                return new ResponseEntity<>(
                        new CustomResponse<>(
                                "OK",
                                Optional.empty(),
                                Optional.of(paymentRequestService.savePaymentRequest(prd))),
                        HttpStatus.OK);
            } else throw new CustomError("payment request or client or bank account not found");
        } catch (Exception e) {
            log.error("decline payment request is failed");
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("Decline payment request is failed"),
                    Optional.empty()), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/accept", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> accept(@RequestHeader(value = AUTH_HEADER_NAME) String authHeader,
                                    @RequestParam(name = "paymentRequest") Integer paymentRequestId) {
        try {
            String userName = TokenUtil.getUserNameFromToken(authHeader);
            Optional<ClientDao> client = clientService.findByUsername(userName);
            Optional<PaymentRequestDao> paymentRequestDao = paymentRequestService.findByPaymentRequestId(paymentRequestId);
            Optional<BankAccountDao> fromBankAccount = client
                    .flatMap(c -> paymentRequestDao.flatMap(pr -> bankAccountsService
                            .findAllBankAccountsByUserId(c.getId())
                            .stream()
                            .filter(ba -> ba.getId().equals(pr.getIdBankAccountFrom()))
                            .findFirst()));

            Optional<BankAccountDao> toBankAccount = paymentRequestDao
                    .flatMap(pr -> bankAccountsService
                            .findByBankAccountId(pr.getIdBankAccountTo()));


            if (client.isPresent() && paymentRequestDao.isPresent() && fromBankAccount.isPresent() && toBankAccount.isPresent()) {
                PaymentRequestDao prd = paymentRequestDao.get();
                BankAccountDao fromBA = fromBankAccount.get();
                BankAccountDao toBA = toBankAccount.get();
                prd.setStatus(ACCEPTED);
                fromBA.minusBalance(prd.getAmount());
                toBA.plusBalance(prd.getAmount());
                synchronized (sync) {
                    bankAccountsService.save(fromBA);
                    bankAccountsService.save(toBA);
                    paymentRequestService.savePaymentRequest(prd);
                }
                return new ResponseEntity<>(
                        new CustomResponse<>(
                                "OK",
                                Optional.empty(),
                                Optional.empty()),
                        HttpStatus.OK);
            } else throw new CustomError("payment request or client or bank account not found");
        } catch (Exception e) {
            log.error("accept payment request is failed");
            return new ResponseEntity<>(new CustomResponse<>(INTERNAL_ERROR,
                    Optional.of("Accept payment request is failed"),
                    Optional.empty()), HttpStatus.OK);
        }
    }
}


