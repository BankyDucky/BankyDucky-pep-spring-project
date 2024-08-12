package com.example.service;

import java.util.*;

import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.ClientSideException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message postMessage(Message message) throws ClientSideException{
        if(message.getMessageText().length() > 255) throw new ClientSideException();
        if(message.getMessageText().trim().length() <= 0) throw new ClientSideException();
        Optional<Account> resultAccount = accountRepository.findById(message.getPostedBy());
        ;
        if(!resultAccount.isPresent()) throw new ClientSideException();
        return messageRepository.save(message);
    }

    public List<Message> getMessages(){
        return messageRepository.findAll();
    }

    public Message getMessagesById(Integer id){
        Optional<Message> resultMessage = messageRepository.findById(id);
        if(resultMessage.isPresent()) return resultMessage.get();
        return null;
    }

    public int deleteMessageById(Integer id){
        Optional<Message> resultMessage = messageRepository.findById(id);
        if(resultMessage.isPresent()){
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public void patchMessageById(Integer id, Message message) throws ClientSideException{
        Optional<Message> resultMessage = messageRepository.findById(id);
        if(resultMessage.isEmpty()) throw new ClientSideException();
        if(message.getMessageText().length() > 255) throw new ClientSideException();
        if(message.getMessageText().trim().length() <= 0) throw new ClientSideException();
        Message innerMessage = resultMessage.get();
        innerMessage.setMessageText(message.getMessageText());
        messageRepository.save(innerMessage);
    }

    public List<Message> getMessageByAccountId(Integer id){
        Optional<List<Message>> allMessagesOfId = messageRepository.findMessageByPostedBy(id);
        if(allMessagesOfId.isEmpty()){
            return null;
        }
        return allMessagesOfId.get();
    }

}
