package com.cs544.vote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cs544.Session;

import jakarta.validation.Valid;

@Controller
public class SessionController {

    @Value("${vote.sessionapi.url}")
    private String sessionApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String index() {
        return "redirect:/sessions";
    }

    @GetMapping("/sessions/add")
    public String showAddSessionForm(Model model) {
        if (!model.containsAttribute("session")) {
            model.addAttribute("session", new Session());
        }
        model.addAttribute("msg", "Add Session");
        return "sessionForm";
    }

    @GetMapping("/sessions")
    public String sessionList(Model model) {
        ResponseEntity<List<Session>> response = restTemplate.exchange(sessionApiUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Session>>() {

                });
        model.addAttribute("voteSessions", response.getBody());

        return "sessionList";
    }

    @PostMapping("/sessions")
    public String createSession(@Valid Session session, BindingResult result, RedirectAttributes attr) {
        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.session", result);
            attr.addFlashAttribute("session", session);
            return "redirect:/sessions/add";
        }
        restTemplate.postForEntity(sessionApiUrl, session, Session.class);
        return "redirect:/sessions";
    }

    @GetMapping("/sessions/delete/{id}")
    public String showDeleteSessionConfirm(@PathVariable Long id, Model model) {
        restTemplate.delete(sessionApiUrl + "/" + id);
        return "redirect:/sessions";
    }

    @PostMapping("/sessions/delete/{id}")
    public String deleteSessionConfirmed(@PathVariable Long id) {
        restTemplate.delete(sessionApiUrl + "/" + id);
        return "redirect:/sessions";
    }

}
