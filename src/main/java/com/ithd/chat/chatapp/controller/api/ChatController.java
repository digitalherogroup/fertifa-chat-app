package com.ithd.chat.chatapp.controller.api;

import com.google.gson.Gson;
import com.ithd.chat.chatapp.api.admin.AdminModelRequestDto;
import com.ithd.chat.chatapp.base.BaseEntity;
import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.model.orders.Orders;
import com.ithd.chat.chatapp.service.admin.AdminModelServices;
import com.ithd.chat.chatapp.service.chat.StompChatService;
import com.ithd.chat.chatapp.service.orders.ChatOrdersService;
import com.ithd.chat.chatapp.service.users.UsersChatService;
import com.ithd.chat.chatapp.util.GsonConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.Produces;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class ChatController {

    private final AdminModelServices adminModelServices;
    private final UsersChatService usersChatService;
    private final StompChatService stompChatService;
    private final ChatOrdersService chatOrdersService;
    private final Gson gson;
    private final GsonConverter gsonConverter;

    @GetMapping(value = "/{from_id}")
    @Produces("application/json")
    @ResponseBody
    public ModelAndView chatView(@PathVariable("from_id") Long fromId) {
        ModelAndView modelAndView = new ModelAndView();
        AdminModelRequestDto adminModelRequestDto = new AdminModelRequestDto();
        adminModelRequestDto.setId(fromId);

        BaseResponse<?> fromObjectResponse = adminModelServices.getAdminObject(adminModelRequestDto);
        BaseResponse<?> toObjectListResponse = usersChatService.getUsersListByUpdateDateTimeAndAdminId(fromId);

        modelAndView.addObject("fromObject", gson.toJson(fromObjectResponse));
        modelAndView.addObject("ObjectsList", gson.toJson(toObjectListResponse));
        modelAndView.addObject("fromId", fromId);
        modelAndView.addObject("destination", "admin");

        modelAndView.setViewName("admin-chat-app");

        return modelAndView;
    }


//    @GetMapping(value = "/{from_id}/{to_id}")
//    @Produces("application/json")
//    public
//    ResponseEntity<?> startUserChat(@PathVariable("from_id") Long fromId, @PathVariable("to_id") Long toId) {
//        ModelAndView modelAndView = new ModelAndView();
//        AdminModelRequestDto adminModelRequestDto = new AdminModelRequestDto();
//        adminModelRequestDto.setId(Long.valueOf(fromId));
//        //get admin object
//        BaseResponse<?> fromObjectResponse = adminModelServices.getAdminObject(adminModelRequestDto);
//        //get AllUsers list
//        BaseResponse<?> toObjectListResponse = usersChatService.getUsersListByUpdateDateTimeAndAdminId(fromId);
//        //get user object by id
//        BaseResponse<?> toObjectResponse = usersChatService.getUserObjectAction(String.valueOf(toId));
//        //get chat body
//        BaseResponse<?> chatBodyResponse = stompChatService.getAllChatBodyWithUserIdAndAdminId(String.valueOf(toId), String.valueOf(fromId));
//        //get users ordered
//        BaseResponse<?> orderResponse = chatOrdersService.findOrdersByUserId(String.valueOf(toId));
//        Map<String, Object> body = new LinkedHashMap<>();
////        modelAndView.addObject("toObject", gson.toJson(toObjectResponse));
////        modelAndView.addObject("fromObject", gson.toJson(fromObjectResponse));
////        modelAndView.addObject("ObjectsList", gson.toJson(toObjectListResponse));
////        modelAndView.addObject("fromId", fromId);
////        modelAndView.addObject("toId", toId);
////        modelAndView.addObject("destination", "admin");
////        modelAndView.addObject("chatBody", gson.toJson(chatBodyResponse));
////        modelAndView.addObject("OrdersObject", gson.toJson(orderResponse));
//        body.put("toObject",gson.toJson(toObjectResponse));
//        body.put("fromObject",gson.toJson(fromObjectResponse));
//        body.put("ObjectsList",gson.toJson(toObjectListResponse));
//        body.put("fromId",fromId);
//        body.put("toId",toId);
//        body.put("destination","admin");
//        body.put("chatBody",gson.toJson(chatBodyResponse));
//        body.put("OrdersObject",gson.toJson(orderResponse));
//        //modelAndView.setViewName("admins-chat-app");
//        return new ResponseEntity<>(body,HttpStatus.ACCEPTED);
//    }

    /*
    need to work on this filed
     */
    @GetMapping("/employee/{from_id}")
    @Produces("application/json")
    public @ResponseBody
    ModelAndView startUserChat(@PathVariable("from_id") String fromId) {
        log.info("mtavvvvvvvvv");
        ModelAndView modelAndView = new ModelAndView();
        BaseResponse<?> toObjectListResponse = adminModelServices.getAllAdmins();
        //get user object by id
        BaseResponse<?> fromObjectResponse = usersChatService.getUserObjectAction(fromId);
        //check if user already got chat room
        if (204 != fromObjectResponse.getCode()) {
            log.info("user got chat account");
            //get list of admins
            modelAndView.addObject("fromObject", gson.toJson(fromObjectResponse));
            modelAndView.addObject("ObjectsList", gson.toJson(toObjectListResponse));
            modelAndView.addObject("fromId", fromId);
            modelAndView.addObject("destination", "employee");
        } else {
            //create new room get all details from the other database and
            log.info("Adding new Request for chat with admin from employee id: {} ", fromId);
            usersChatService.createUserAndRoom(fromId);
            modelAndView.addObject("fromObject", gson.toJson(fromObjectResponse));
            modelAndView.addObject("ObjectsList", gson.toJson(toObjectListResponse));
            modelAndView.addObject("fromId", fromId);
            modelAndView.addObject("destination", "employee");
        }
        modelAndView.setViewName("employee-chat-app");
        return modelAndView;
    }

    @GetMapping("/employee/{from_id}/{to_id}")
    @Produces("application/json")
    public @ResponseBody
    ModelAndView startUserAdminChat(@PathVariable("from_id") String fromId, @PathVariable("to_id") String toId) {
        log.info("mtavvvvvvvvv");
        ModelAndView modelAndView = new ModelAndView();
        AdminModelRequestDto adminModelRequestDto = new AdminModelRequestDto();
        adminModelRequestDto.setId(Long.valueOf(toId));
        //get list of admins
        BaseResponse<?> toObjectListResponse = adminModelServices.getAllAdmins();
        //get from object by id
        BaseResponse<?> fromObjectResponse = usersChatService.getUserObjectAction(toId);
        //get to object by id
        //BaseResponse<?> toObjectResponse = adminModelServices.getAdminObject(adminModelRequestDto);
        BaseResponse<?> toObjectResponse = adminModelServices.getAdminObject(adminModelRequestDto);
        //get chat body
        BaseResponse<?> chatBodyResponse = stompChatService.getAllChatBodyWithUserIdAndAdminId(fromId, toId);
        //get users ordered
        BaseResponse<?> orderResponse = chatOrdersService.findOrdersByUserId(fromId);

        modelAndView.addObject("toObject", gson.toJson(toObjectResponse));
        modelAndView.addObject("fromObject", gson.toJson(fromObjectResponse));
        modelAndView.addObject("ObjectsList", gson.toJson(toObjectListResponse));
        modelAndView.addObject("fromId", fromId);
        modelAndView.addObject("toId", toId);
        modelAndView.addObject("destination", "employee");
        modelAndView.addObject("chatBody", gson.toJson(chatBodyResponse));
        modelAndView.addObject("OrdersObject", gson.toJson(orderResponse));

        modelAndView.setViewName("employees-chat-app");
        return modelAndView;
    }

    @GetMapping("/getUserJsonData/{from}/{to}")
    public ResponseEntity<BaseResponse<?>> getUsersList(@PathVariable("from") String from, @PathVariable("to") String to) {
        BaseResponse<?> getUsersResponse;
        getUsersResponse = usersChatService.getUsersListByUpdateDateTimeAndAdminId(Long.parseLong(to));
        if (((ArrayList) getUsersResponse.getData()).size() == 0) {
            getUsersResponse = adminModelServices.getAllAdmins();
        }
        log.info("users list {}", gson.toJson(getUsersResponse));

        return new ResponseEntity<>(getUsersResponse, HttpStatus.ACCEPTED);
    }

//    @GetMapping("/updateChats/{toId}/{fromId}")
//    public ResponseEntity<?> updateChatsById(@PathVariable("toId") String toId, @PathVariable("fromId") String fromId) {
//        BaseResponse<?> getUsersChatResponse = stompChatService.updateChatListById(fromId, toId);
//        return new ResponseEntity<>(getUsersChatResponse, HttpStatus.ACCEPTED);
//    }
//
//    @GetMapping("/updateList/{toId}")
//    public ResponseEntity<?> updateList(@PathVariable("toId") String toId) {
//        BaseResponse<?> response = null;
//        Map<String, Object> getData = gsonConverter.convertObjectToMapObject(usersChatService.getUserObjectAction(toId).getData());
//        log.info("getData {} ", getData);
//        if (getData.get("role").equals("EMPLOYEE") && toId.equals("1") || getData.get("role").equals("ADMIN")) {
//            response = usersChatService.getUsersListByUpdateDateTimeAndAdminId(Long.parseLong(toId));
//        } else if (getData.get("role").equals("EMPLOYEE")) {
//            response = adminModelServices.getAllAdmins();
//        }
//        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
//    }



}