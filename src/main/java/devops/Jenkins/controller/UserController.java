package devops.Jenkins.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;

import devops.Jenkins.model.User;
import devops.Jenkins.service.S3Service;
import devops.Jenkins.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private S3Service s3Service;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    @GetMapping("/showNewUserForm")
    public String showNewUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "showNewUserForm";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "showFormForUpdate";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("image") MultipartFile image) {

        String filename = image.getOriginalFilename();

        try {
            s3Service.uploadPhotoToS3(image.getInputStream(),filename);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setImgUrl("https://photos-devops.s3.amazonaws.com/"+filename);
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable(value = "id") long id){
        User user = userService.findUserById(id);
        String filename = s3Service.extractFilenameFromUrl(user.getImgUrl());
        System.out.println(filename);
        s3Service.deletePhotoFromS3(filename);
        this.userService.deleteUser(id);
        return "redirect:/";
    }
}
