package pl.kurs.javatestr3.controller;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.kurs.javatestr3.commands.CreateUserCommand;
import pl.kurs.javatestr3.dto.StatusDto;
import pl.kurs.javatestr3.dto.UserFullDto;
import pl.kurs.javatestr3.security.AppUser;
import pl.kurs.javatestr3.service.AppUserService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final AppUserService appUserService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserController(AppUserService appUserService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<StatusDto> addUser(@RequestBody @Valid CreateUserCommand command) {
        AppUser newUser = modelMapper.map(command, AppUser.class);
        newUser.setPassword(passwordEncoder.encode(command.getPassword()));
        AppUser savedUser = appUserService.createUser(newUser, command.getRoleName());

        String savedUserRoleName = savedUser.getRoles().iterator().next().getName();

        return ResponseEntity.ok(new StatusDto("new User with name " + savedUser.getUsername() + " successfully added with role: " + savedUserRoleName));
    }

    @GetMapping
    public ResponseEntity<Page<UserFullDto>> getAllUsers(@PageableDefault Pageable pageable) {
       return new ResponseEntity(appUserService.getAllUsers(pageable).stream()
               .map(this::toFullDto)
               .collect(Collectors.toList()), HttpStatus.OK);
    }

    public UserFullDto toFullDto(AppUser appUser) {
        return modelMapper.map(appUser, UserFullDto.class);
    }
}

