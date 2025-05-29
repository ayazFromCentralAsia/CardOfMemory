package com.example.CardOfMemory.mvc;


import com.example.CardOfMemory.dto.auth.AuthenticationResponse;
import com.example.CardOfMemory.dto.auth.SignInRequest;
import com.example.CardOfMemory.dto.auth.SignUpRequest;
import com.example.CardOfMemory.service.Impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationServiceImpl authService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("signInRequest", new SignInRequest());
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String login(@ModelAttribute SignInRequest request, Model model) {
        try {
            AuthenticationResponse response = authService.signIn(request);
            model.addAttribute("user", response);
            return "redirect:/dashboard"; // или куда ты хочешь после входа
        } catch (Exception e) {
            model.addAttribute("error", "Неверный логин или пароль");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "register"; // register.html
    }

    @PostMapping("/register")
    public String register(@ModelAttribute SignUpRequest request, Model model) {
        try {
            // регистрируем
            authService.authenticate(request);

            // сразу логиним
            SignInRequest loginRequest = new SignInRequest();
            loginRequest.setEmail(request.getEmail());
            loginRequest.setPassword(request.getPassword());
            AuthenticationResponse response = authService.signIn(loginRequest);

            model.addAttribute("user", response);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации: " + e.getMessage());
            return "register";
        }
    }

}