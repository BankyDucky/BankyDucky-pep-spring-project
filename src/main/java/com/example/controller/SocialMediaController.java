package com.example.controller;

import java.util.*;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ClientSideException;
import com.example.exception.ConflictionException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService,MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity registerAccount(@RequestBody Account account){
        try{
            Account resultAccount = accountService.registerUser(account);
            return ResponseEntity.status(HttpStatus.OK).body(resultAccount);
        }
        catch(ClientSideException e){
            return ResponseEntity.status(409).body(null);
        }
        catch(ConflictionException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.SEE_OTHER).body(null);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity loginAccount(@RequestBody Account account){
        try{
            Account resultAccount = accountService.loginUser(account);
            return ResponseEntity.status(HttpStatus.OK).body(resultAccount);
        }
        catch(UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.SEE_OTHER).body(null);
        }
    }

    @PostMapping(value = "/messages")
    public ResponseEntity postMessage(@RequestBody Message message){
        try{
            Message resultMessage = messageService.postMessage(message);
            return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
        }
        catch(ClientSideException e){
            return ResponseEntity.status(400).body(null);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.SEE_OTHER).body(null);
        }
    }

    @GetMapping(value = "/messages")
    public ResponseEntity getMessages(){
        List<Message> resultList = messageService.getMessages();
        return ResponseEntity.status(HttpStatus.OK).body(resultList);
    }

    @GetMapping(value = "/messages/{id}")
    public ResponseEntity getMessagesById(@PathVariable Integer id){
        Message resultMessage = messageService.getMessagesById(id);
        return ResponseEntity.status(HttpStatus.OK).body(resultMessage);
    }

    @DeleteMapping(value = "/messages/{id}")
    public ResponseEntity deleteMessageById(@PathVariable Integer id){
        int resultMessageRowCount = messageService.deleteMessageById(id);
        if(resultMessageRowCount == 0){
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(resultMessageRowCount);
    }

    @PatchMapping(value = "/messages/{id}")
    public ResponseEntity patchMessageById(@PathVariable Integer id, @RequestBody Message message){
        try{
            messageService.patchMessageById(id,message);
            return ResponseEntity.status(HttpStatus.OK).body(1);
        }
        catch(ClientSideException e){
            return ResponseEntity.status(400).body(null);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.SEE_OTHER).body(null);
        }
    }

    @GetMapping(value = "/accounts/{accountId}/messages")
    public ResponseEntity getAllMessagesByAccountId(@PathVariable Integer accountId){
        List<Message> finalResult = messageService.getMessageByAccountId(accountId);
        if(finalResult == null){
            return ResponseEntity.status(HttpStatus.OK).body(null); 
        }
        return ResponseEntity.status(HttpStatus.OK).body(finalResult);
    }
}
