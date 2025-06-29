package com.example.consultaMedica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.consultaMedica.model.Consulta;
import com.example.consultaMedica.model.EstadoConsulta;
import com.example.consultaMedica.model.Role;
import com.example.consultaMedica.model.User;
import com.example.consultaMedica.service.ConsultaService;
import com.example.consultaMedica.service.UserLog;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Controller
public class AuthController {

    @Autowired
    private UserLog userService;
    @Autowired
    private ConsultaService consultaService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (user.getRole() == null) {
            user.setRole(Role.ROLE_PACIENTE);
        } else if (!user.getRole().name().startsWith("ROLE_")) {
            user.setRole(Role.valueOf("ROLE_" + user.getRole().name()));
        }
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Este nome de usuário já está em uso.");
        }

        if (result.hasErrors()) {
            return "register";
        }

        userService.save(user);
        return "redirect:/login?success";

    }

    @GetMapping("/")
    public String home() {
        return "redirect:/consultas";
    }

    @GetMapping("/consultas")
    public String consultas(Model model) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().forEach(a -> System.out.println("Authority: " + a.getAuthority()));

        boolean isMedico = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_MEDICO"));
        model.addAttribute("isMedico", isMedico);

        String username;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("usuario", username);

        List<Consulta> consultas;
        if (isMedico) {
            // Se for médico, busca todas as consultas
            // Assumindo que o médico é o usuário logado
            consultas = consultaService.findAll();
        } else {
            // Se não for médico, busca as consultas do paciente logado
            // Assumindo que o paciente é o usuário logado
            User paciente = userService.findByUsername(username);
            consultas = consultaService.findByPaciente(paciente);
            model.addAttribute("usuario", username);

        }
        System.out.println("O que sera retornado: " + username);

        model.addAttribute("consultas", consultas);
        return "consultas";
    }

    @GetMapping("/consultas/nova")
    public String novaConsultaForm(Model model) {
        model.addAttribute("consulta", new Consulta());
        return "form-consulta";
    }

    @PostMapping("/consultas/nova")
    public String salvarConsulta(@ModelAttribute("consulta") Consulta consulta) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User paciente = userService.findByUsername(username);
        consulta.setPaciente(paciente); // Associe o paciente à consulta

        consultaService.save(consulta);
        return "redirect:/consultas";
    }

    @PostMapping("/consultas/cancelar/{id}")
    public String cancelarConsulta(@PathVariable Long id) {
        consultaService.cancelar(id); // Sua lógica de cancelamento
        return "redirect:/consultas?cancelSuccess";
    }

    @PostMapping("/consultas/estado/{id}")
    public String atualizarEstadoConsulta(@PathVariable Long id, @RequestParam("estado") String estado) {
        Consulta consulta = consultaService.findById(id);
                

        consulta.setEstado(EstadoConsulta.valueOf(estado));
        consultaService.save(consulta);

        return "redirect:/consultas?updateSuccess";
    }

}
