package com.ithd.chat.chatapp.impl.users;

import com.ithd.chat.chatapp.api.admin.AdminModelResponseDto;
import com.ithd.chat.chatapp.api.users.UsersResponseDto;
import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.chatserver.model.MessageModel;
import com.ithd.chat.chatapp.exceptions.IdNotFoundException;
import com.ithd.chat.chatapp.mapper.admin.AdminModelMapper;
import com.ithd.chat.chatapp.mapper.users.UsersModelMapper;
import com.ithd.chat.chatapp.model.users.UsersModel;
import com.ithd.chat.chatapp.repository.admin.AdminModelRepository;
import com.ithd.chat.chatapp.repository.users.UserChatDataController;
import com.ithd.chat.chatapp.repository.users.UsersRepository;
import com.ithd.chat.chatapp.service.chat.StompChatService;
import com.ithd.chat.chatapp.service.users.UsersChatService;
import com.ithd.chat.chatapp.util.GsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersChatService {

    private final UsersModelMapper usersChatModelMapper;
    private final UsersRepository usersRepository;
    private final AdminModelRepository adminModelRepository;
    private final GsonConverter gsonConverter;
    private final StompChatService stompChatService;
    private final AdminModelMapper adminModelMapper;

    @Override
    public BaseResponse<?> getUsersListByUpdateDateTimeAndAdminId(Long id) {
        List<Object> body = new ArrayList<>();
        List<Object> data = new ArrayList<>();
        UserChatDataController userChatDataController = new UserChatDataController();

        try {
            //UsersController usersController = new UsersController();
            //List<Users> usersList= usersController.getAllUsers();
            //List<UsersResponseDto> usersResponseDtoList = usersChatModelMapper.fromNoObjectToUsersChatResponse(usersRepository.findAllByAdminModelsIdOrderByUpdatedDesc(id));
            //List<UsersResponseDto> usersResponseDtoList = usersChatModelMapper.fromUsersEEToUserListResponseObject(usersController.getAllUsersForChat());
            List<UsersResponseDto> usersResponseDtoList = usersChatModelMapper.fromUsersEEToUserListResponseObject(userChatDataController.getAllUsersForChat());
            for (int i = 0; i < usersResponseDtoList.size(); i++) {
                usersResponseDtoList.get(i).setCount(stompChatService.countUnreadMessagesById(usersResponseDtoList.get(i).getId()));
                data.add(usersResponseDtoList.get(i));
            }
            body = gsonConverter.convertObjectToListObject(data);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public BaseResponse<?> getUserObjectAction(String id) {
        Map<String, Object> body;
        UserChatDataController userChatDataController = new UserChatDataController();
        if (null == id) throw new IdNotFoundException(id);
        try {
//            if (!usersRepository.existsByUserId(Long.parseLong(id))) {
//                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
//            }
            if (!userChatDataController.userIsExistsById(Long.parseLong(id))) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
            //UsersResponseDto usersResponseDto = usersChatModelMapper.fromUserIdToUserObject(usersRepository.getOne(Long.parseLong(id)));
            //UsersResponseDto usersResponseDto = usersChatModelMapper.fromUserIdToUserObject(usersRepository.getAllByUserId(Long.parseLong(id)));
            UsersResponseDto usersResponseDto = usersChatModelMapper.fromChatUserIdToUserObject(userChatDataController.getUserObjectById(id));
            body = gsonConverter.convertUsersObjectToMapObject(usersResponseDto);
            if (body.isEmpty()) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public BaseResponse<?> getUserObjectActionByUserId(String id, String adminId) {
        Map<String, Object> body;
        if (null == id) throw new IdNotFoundException(id);
        try {
            if (!usersRepository.existsByUserId(Long.parseLong(id))) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
            UsersResponseDto usersResponseDto = usersChatModelMapper.fromUserIdToUserObject(
                    usersRepository.getAllUsersModelByUserIdAndAdminModelsId(Long.parseLong(id), Long.parseLong(adminId)));
            body = gsonConverter.convertUsersObjectToMapObject(usersResponseDto);
            if (body.isEmpty()) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public BaseResponse<?> getUserObjectByUserId(String id, String adminId) {
        Map<String, Object> body;
        if (null == id) throw new IdNotFoundException(id);
        try {
            if (!usersRepository.existsById(Long.parseLong(id))) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
            UsersResponseDto usersResponseDto = usersChatModelMapper.fromUserIdToUserObject(
                    usersRepository.getAllUsersModelByUserIdAndAdminModelsId(Long.parseLong(id), Long.parseLong(adminId)));
            body = gsonConverter.convertUsersObjectToMapObject(usersResponseDto);
            if (body.isEmpty()) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public BaseResponse<?> createUserAndRoom(String employeeId) {
//        Map<String, Object> body;
//        //UsersController usersController = new UsersController();
//        if (null == employeeId) throw new IdNotFoundException(employeeId);
//        try {
//            UsersModel users = usersChatModelMapper.fromUsersEEToUserResponseObject(usersController.getEmployeeObjectById(employeeId));
//            AdminModel adminModel = adminModelRepository.getOne(1L);
//            users.setAdminModels(adminModel);
//            UsersResponseDto usersResponseDto = usersChatModelMapper.fromUserRequestDtoToCreateNewUserResponse(usersRepository.save(users));
//            body = gsonConverter.convertUsersObjectToMapObject(usersResponseDto);
//            if (body.isEmpty()) {
//                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", employeeId));
//            }
//        } catch (Exception e) {
//            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
//        }
//        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
        return null;
    }

    //Rest

    @Override
    public BaseResponse<?> getUsersObjectsById(String adminId, String id) {
        Map<String, Object> body;
        if (null == id) throw new IdNotFoundException(id);
        try {
            if (!usersRepository.existsByUserId(Long.parseLong(id))) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
            UsersResponseDto usersResponseDto = usersChatModelMapper.fromUserIdToUserObject(
                    usersRepository.getAllUsersModelByUserIdAndAdminModelsId(Long.parseLong(id), Long.valueOf(adminId)));
            body = gsonConverter.convertUsersObjectToMapObject(usersResponseDto);
            if (body.isEmpty()) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }

        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public ResponseEntity<BaseResponse<?>> getUserObjectRest(String id) {
        BaseResponse<?> response = getUserByIdRest(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    public BaseResponse<?> getUserByIdRest(String id) {
        Map<String, Object> body;
        UserChatDataController userChatDataController = new UserChatDataController();
        try {
            System.out.println();
            //UsersResponseDto usersResponseDto = usersChatModelMapper.fromUserRequestDtoToCreateNewUserResponse(usersRepository.getOne(Long.parseLong(id)));
            UsersResponseDto usersResponseDto = usersChatModelMapper.fromChatUserIdToUserObject(userChatDataController.getUserObjectById(id));

            body = gsonConverter.convertUsersObjectToMapObject(usersResponseDto);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @Override
    public ResponseEntity<BaseResponse<?>> updateDateByUserId(MessageModel message) {
        BaseResponse<?> response = updateUserDateById(message.getFrom());
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @Override
    public UsersModel getUserById(String userId) throws SQLException {
        UserChatDataController userChatDataController = new UserChatDataController();
        UsersResponseDto usersResponseDto = usersChatModelMapper.fromChatUserIdToUserObject(userChatDataController.getUserObjectById(userId));
        return usersChatModelMapper.fromUserResponseToUserModel(usersResponseDto);
    }

    @Override
    public ResponseEntity<BaseResponse<?>> getAdminObjectRest(String fromId) {
        BaseResponse<?> response = getAdminByIdRest(fromId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    @Override
    public int getCompanyIdByUserID(long fromId) {
        UserChatDataController userChatDataController = new UserChatDataController();
        return userChatDataController.findCompanyIdByUserId(fromId);
    }

    private BaseResponse<?> getAdminByIdRest(String id) {
        Map<String, Object> body;
        UserChatDataController userChatDataController = new UserChatDataController();
        try {
            System.out.println();
            //UsersResponseDto usersResponseDto = usersChatModelMapper.fromUserRequestDtoToCreateNewUserResponse(usersRepository.getOne(Long.parseLong(id)));
            AdminModelResponseDto adminModelResponseDto = adminModelMapper.fromAdminRequestDtoByIdToAdminObject(adminModelRepository.getOne(Long.valueOf(id)));

            body = gsonConverter.convertObjectToMapObject(adminModelResponseDto);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    private BaseResponse<?> updateUserDateById(String from) {
        Map<String, Object> body;
        try {
            UsersModel usersModel = usersRepository.getOne(Long.parseLong(from));
            usersModel.setId(Long.valueOf(from));
            usersModel.setUpdated(new Timestamp(new Date().getTime()));
            UsersResponseDto usersResponseDto = usersChatModelMapper.fromUserRequestDtoToCreateNewUserResponse(usersRepository.save(usersModel));
            body = gsonConverter.convertObjectToMapObject(usersResponseDto);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }
}
