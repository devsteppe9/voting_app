package com.cs544.vote;

import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.WebUtils;

import com.cs544.Vote;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class VoteController {

    @Autowired
    private KafkaTemplate<String, Vote> template;

    private final SecureRandom random = new SecureRandom();

    @GetMapping("/")
    public String index(
            HttpServletRequest req,
            HttpServletResponse res,
            Model model) {

        Cookie voterCookie = WebUtils.getCookie(req, "voter_id");
        String voterId;
        if (voterCookie == null) {
            voterId = HexFormat.of().formatHex(random.generateSeed(8));
            Cookie newCookie = new Cookie("voter_id", voterId);
            newCookie.setPath("/");
            res.addCookie(newCookie);
        } else {
            voterId = voterCookie.getValue();
        }

        model.addAttribute("option_a", "Option A");
        model.addAttribute("option_b", "Option B");
        model.addAttribute("hostname", req.getServerName());
        // model.addAttribute("vote", vote);

        return "index";
    }

    @PostMapping("/")
    public String processVote(
            HttpServletRequest req,
            HttpServletResponse res,
            @RequestParam(value = "vote") String vote,
            RedirectAttributes attr) {
        Cookie voterCookie = WebUtils.getCookie(req, "voter_id");
        if (voterCookie == null)
            return "redirect:/";

        String topic = "voter";
        Vote msg = new Vote(voterCookie.getValue(), vote);
        template.send(topic, msg);
        // System.out.printf("Sent vote to topic '%s': voterId=%s, vote=%s%n", topic, voterCookie.getValue(), vote);

        attr.addFlashAttribute("vote", vote);
        return "redirect:/";
    }

}
