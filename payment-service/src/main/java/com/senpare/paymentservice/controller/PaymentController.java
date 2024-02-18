package com.senpare.paymentservice.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.senpare.paymentservice.dto.*;
import com.senpare.paymentservice.model.LicenseType;
import com.senpare.paymentservice.model.PaymentLog;
import com.senpare.paymentservice.service.LicenseTypeService;
import com.senpare.paymentservice.service.PaymentLogService;
import com.yaphet.chapa.Chapa;
import com.yaphet.chapa.model.Customization;
import com.yaphet.chapa.model.InitializeResponseData;
import com.yaphet.chapa.model.PostData;
import com.yaphet.chapa.model.VerifyResponseData;
import com.yaphet.chapa.utility.Util;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment-service/payments")
@Tag(name = "Payment endpoint", description = "Processes payment through the specified payment gateway.")
@Slf4j
public class PaymentController {

    private final String paymentModalTitle;
    private final String paymentModalDescription;
    private final String chapaSecreteKey;
    private final String chapaWebhookSecreteKey;
    private final PaymentLogService paymentLogService;
    private final LicenseTypeService licenseTypeService;

    public PaymentController(PaymentLogService paymentLogService,
                             LicenseTypeService licenseTypeService,
                             @Value("${chapa.secrete-key}") String chapaSecreteKey,
                             @Value("${chapa.webhook.secrete-key}") String chapaWebhookSecreteKey,
                             @Value("${app.payment.modalTitle}") String paymentModalTitle,
                             @Value("${app.payment.modalTitle}") String paymentModalDescription) {
        this.paymentLogService = paymentLogService;
        this.licenseTypeService = licenseTypeService;
        this.chapaSecreteKey = chapaSecreteKey;
        this.chapaWebhookSecreteKey = chapaWebhookSecreteKey;
        this.paymentModalTitle = paymentModalTitle;
        this.paymentModalDescription = paymentModalDescription;
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Returns a payment log associated with the specified UUID.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<PaymentLog> get(@PathVariable("uuid") String uuid) {
        PaymentLog payment = paymentLogService.get(UUID.fromString(uuid));

        return ResponseEntity.ok(payment);
    }

    @GetMapping
    @Operation(summary = "Returns a paginated list of all payment logs")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<PaymentLog>> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                   @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy) {
        Page<PaymentLog> payments = paymentLogService.getAll(page, size, sortBy);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(payments.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(payments.getTotalPages()));

        return new ResponseEntity<>(payments.getContent(), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/examiner")
    @Operation(summary = "Returns a paginated list of all payment logs for logged in examiner")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<List<PaymentLog>> getAllByEmail(@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                   @RequestParam(value = "sort", defaultValue = "createdOn") String sortBy,
                                                   Principal principal) {
        String email = principal.getName();
        Page<PaymentLog> payments = paymentLogService.getAllByEmail(page, size, sortBy, email);
        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add("Pagination-Count", String.valueOf(payments.getTotalElements()));
        responseHeaders.add("Pagination-Page", String.valueOf(payments.getTotalPages()));

        return new ResponseEntity<>(payments.getContent(), responseHeaders, HttpStatus.OK);
    }

    @PostMapping("/chapa/initialize")
    @Operation(summary = "Initializes payment through Chapa payment gateway.")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')")
    public ResponseEntity<InitializePaymentResponse> initializeChapaPayment(Principal principal, @RequestBody @Valid InitializePaymentRequest initializePaymentRequest, HttpServletRequest request) throws Throwable {
        Chapa chapa = new Chapa(chapaSecreteKey);
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        PostData postData = mapToPostData(principal.getName(), initializePaymentRequest, baseUrl + "/api/v1/payment-service/payments/chapa/verify/callback");
        log.info("callback url being sent to chapa: {}", postData.getCallbackUrl());
        InitializeResponseData responseData = chapa.initialize(postData);
        InitializePaymentResponse response = new InitializePaymentResponse()
                .setMessage(responseData.getMessage())
                .setTxRef(postData.getTxRef())
                ;

        // If the response status code is 2XX, return the checkout url
        if(responseData.getStatusCode() >= HttpStatus.OK.value() && responseData.getStatusCode() < HttpStatus.MULTIPLE_CHOICES.value()) {
            response.setCheckoutUrl(responseData.getData().getCheckOutUrl());

            PaymentLogRequest paymentLogRequest = new PaymentLogRequest()
                    .setPaymentProvider("chapa")
                    .setEmail(principal.getName())
                    .setTransactionReference(postData.getTxRef())
                    .setAmount(postData.getAmount())
                    ;
            paymentLogService.create(paymentLogRequest);

            log.info("Transaction " + response.getTxRef() + " successfully initialized");
            return ResponseEntity.ok(response);
        } else {
            log.warn("Transaction " + response.getTxRef() + " failed to be initialized with message: " + responseData.getMessage());
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/chapa/verify/callback")
    @Operation(summary = "Serves as the callback url for Chapa payment gateway.")
    public ResponseEntity<Void> verifyChapaPayment(@RequestParam("tx_ref") String txRef, @RequestParam("status") String status) throws Throwable {
        log.info("Callback received from Chapa with transaction reference: {} and status: {}", txRef, status);
        Sentry.captureMessage(String.format("Callback received from Chapa with transaction reference: %s and status: %s", txRef, status), SentryLevel.INFO);

        verifyWithChapa(txRef);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/chapa/webhook")
    @Operation(summary = "Serves as a webhook listener for Chapa payment gateway.")
    public ResponseEntity<Void> chapaWebhook(@RequestHeader("Chapa-Signature") String chapaSignature, @RequestBody ChapaWebhookRequest chapaWebhookRequest, HttpServletRequest request) throws Throwable {
        log.info("Webhook received from Chapa with reference: {} and status: {}", chapaWebhookRequest.getReference(), chapaWebhookRequest.getStatus());
        Sentry.captureMessage(String.format("Webhook received from Chapa with reference: %s and status: %s", chapaWebhookRequest.getReference(), chapaWebhookRequest.getStatus()), SentryLevel.INFO);

        String hash =  HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, chapaWebhookSecreteKey.getBytes(StandardCharsets.UTF_8))
                .doFinal(new JsonMapper().writeValueAsBytes(chapaWebhookRequest))
                .toString();

        if(hash.equals(chapaSignature)) {
            if(chapaWebhookRequest.getStatus().equals("success")) {
                verifyWithChapa(chapaWebhookRequest.getReference());
            }
        }

        return ResponseEntity.ok().build();
    }

    @PutMapping("/chapa/verify/{txRef}")
//    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'EXAMINER')") TODO: this temporary and commented out because of the bug in frontend
    @Operation(summary = "Verifies payment through Chapa payment gateway", parameters = {
            @Parameter(name = "txRef", description = "Transaction reference that uniquely identifies a payment. It is returned from /chapa/initialize endpoint.", required = true),
            @Parameter(name = "forceCheck", description = "Indicates whether to force check the payment status with Chapa payment gateway, if false, it will only check the local database.")
    })

    public ResponseEntity<Boolean> verifyChapaPayment(@PathVariable("txRef") String txRef, @RequestParam(value = "forceCheck", required = false) boolean forceCheck) throws Throwable {
        if(paymentLogService.isPaymentVerified(txRef)) {
            return ResponseEntity.ok(true);
        }

        if(!forceCheck) {
            return ResponseEntity.badRequest().body(false);
        }

        return verifyWithChapa(txRef);
    }

    private ResponseEntity<Boolean> verifyWithChapa(String txRef) throws Throwable {
        log.debug("Making a call to Chapa API to verify payment with a transaction reference: {}", txRef);
        Chapa chapa = new Chapa(chapaSecreteKey);
        VerifyResponseData responseData = chapa.verify(txRef);

        if(HttpStatus.resolve(responseData.getStatusCode()).is2xxSuccessful()) {
            paymentLogService.verifyPayment(txRef);
            log.info("Transaction {} verified", txRef);
            Sentry.captureMessage(String.format("Transaction %s verified", txRef), SentryLevel.INFO);
            return ResponseEntity.ok(true);
        }

        paymentLogService.cancelPayment(txRef);
        log.info("Verification of transaction {} failed with a message: {}", txRef, responseData.getMessage());
        Sentry.captureMessage(String.format("Verification of transaction %s failed with a message: %s", txRef, responseData.getMessage()), SentryLevel.INFO);

        return ResponseEntity.badRequest().body(false);
    }

    private PostData mapToPostData(String examinerEmail, InitializePaymentRequest initializePaymentRequest, String callbackUrl) {
        Customization customization = new Customization()
                .setTitle(paymentModalTitle)
                .setDescription(paymentModalDescription)
                ;

        LicenseType licenseType = licenseTypeService.getByTypeName(initializePaymentRequest.getPaymentType());
        String txRef = "SEM-" + Util.generateToken();

        String returnUrl = initializePaymentRequest.getReturnUrl();
        if(Util.notNullAndEmpty(returnUrl)) {
            returnUrl = returnUrl.endsWith("/") ? returnUrl + txRef : returnUrl + "/" + txRef;
        }

        return new PostData()
                .setAmount(licenseType.getAmount())
                .setCurrency(SupportedCurrency.lookupByCode(initializePaymentRequest.getCurrency()).name())
                .setFirstName("Abebe") // TODO: This value should be extracted from idToken or keycloak
                .setLastName("Bikila")
                .setEmail(examinerEmail)
                .setTxRef(txRef)
                .setCallbackUrl(callbackUrl)
                .setReturnUrl(returnUrl)
                .setCustomization(customization);
    }


    @GetMapping("/generated-revenue")
    @Operation(summary = "Returns generated revenue filtered by status. Payment status can be one of the following: INITIALIZED, SUCCESSFUL, FAILED")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Double> getGeneratedRevenue(@RequestParam("paymentStatus") String paymentStatus) {
        BigDecimal revenue = paymentLogService.getGeneratedRevenue(PaymentStatus.lookupByCode(paymentStatus));
        return ResponseEntity.ok(revenue == null ? 0 : revenue.doubleValue());
    }
}
