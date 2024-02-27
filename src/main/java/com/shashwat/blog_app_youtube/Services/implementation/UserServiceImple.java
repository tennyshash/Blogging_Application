package com.shashwat.blog_app_youtube.Services.implementation;

import com.shashwat.blog_app_youtube.Config.AppConstants;
import com.shashwat.blog_app_youtube.Dtos_Payloads.ApiResponseDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.ForgetPasswordDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UpdateUserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.UserDto;
import com.shashwat.blog_app_youtube.Dtos_Payloads.Pagination.UserPaginationResponse;
import com.shashwat.blog_app_youtube.Exception.ApiException;
import com.shashwat.blog_app_youtube.Exception.ResourceNotFoundException;
import com.shashwat.blog_app_youtube.Models.Role;
import com.shashwat.blog_app_youtube.Models.User;
import com.shashwat.blog_app_youtube.Repository.RoleRepository;
import com.shashwat.blog_app_youtube.Repository.UserRepository;
import com.shashwat.blog_app_youtube.Services.UserService;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
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
    public UserDto registerNewUSer(UserDto request)  {

        // Check For User Exist with Given Email or not ..!
        Optional<User> checkUser=userRepository.findByEmail(request.getEmail());
        if(checkUser.isPresent()){
            throw new ApiException("User Already Exist With Given Email");
        }

        User user=modelMapper.map(request,User.class);
        // password coded
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //roles
        //extra step to make myself admin for first time !
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
    public ApiResponseDto forgetPassword(ForgetPasswordDto request) {
        // Validating user & user name
        Optional<User> optionalUser=userRepository.findByEmail(request.getEmail());
        if(optionalUser.isEmpty())
            throw  new ApiException("User Does not exist with Given Email");

        User user=optionalUser.get();

        if(!user.getName().equals(request.getName()))
            throw new ApiException("Please Check your Email id &  User Name : They should be of same Account");

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User updateUser=userRepository.save(user);

        ApiResponseDto response= new ApiResponseDto("SUCCESS", "Password Set Successfully.");

        return response;
    }

    @Override
    public UpdateUserDto updateUser(UpdateUserDto request, Long userId){

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

        UpdateUserDto response= modelMapper.map( updateUser,UpdateUserDto.class);
        response.setPassword(" Mai Nahi btauga .!!");

        return  response;
    }
    @Override
    public UserDto getUserById(Long userId){
        User user= userRepository.findById(userId)
                .orElseThrow( ()-> new ResourceNotFoundException("User", "ID", userId));

           /*         List<FollowerResponse> followingList= user.getFollowing().stream().map(
                    user1 -> new FollowerResponse(user1.getId(),user1.getName())
            ).collect(Collectors.toList());

            List<FollowerResponse> followerList=user.getFollowers().stream().map(
                    user1 -> modelMapper.map( user1,FollowerResponse.class)
            ).collect(Collectors.toList());

        response.setFollowing(followingList);
        response.setFollowers(followerList);    */

        return userToDto(user);
    }

    @Override
    public ApiResponseDto followUser(Long followerID, Long followingID) {

        if(followerID==followingID) throw new  ApiException ("Invalid Data.");

        User userWhoFollow= userRepository.findById(followerID)
                .orElseThrow( ()-> new ResourceNotFoundException("User", "ID", followerID) );

        User userToFollow=userRepository.findById(followingID)
                .orElseThrow( ()-> new ResourceNotFoundException("User to Follow with ", "ID" , followingID));

        if(userToFollow.getFollowers().contains(userWhoFollow) && userWhoFollow.getFollowing().contains(userToFollow)){
            return new ApiResponseDto("Success", "Already Followed ");
        }

        userToFollow.getFollowers().add(userWhoFollow);
            userRepository.save(userToFollow);

        userWhoFollow.getFollowing().add(userToFollow);
            userRepository.save(userWhoFollow);

        return new ApiResponseDto("Success", "Followed Successfully");
    }

    @Override
    public ApiResponseDto unFollowUser(Long followerID, Long followingID) {

        if(followerID==followingID) throw new  ApiException ("Invalid Data.");

        User userWhoWantToUnFollow= userRepository.findById(followerID)
                .orElseThrow( ()-> new ResourceNotFoundException("User", "ID", followerID) );

        User userToUnFollow=userRepository.findById(followingID)
                .orElseThrow( ()-> new ResourceNotFoundException("User to Follow with ", "ID" , followingID));

        if(!userToUnFollow.getFollowers().contains(userWhoWantToUnFollow) && !userWhoWantToUnFollow.getFollowing().contains(userToUnFollow)){
            return new ApiResponseDto("Failure", "User Does Not Follow ");
        }

        userToUnFollow.getFollowers().remove(userWhoWantToUnFollow);
        userRepository.save(userToUnFollow);

        userWhoWantToUnFollow.getFollowing().remove(userToUnFollow);
        userRepository.save(userWhoWantToUnFollow);

        return new ApiResponseDto("Success", "UnFollowed Successfully");
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
    public ApiResponseDto registerUsersInBulk(String url) throws IOException {
            FileInputStream fileInputStream= new FileInputStream(url);
            Workbook wb= WorkbookFactory.create(fileInputStream);
            Sheet sheet= wb.getSheet("sheet1");

            for( int i=1;i<sheet.getLastRowNum();i++){
                UserDto userDto= new UserDto();
                for( int j=0;j<sheet.getRow(i).getLastCellNum();j++){
                    Row row= sheet.getRow(i);
                    Cell cell= row.getCell(j);
                    String data=cell.getStringCellValue();
                    switch(j){
                        case 0:
                            userDto.setName(data);
                            break;
                        case 1:
                            userDto.setEmail(data);
                            break;
                        case 2:
                            userDto.setPassword(data);
                            break;
                        case 3:
                            userDto.setAbout(data);
                            break;
                        default:
                            break;
                    }
                }
                // Check For User Exist with Given Email or not ..!
                Optional<User> checkUser=userRepository.findByEmail(userDto.getEmail());
                if(checkUser.isPresent()){
                    this.writeInExcel(url, sheet.getSheetName(),i,sheet.getRow(i).getLastCellNum(),"User Already Exist" );
                    continue;
                }
                this.registerNewUSer( userDto);
                this.writeInExcel(url, sheet.getSheetName(),i,sheet.getRow(i).getLastCellNum(),"User Registered" );

            }
            return new ApiResponseDto("Success" , "All Users registered ");
    }
    @Override
    public void writeInExcel(String url,String sheet,int nRow, int nCell, String Data) throws IOException {
        FileInputStream fileInputStream= new FileInputStream(url);
        Workbook workbook=WorkbookFactory.create(fileInputStream);
        Sheet sheet1= workbook.getSheet(sheet);
        Row row= sheet1.getRow(nRow);
        Cell cell=row.createCell(nCell);
        cell.setCellValue(Data);

        FileOutputStream fileOutputStream= new FileOutputStream(url);
        workbook.write(fileOutputStream);

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
