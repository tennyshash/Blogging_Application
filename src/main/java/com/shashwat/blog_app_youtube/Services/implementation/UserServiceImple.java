package com.shashwat.blog_app_youtube.Services.implementation;

import com.shashwat.blog_app_youtube.Config.AppConstants;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.UserPaginationResponse;
import com.shashwat.blog_app_youtube.Exception.ApiException;
import com.shashwat.blog_app_youtube.Exception.ResourceNotFoundException;
import com.shashwat.blog_app_youtube.Models.Role;
import com.shashwat.blog_app_youtube.Models.User;
import com.shashwat.blog_app_youtube.Repository.RoleRepository;
import com.shashwat.blog_app_youtube.Repository.UserRepository;
import com.shashwat.blog_app_youtube.Services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImple implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImple(UserRepository userRepository, ModelMapper modelMapper,
                            PasswordEncoder passwordEncoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
    }

    @Override
    public UserDto registerNewUSer(UserDto request) {

        // Check For User Exist with Given Email or not ..!
        Optional<User> checkUser=userRepository.findByEmail(request.getEmail());
        if(checkUser.isPresent()){
            throw new ApiException("User Already Exist With Given Email");
        }

        User user=modelMapper.map(request,User.class);
        // password coded
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //roles

        //extra step to make myself admin for first time..!
        if(user.getEmail().equals("shashwatpratap@gmail.com")){
            Role role=roleRepository.findById(AppConstants.ADMIN_USER).get();
            user.getRoles().add(role);
        }else {
            Role role=roleRepository.findById(AppConstants.NORMAL_USER).get();
            user.getRoles().add(role);
        }
        User savedUser=userRepository.save(user);

        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto request, Long userId){

        User user=userRepository.findById(userId).orElseThrow( ()-> new ResourceNotFoundException("User", "ID", userId));

        user.setName(request.getName() == null ? user.getName() : request.getName());
        user.setEmail(request.getEmail() == null ? user.getEmail() : request.getEmail());
        user.setAbout(request.getAbout() == null ? user.getAbout() : request.getAbout());

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }else {
            user.setPassword(user.getPassword());
        }

        User updateUser=userRepository.save(user);

        UserDto response= modelMapper.map( updateUser,UserDto.class);
        response.setPassword(" Mai Nahi btauga .!!");

        return  response;
    }
    @Override
    public UserDto getUserById(Long userId){
        User user= userRepository.findById(userId).orElseThrow( ()-> new ResourceNotFoundException("User", "ID", userId));
        return  userToDto(user);
    }

                                /*          -->>>ADMIN USER Fields <<<<----     */
    @Override
    public UserPaginationResponse getAllUser(Integer pageNumber, Integer pageSize , String sortBy, String sortDir){

        // adding code for sorting by ascending or desc using ternary operator
        Sort sort=sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // DOING PAGINATION
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<User> page=userRepository.findAll(pageable);
        List<User> users= page.getContent();
        List<UserDto> userDto=users.stream().map(user -> userToDto(user)).collect(Collectors.toList());

        UserPaginationResponse userPaginationResponse = new UserPaginationResponse();
            userPaginationResponse.setContent(userDto);
            userPaginationResponse.setPageNumber(page.getNumber());
            userPaginationResponse.setPageSize(page.getSize());
            userPaginationResponse.setTotalElements(page.getTotalElements());

            userPaginationResponse.setTotalPages(page.getTotalPages());
            userPaginationResponse.setLastPage(page.isLast());

        return  userPaginationResponse;
    }
    @Override
    public void deleteUser(Long userId){
        User user=userRepository.findById(userId).orElseThrow( ()-> new ResourceNotFoundException("User" ,"ID" , userId));
         userRepository.delete(user);
    }

    @Override
    public void updateRole(Long userID, Integer roleID) {

        //TODO: update isAdmin in user

        User user= userRepository.findById(userID).orElseThrow( ()-> new ResourceNotFoundException("User", "ID", userID));

        Role role=roleRepository.findById(roleID).get();
        user.setRoles(new HashSet<>());
        user.getRoles().add(role);

        userRepository.save(user);

    }

                        /*          -->>>Model Mapper via Methods <<<<----     */

    private User dtoTOUser(UserDto userDto){
        User user= modelMapper.map(userDto,User.class);
        return  user;
    }

    private UserDto userToDto(User user){
        UserDto userDto= modelMapper.map(user,UserDto.class);
        return userDto;
    }

    @Override
    public UserDto createUser(UserDto userDto){
        User user=dtoTOUser(userDto);
        User savedUSer= userRepository.save(user);
        return userToDto(user);
    }

}
