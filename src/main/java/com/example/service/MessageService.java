package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    /**
     * Add a new message to the database.
     * 
     * The creation of the message will be successful if and only if the 
     * message_text is not blank, is under 255 characters, and posted_by 
     * refers to a real, existing user.
     *
     * @param message an object representing a new message.
     * @return the newly added message if the add operation was successful, including the message_id. 
     */
    public Message insertMessage(Message message) {
        if (message != null && message.getMessageText().length() > 0 && message.getMessageText().length() <= 255) {
            if (messageRepository.existsByPostedBy(message.getPostedBy())) {
                return messageRepository.save(message);
            }
        }

        return null;
    }


    /**
     * Update an existing message from the database.
     * 
     * The update of a message should be successful if and only if the 
     * message id already exists and the new message_text is not blank and 
     * is not over 255 characters.
     * 
     * @param message_id the ID of the message to be modified.
     * @param message an object containing all data that should replace the values contained by the existing message_id.
     *         the message object does not contain a message ID.
     * @return the number of rows updated (1)
     */
    public int updateMessage(int message_id, Message message) {
        if (message != null && message.getMessageText().length() > 0 && message.getMessageText().length() <= 255) {
            if (messageRepository.existsById(message_id)) {
                Message m = messageRepository.findById(message_id).get();
                m.setMessageText(message.getMessageText());
                messageRepository.save(m);

                return 1;
            }
        }
        
        return 0;
    }


    /**
     * Delete the message with the specific message id.
     *
     * @param message_id a message ID.
     * @return the removed message object, if message_id exist
     */
    public int deleteMessage(int message_id) {
        if (messageRepository.existsById(message_id)) {
            return messageRepository.deleteByIdAndGetCount(message_id);
        }

        return 0;
    }


    /**
     * Retrieve all messages.
     * 
     * @return all messages.
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }


    /**
     * Retrieve a specific message using its message ID.
     *
     * @param message_id a message ID.
     * @return the message object, null if message_id does not exist
     */
    public Message getMessageById(int message_id) {
        Optional<Message> output = messageRepository.findById(message_id);
        return output.isPresent() ? output.get() : null;
    }


    /**
     * Retrieve all messages from a particular user (account_id).
     * 
     * @param account_id a account ID.
     * @return all messages from the user
     */
    public List<Message> getMessagesFromAccountId(int account_id) {
        return messageRepository.findByPostedBy(account_id);
    }
}

