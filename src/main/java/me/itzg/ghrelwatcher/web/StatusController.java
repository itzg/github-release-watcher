package me.itzg.ghrelwatcher.web;

import me.itzg.ghrelwatcher.model.Status;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author Geoff Bourne
 * @since Jun 2018
 */
@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping
    public Status getStatus(Principal principal) {
        final Status status = new Status();

        status.setLoggedIn(principal != null);

        return status;
    }
}
