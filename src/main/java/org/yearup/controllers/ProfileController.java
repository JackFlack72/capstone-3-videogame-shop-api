package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    // other classes this controller needs to work
    private ProfileDao profileDao;  // for database operations
    private UserDao userDao;        // for getting user information

    // @Autowired tells Spring to automatically inject these dependencies
    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao)
    {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    // GET /profile - Get the current user's profile information
    // @GetMapping handles HTTP GET requests
    @GetMapping
    public Profile getProfile(Principal principal)
    {
        try
        {
            // Principal represents the currently logged-in user
            // Get their username from the security token
            String userName = principal.getName();

            // Look up the full User object in the database
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Get the user's profile from the database
            Profile profile = profileDao.getByUserId(userId);

            // If profile doesn't exist, return 404 Not Found error
            if (profile == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
            }

            // Return the profile as JSON
            return profile;
        }
        catch (Exception e)
        {
            // If anything goes wrong, return 500 Internal Server Error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // PUT /profile - Update the current user's profile
    // @PutMapping handles HTTP PUT requests (for updates)
    // @RequestBody tells Spring to read the JSON from the request body and convert it to a Profile object
    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile, Principal principal)
    {
        try
        {
            // Get the currently logged-in user's ID
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Update the profile in the database
            // userId tells us WHICH user's profile to update
            // profile contains the NEW information to save
            profileDao.update(userId, profile);

            // Get and return the updated profile to confirm changes were saved
            return profileDao.getByUserId(userId);
        }
        catch (Exception e)
        {
            // If update fails, return 500 Internal Server Error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}