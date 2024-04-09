package rs.edu.raf.banka1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.banka1.dtos.PaymentDto;
import rs.edu.raf.banka1.dtos.TransferDto;
import rs.edu.raf.banka1.model.Transfer;
import rs.edu.raf.banka1.requests.CreateTransferRequest;
import rs.edu.raf.banka1.services.TransferService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }


//ovde fali subTypes = {ExchangeDto.class} nakon schema
//    @GetMapping(value = "/rates/{baseCode}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "Get all exchange rates", description = "Get all rates")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Successful operation",
//                    content = {@Content(mediaType = "application/json",
//                            schema = @Schema(implementation = List.class))}),
//            @ApiResponse(responseCode = "403", description = "Unauthorized"),
//            @ApiResponse(responseCode = "500", description = "Internal server error")
//    })
//    public ResponseEntity<List<ExchangeRate>> getExchangeRates(@PathVariable(name = "baseCode") String baseCode) {
//        List<ExchangeRate> rates = transferService.getExchangeRates(baseCode);
//        return ResponseEntity.ok(rates);
//    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create new transfer", description = "Create new transfer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Non existent accounts"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> createTransfer(@RequestBody CreateTransferRequest createTransferRequest) {
        Long id = transferService.createTransfer(createTransferRequest);
        if (id > -1) {
            transferService.processTransfer(id);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/getAll/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all transfers", description = "Get all transfers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class,
                                    subTypes = {TransferDto.class}))}),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<TransferDto>> getAll(@PathVariable(name = "accountNumber") String accountNumber) {
        return ResponseEntity.ok(transferService.getAllTransfersForAccountNumber(accountNumber));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get transfer by id", description = "Get transfer by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransferDto.class))}),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TransferDto> getById(@PathVariable(name = "id") Long id) {
        TransferDto resp = transferService.getTransferById(id);
        return new ResponseEntity<>(resp, resp != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

}