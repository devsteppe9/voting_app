package com.cs544.vote;

import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.cs544.Session;
import com.cs544.Vote;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class VoteController {

    @Autowired
    private KafkaTemplate<String, Vote> template;

    @Value("${vote.sessionapi.url}")
    private String sessionApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    private final SecureRandom random = new SecureRandom();

    @GetMapping("/votes/{sessionId}")
    public String votes(
            HttpServletRequest req,
            HttpServletResponse res,
            Model model,
            @PathVariable Long sessionId) {

        Cookie voterCookie = WebUtils.getCookie(req, "voter_id");
        String voterId;
        ResponseEntity<Session> response;
        try {
            response = restTemplate.getForEntity(sessionApiUrl + "/" + sessionId, Session.class);
        } catch (HttpClientErrorException.NotFound e) {
            return "notfound";
        }

        Session session = response.getBody();

        if (voterCookie == null) {
            voterId = HexFormat.of().formatHex(random.generateSeed(8));
            Cookie newCookie = new Cookie("voter_id", voterId);
            newCookie.setPath("/");
            res.addCookie(newCookie);
        } else {
            voterId = voterCookie.getValue();
        }

        model.addAttribute("option_a", session.getOptionA());
        model.addAttribute("option_b", session.getOptionB());
        model.addAttribute("title", session.getTitle());
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("hostname", req.getServerName());

        return "index";
    }

    @PostMapping("/votes")
    public String processVote(
            HttpServletRequest req,
            HttpServletResponse res,
            @RequestParam(value = "vote") String vote,
            @RequestParam(value = "sessionId") Long sessionId,
            RedirectAttributes attr) {
        Cookie voterCookie = WebUtils.getCookie(req, "voter_id");
        if (voterCookie == null)
            return "redirect:/";

        String topic = "voter";
        Session s = new Session();
        s.setId(sessionId);
        Vote msg = new Vote(voterCookie.getValue(), vote, s);
        template.send(topic, msg);
        attr.addFlashAttribute("vote", vote);
        return "redirect:/votes/" + sessionId;
    }

}
