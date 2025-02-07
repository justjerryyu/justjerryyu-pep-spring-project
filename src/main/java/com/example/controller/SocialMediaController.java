package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.lang.RuntimeException;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;
    

    /**
     * POST /register
     * Handler to create a new account.
     * 
     * The body will contain a representation of a JSON Account, but will not c
     * ontain an account_id.
     * 
     * The response status should be 200 OK, which is the default. The new 
     * account should be persisted to the database.
     * 
     * If the registration is not successful due to a duplicate username, the 
     * response status should be 409. (Conflict)
     * 
     * If the registration is not successful for some other reason, the 
     * response status should be 400. (Client error)
     * 
     * @param account the account object retrieved from the body of the HTTP request.
     * @throws RuntimeException any other issues that arises.
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) throws RuntimeException {

        // if not null, there is a duplicate
        if (accountService.getAccountByUsername(account.getUsername()) != null) {
            return ResponseEntity.status(409).build();
        }

        Account output = accountService.insertAccount(account);
        if (output != null) {
            return ResponseEntity.ok().body(output);
        }

        return ResponseEntity.status(400).build();
    }


    /**
     * POST /login
     * Handler to verify login credentials.
     * 
     * The request body will contain a JSON representation of an Account, not 
     * containing an account_id.
     * 
     * If successful, the response body should contain a JSON of the account in 
     * the response body, including its account_id. The response status should 
     * be 200 OK, which is the default.
     * 
     * If the login is not successful, the response status should be 401. (Unauthorized)
     * 
     * @param account the account object retrieved from the body of the HTTP request.
     * @throws RuntimeException any other issues that arises.
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) throws RuntimeException {

        Account output = accountService.login(account);
        if (output != null) {
            return ResponseEntity.ok().body(output);
        }

        return ResponseEntity.status(401).build();
    }



    /**
     * POST /messages
     * Handler to create a message.
     * 
     * The request body will contain a JSON representation of a message, which 
     * should be persisted to the database, but will not contain a message_id.
     * 
     * If successful, the response body should contain a JSON of the message, 
     * including its message_id. The response status should be 200, which is 
     * the default. The new message should be persisted to the database.
     *
     * @param message the message object retrieved from the body of the HTTP request.
     * @throws RuntimeException any other issues that arises.
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) throws RuntimeException {

        Message output = messageService.insertMessage(message);
        if (output != null) {
            return ResponseEntity.ok().body(output);
        }

        return ResponseEntity.status(400).build();
    }


    /**
     * PATCH /messages/{message_id}
     * Handler to update a message given id.
     * 
     * The request body should contain a new message_text values to replace the
     * message identified by message_id. The request body can not be guaranteed 
     * to contain any other information. 
     * 
     * If the update is successful, the response body should contain the number 
     * of rows updated (1), and the response status should be 200, which is the 
     * default. The message existing on the database should have the updated 
     * message_text.
     * 
     * If the update of the message is not successful for any reason, the 
     * response status should be 400. (Client error)
     *
     * @param message_id message id of the message object to retrieve.
     * @param message the message object retrieved from the body of the HTTP request.
     * @throws RuntimeException any other issues that arises.
     */
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer message_id, @RequestBody Message message) throws RuntimeException {

        int output = messageService.updateMessage(message_id, message);
        if (output != 0) {
            return ResponseEntity.ok().body(output);
        }

        return ResponseEntity.status(400).build();
    }


    /**
     * DELETE /messages/{message_id}
     * Handler to delete a message given id
     * 
     * The deletion of an existing message should remove an existing message 
     * from the database. If the message existed, the response body should 
     * contain the number of rows updated (1). The response status should be 
     * 200, which is the default. 
     * 
     * If the message did not exist, the response status should be 200, but 
     * the response body should be empty. This is because the DELETE verb is 
     * intended to be idempotent, ie, multiple calls to the DELETE endpoint 
     * should respond with the same type of response.
     *
     * @param message_id message id of the message object to retrieve.
     * @throws RuntimeException any other issues that arises.
     */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer message_id) throws RuntimeException {

        int output = messageService.deleteMessage(message_id);
        if (output != 0) {
            return ResponseEntity.ok().body(output);
        }

        return ResponseEntity.ok().build();
    }


    /**
     * GET /messages
     * Handler to get all message.
     * 
     * The response body should contain a JSON representation of a list 
     * containing all messages retrieved from the database. It is expected for 
     * the list to simply be empty if there are no messages. The response 
     * status should always be 200, which is the default.
     *
     * @param message the message object retrieved from the body of the HTTP request.
     * @throws RuntimeException any other issues that arises.
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() throws RuntimeException {

        List<Message> output = messageService.getAllMessages();
        return ResponseEntity.ok().body(output);
    }


    /**
     * GET /messages/{message_id}
     * Handler to get the message object given id.
     * 
     * The response body should contain a JSON representation of the message 
     * identified by the message_id. It is expected for the response body to 
     * simply be empty if there is no such message. The response status should 
     * always be 200, which is the default.
     *
     * @param message_id message id of the message object to retrieve.
     * @throws RuntimeException any other issues that arises.
     */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessage(@PathVariable Integer message_id) throws RuntimeException {

        Message output = messageService.getMessageById(message_id);
        if (output != null) {
            return ResponseEntity.ok().body(output);
        }

        return ResponseEntity.ok().build();
    }


    /**
     * GET /accounts/{account_id}/messages
     * Handler to get all messages given account id
     * 
     * The response body should contain a JSON representation of a list 
     * containing all messages posted by a particular user, which is retrieved 
     * from the database. It is expected for the list to simply be empty if 
     * there are no messages. The response status should always be 200, 
     * which is the default
     *
     * @param account_id account id. get messages from this account.
     * @throws RuntimeException any other issues that arises.
     */
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesFromUser(@PathVariable Integer account_id) throws RuntimeException {

        List<Message> output = messageService.getMessagesFromAccountId(account_id);
        return ResponseEntity.ok().body(output);
    }


    // @GetMapping(value = "/test", params = {"one", "two"})
    // public String[] getSearchFormatAndAmount(@RequestParam String one, @RequestParam String two){
    // }


    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)     // 400
    public String handleNotFound(RuntimeException ex) {
        return ex.getMessage();
    }
}
