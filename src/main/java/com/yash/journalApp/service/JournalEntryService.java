package com.yash.journalApp.service;

import com.yash.journalApp.entity.JournalEntry;
import com.yash.journalApp.entity.User;
import com.yash.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveEntry(JournalEntry journalEntity, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntity.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntity);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void saveEntry(JournalEntry journalEntity) {
        journalEntryRepository.save(journalEntity);
    }

    public List<JournalEntry> getAllJournalEntry() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        if(removed) {
            userService.saveUser(user);
            journalEntryRepository.deleteById(id);
        }
    }
}
