package ru.myhlv.trest.controllers;

import org.springframework.web.bind.annotation.*;
import ru.myhlv.trest.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageController {
    private long counter = 4;

    private List<Map<String, String>> messages = new ArrayList<>(){{
        add(new HashMap<>(){{ put("id", "1"); put("text", "Some message1");}});
        add(new HashMap<>(){{ put("id", "2"); put("text", "Some message2");}});
        add(new HashMap<>(){{ put("id", "3"); put("text", "Some message3");}});
    }};

    @GetMapping
    public List<Map<String, String>> list() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getOne(@PathVariable String id) {
        return getMessage(id);
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++));
        messages.add(message);
        return message;
    }

    @PutMapping("{id}")
    public Map<String, String> update(
            @PathVariable String id,
            @RequestBody Map<String, String> message) {

        Map<String, String> messageFromDb = getMessage(id);
        messageFromDb.putAll(message);
        messageFromDb.put("id", id);
        return messageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Map<String, String> message = getMessage(id);
        messages.remove(message);
    }

    private Map<String, String> getMessage(String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }
}
