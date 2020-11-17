package com.ithd.chat.chatapp.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ithd.chat.chatapp.api.admin.AdminModelRequestDto;
import com.ithd.chat.chatapp.api.admin.AdminModelResponseDto;
import com.ithd.chat.chatapp.api.documents.DocumentsResponseDto;
import com.ithd.chat.chatapp.api.orders.OrdersResponseDto;
import com.ithd.chat.chatapp.api.products.ProductsResponseDto;
import com.ithd.chat.chatapp.api.users.UsersResponseDto;
import com.ithd.chat.chatapp.base.BaseResponse;
import com.ithd.chat.chatapp.controller.api.ChatController;
import com.ithd.chat.chatapp.exceptions.ObjectNullException;
import com.ithd.chat.chatapp.mapper.admin.AdminModelMapper;
import com.ithd.chat.chatapp.mapper.chat.StompChatMapper;
import com.ithd.chat.chatapp.mapper.documents.DocumentsMapper;
import com.ithd.chat.chatapp.mapper.orders.OrdersMapper;
import com.ithd.chat.chatapp.mapper.products.ProductsMapper;
import com.ithd.chat.chatapp.mapper.users.UsersModelMapper;
import com.ithd.chat.chatapp.model.chat.MessagingStompChat;
import com.ithd.chat.chatapp.model.chat.MessagingStompResponseChat;
import com.ithd.chat.chatapp.model.documents.Documents;
import com.ithd.chat.chatapp.model.orders.Orders;
import com.ithd.chat.chatapp.model.orders.OrdersModel;
import com.ithd.chat.chatapp.model.products.StripePrice;
import com.ithd.chat.chatapp.model.products.StripeProducts;
import com.ithd.chat.chatapp.model.shoppingcard.PaymentRequest;
import com.ithd.chat.chatapp.model.shoppingcard.ProductObject;
import com.ithd.chat.chatapp.model.shoppingcard.ShoppingCard;
import com.ithd.chat.chatapp.model.users.Users;
import com.ithd.chat.chatapp.orders.DateDate;
import com.ithd.chat.chatapp.orders.Dates;
import com.ithd.chat.chatapp.orders.OrdersController;
import com.ithd.chat.chatapp.repository.admin.AdminModelRepository;
import com.ithd.chat.chatapp.repository.chat.StompChatRepository;
import com.ithd.chat.chatapp.repository.documents.DocumentsRepository;
import com.ithd.chat.chatapp.repository.orders.OrderChatController;
import com.ithd.chat.chatapp.repository.orders.OrdersRepository;
import com.ithd.chat.chatapp.repository.products.ProductsRepository;
import com.ithd.chat.chatapp.shoppingCard.ShoppingCartController;
import com.ithd.chat.chatapp.repository.users.UserChatDataController;
import com.ithd.chat.chatapp.repository.users.UsersRepository;
import com.ithd.chat.chatapp.service.admin.AdminModelServices;
import com.ithd.chat.chatapp.service.chat.StompChatService;
import com.ithd.chat.chatapp.service.users.UsersChatService;
import com.ithd.chat.chatapp.storage.FileService;
import com.ithd.chat.chatapp.stripe.UsersController;
import com.ithd.chat.chatapp.stripe.payments.ShoppingCart;
import com.ithd.chat.chatapp.stripe.stripe.NewStripeChargeShoppingCardRequest;
import com.ithd.chat.chatapp.stripe.stripecontroller.NewStripeCardController;
import com.ithd.chat.chatapp.stripe.stripecontroller.NewStripeUsersControllerDao;
import com.ithd.chat.chatapp.stripe.stripe.NewStripeCardResponse;
import com.ithd.chat.chatapp.stripe.stripe.NewStripeUsersResponse;
import com.ithd.chat.chatapp.time.ServiceDateTime;
import com.ithd.chat.chatapp.util.DecodingEncoding;
import com.ithd.chat.chatapp.util.GsonConverter;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1")
public class ControllerRest {
    private String paymentId = "";
    private final AdminModelServices adminModelServices;
    private final UsersChatService usersChatService;
    private final StompChatService stompChatService;
    private final Gson gson;
    private final GsonConverter gsonConverter;
    private final UsersModelMapper usersChatModelMapper;
    private final OrdersMapper ordersMapper;
    private final StompChatRepository stompChatRepository;
    private final StompChatMapper stompChatMapper;
    private final DocumentsMapper documentsMapper;
    private final DocumentsRepository documentsRepository;
    private final FileService fileService;
    private final ProductsRepository productsRepository;
    private final ProductsMapper productsMapper;
    private final OrdersRepository ordersRepository;
    private final UsersRepository usersRepository;
    private final AdminModelMapper adminModelMapper;
    private final AdminModelRepository adminModelRepository;
    private final NewStripeUsersControllerDao newStripeUsersControllerDao = new NewStripeUsersControllerDao();
    private NewStripeUsersResponse newStripeUsersResponse = new NewStripeUsersResponse();
    private final NewStripeCardController newStripeCardController = new NewStripeCardController();
    private NewStripeCardResponse newStripeCardResponse = new NewStripeCardResponse();
    private final DecodingEncoding decodingEncoding;
    private List<Integer> resultId = new ArrayList<>();


    //update chat count
    @GetMapping("/updateChats/{toId}/{fromId}")
    public ResponseEntity<?> updateChatsById(@PathVariable("toId") String toId, @PathVariable("fromId") String fromId) {
        BaseResponse<?> getUsersChatResponse = stompChatService.updateChatListById(fromId, toId);
        return new ResponseEntity<>(getUsersChatResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/toObject/{to_id}")
    @Produces("application/json")
    public ResponseEntity<?> getToObjectChat(@PathVariable("to_id") Long toId) {
        //get user object by id
        BaseResponse<?> toObjectResponse = usersChatService.getUserObjectAction(String.valueOf(toId));
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("toObject", gson.toJson(toObjectResponse));
        return new ResponseEntity<>(body, HttpStatus.ACCEPTED);
    }

    /*
    Get from object
     */
    @GetMapping(value = "/fromObject/{role}/{from_id}")
    @Produces("application/json")
    public ResponseEntity<?> getFromObjectChat(@PathVariable("role") String role, @PathVariable("from_id") Long fromId) {
        BaseResponse<?> response = null;
        if (role.equals("ADMIN") || role.equals("NURSE")) {
            AdminModelRequestDto adminModelRequestDto = new AdminModelRequestDto();
            adminModelRequestDto.setId(Long.valueOf(fromId));
            response = getResponseFromAdminObject(adminModelRequestDto);
        } else {
            response = getResponseFromUserObject(String.valueOf(fromId));
        }
        return new ResponseEntity<>(response.getData(), HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getResponseFromUserObject(String id) {
        Map<String, Object> body;
        BaseResponse<?> responseFromObject = usersChatService.getUserObjectAction(id);
        body = gsonConverter.convertObjectToMapObject(responseFromObject);
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    private BaseResponse<?> getResponseFromAdminObject(AdminModelRequestDto adminModelRequestDto) {
        Map<String, Object> body;
        BaseResponse<?> responseFromObject = adminModelServices.getAdminObject(adminModelRequestDto);
        body = gsonConverter.convertObjectToMapObject(responseFromObject);
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }


    /*
    Chat body response
     */
    @GetMapping(value = "/chatBody/{from_id}/{to_id}")
    @Produces("application/json")
    public ResponseEntity<?> getChatBodyChat(@PathVariable("from_id") String fromId, @PathVariable("to_id") String toId) {
        BaseResponse<?> response = getResponseChatBody(fromId, toId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getResponseChatBody(String fromId, String toId) {
        if (null == toId || null == fromId) throw new ObjectNullException();
        List<String> body;
        try {
            List<MessagingStompResponseChat> messagingStompChat = new ArrayList<>();
            List<MessagingStompResponseChat> responseChats = stompChatMapper.fromUserIDToChatBody(stompChatRepository.getDataByQuery(fromId, toId));
            messagingStompChat.addAll(responseChats);
            body = gsonConverter.gsonObjectConverter(messagingStompChat);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    /*
    Get list of
     */
    @GetMapping(value = "/list/{from_id}")
    @Produces("application/json")
    public ResponseEntity<?> getList(@PathVariable("from_id") Long fromId) throws SQLException {
        BaseResponse<?> response = null;
        if (null != usersChatService.getUserById(String.valueOf(fromId)) && fromId > 10) {
            response = getChatResponseAdmins(fromId);
        } else {
            AdminModelRequestDto adminModelRequestDto = new AdminModelRequestDto();
            adminModelRequestDto.setId(Long.valueOf(fromId));
            response = getChatResponseUsers(fromId);
        }
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getChatResponseAdmins(long fromId) {
        List<String> body;
        List<Object> data = new ArrayList<>();
        int companyIdByUserID = usersChatService.getCompanyIdByUserID(fromId);
        if (companyIdByUserID == 0) {
            try {
                List<AdminModelResponseDto> response = adminModelMapper.toAdminResponseList(adminModelRepository.findAllById(1L));
                for (int i = 0; i < response.size(); i++) {
                    response.get(i).setCount(stompChatService.countUnreadMessagesAdminUserById(response.get(i).getId(), Long.valueOf(fromId)));
                    data.add(response.get(i));
                }
                body = gsonConverter.gsonObjectConverter(data);
            } catch (Exception e) {
                return new BaseResponse<>(new Date(), HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), e.getMessage());
            }
        } else {
            //AdminController adminController = new AdminController();
            try {
                List<AdminModelResponseDto> response = adminModelMapper.toAdminResponseList(adminModelRepository.findAll());
                for (int i = 0; i < response.size(); i++) {
                    response.get(i).setCount(stompChatService.countUnreadMessagesAdminUserById(response.get(i).getId(), Long.valueOf(fromId)));
                    data.add(response.get(i));
                }
                body = gsonConverter.gsonObjectConverter(data);
            } catch (Exception e) {
                return new BaseResponse<>(new Date(), HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), e.getMessage());
            }
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    private BaseResponse<?> getChatResponseUsers(Long fromId) {
        List<Object> body = new ArrayList<>();
        List<Object> data = new ArrayList<>();
        UserChatDataController userChatDataController = new UserChatDataController();
        try {
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

    /*
   Get orders list of
    */
    @GetMapping(value = "/orders/{from_id}/{toId}/{role}")
    @Produces("application/json")
    public ResponseEntity<?> getOrdersList(@PathVariable("from_id") String fromId,
                                           @PathVariable("toId") String toId,
                                           @PathVariable("role") String roles) {
        String theId = "";
        if (roles.equals("ADMIN")) {
            theId = toId;
        } else {
            theId = fromId;
        }
        BaseResponse<?> response = getOrders(theId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    @GetMapping(value = "/orders/{from_id}/{toId}")
    @Produces("application/json")
    public ResponseEntity<?> getOrdersUpdateList(@PathVariable("from_id") String fromId,
                                                 @PathVariable("toId") String toId) {
        String theId = "";
        if (adminModelRepository.existsById(Long.valueOf(fromId))) {
            theId = toId;
        } else {
            theId = fromId;
        }
        BaseResponse<?> response = getOrders(theId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getOrders(String id) {
        List<Object> body;
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        try {
            List<OrdersResponseDto> ordersModels = ordersMapper.fromUserIdAndUserAppControllerToChatOrderListObject(shoppingCartController.getAllOrdersById(Integer.parseInt(id)));
            body = gsonConverter.convertOrdersToListObject(ordersModels);
            if (body.isEmpty()) {
                return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), String.format("No Data for the id:%s", id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    /*
    Get chat
     */
    @GetMapping(value = "/chat/{toId}/{from_id}")
    @Produces("application/json")
    public ResponseEntity<?> getChats(@PathVariable("toId") String toId, @PathVariable("from_id") String fromId) {
        BaseResponse<?> response = getChat(toId, fromId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    private BaseResponse<?> getChat(String toId, String fromId) {
        if (null == toId || null == fromId) throw new ObjectNullException();
        List<String> body;
        try {
            List<MessagingStompResponseChat> responseChats = stompChatMapper.fromUserIDToChatBody(stompChatRepository.getDataByQuery(fromId, toId));
            body = gsonConverter.gsonObjectConverter(responseChats);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @GetMapping("/chat/lastId")
    public ResponseEntity<?> getLastChatId() {
        int response = stompChatService.getLastId();
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    /*
    uploading
     */
    @PostMapping(value = "/upload/{username}")
    @ResponseBody
    @Produces("application/json")
    public BaseResponse<?> upload(@PathVariable("username") String username,
                                  @RequestParam("file") @Nullable MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int code = 0;
        ResponseEntity<?> response;
        try {
            if (file == null) throw new AssertionError();
            response = fileService.uploadFile(file);
            result = (Map<String, Object>) response.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        if (response.getStatusCode().value() >= 200 && response.getStatusCode().value() <= 299) {
            saveDocument(result, username);
            return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), result);
        }
        return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), result);
    }

    private BaseResponse<?> saveDocument(Map<String, Object> file, String username) {
        Map<String, Object> body;
        try {
            Documents documents = documentsMapper.fromMultipartFileToDocument(file, username);
            DocumentsResponseDto documentsResponseDto = documentsMapper.fromDocumentToDocumentResponseDto(documentsRepository.save(documents));
            body = gsonConverter.convertObjectToMapObject(documentsResponseDto);
            if (body.isEmpty()) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the file:%s", file));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    /*
  get order by id
   */
    @GetMapping(value = "/orderId/{id}")
    @Produces("application/json")
    public ResponseEntity<?> getOrderById(@PathVariable("id") String id) {
        BaseResponse<?> response = getOrder(id);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getOrder(String id) {
        Map<String, Object> body;
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        try {
            OrdersResponseDto ordersModels = ordersMapper.fromOrderIdToShoppingDto(shoppingCartController.getObjectById(Integer.parseInt(id)));
            body = gsonConverter.convertObjectToMapObject(ordersModels);
            if (body.isEmpty()) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    /*
    get Documents
     */
    @GetMapping(value = "/documents/{fromId}/{toId}/{role}")
    @Produces("application/json")
    public ResponseEntity<?> getDocumentsById(@PathVariable("fromId") Long fromId,
                                              @PathVariable("toId") Long toId,
                                              @PathVariable("role") String role) {
        Long thId = 0l;
        if (role.equals("ADMIN")) {
            thId = toId;
        } else {
            thId = fromId;
        }
        BaseResponse<?> response = getDocument(thId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/documents/{fromId}/{toId}")
    @Produces("application/json")
    public ResponseEntity<?> getDocumentsById(@PathVariable("fromId") Long fromId,
                                              @PathVariable("toId") Long toId) throws SQLException {
        Long thId = 0l;
        if (adminModelRepository.existsById(fromId)) {
            thId = toId;
        } else {
            thId = fromId;
        }
        BaseResponse<?> response = getDocument(thId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getDocument(Long id) {
        List<Object> body;
        try {
            List<DocumentsResponseDto> documentsResponseDto = documentsMapper.fromDocumentIdToDocumentList(documentsRepository.findAllByUserIdOrderByUpdated(id));
            body = gsonConverter.convertObjectToListObject(documentsResponseDto);
            if (body.isEmpty()) {
                return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @GetMapping(value = "/documents/getId/{id}")
    @Produces("application/json")
    public ResponseEntity<?> getDocumentId(@PathVariable("id") Long id) {
        BaseResponse<?> response = getDocumentById(id);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getDocumentById(Long id) {
        Map<String, Object> body;
        try {
            DocumentsResponseDto documentsResponseDto = documentsMapper.fromDocumentToDocumentResponseDto(documentsRepository.findAllById(id));
            body = gsonConverter.convertObjectToMapObject(documentsResponseDto);
            if (body.isEmpty()) {
                return new BaseResponse<>(new Date(), HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value(), String.format("No Data for the id:%s", id));
            }
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }


    @GetMapping(value = "/register/user/{fromId}/{role}")
    @Produces("application/json")
    public ResponseEntity<?> register(@PathVariable("fromId") String fromId, @PathVariable("role") String role) {
        ResponseEntity<?> response = null;
        if (role.equals("ADMIN")) {
            response = usersChatService.getAdminObjectRest(fromId);
        } else {
            response = usersChatService.getUserObjectRest(fromId);
        }
        return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());
    }

    @GetMapping(value = "/products/list")
    public ResponseEntity<?> getProductsList() {
        BaseResponse<?> response = getProducts();
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getProducts() {
        List<Object> body = new ArrayList<>();
        try {
            List<ProductsResponseDto> productsResponseDto = productsMapper.fromChatToProductsResponseDeto(productsRepository.findAll());
            body = gsonConverter.convertObjectToListObject(productsResponseDto);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @GetMapping("/productById/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {
        BaseResponse<?> response = getProduct(productId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getProduct(Long productId) {
        Map<String, Object> body;
        try {
            ProductsResponseDto productsResponseDto = productsMapper.fromProductIdChatToProductResponseDto(productsRepository.getOne(productId));
            body = gsonConverter.convertObjectToMapObject(productsResponseDto);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), body);
    }

    @PostMapping(value = "/products/add/{userId}/{taxAmount}/{finalTotal}/{finalMainTotal}/{chatId}")
    @ResponseBody
    public ResponseEntity<?> saveProductForUser(@PathVariable("userId") String userId,
                                                @PathVariable("taxAmount") String taxAmount,
                                                @PathVariable("finalTotal") String finalTotal,
                                                @PathVariable("finalMainTotal") String finalMainTotal,
                                                @PathVariable("chatId") int chatId,
                                                @RequestBody List<ProductObject> commits) throws SQLException {
        log.info("inside saveProductForUser");
        BaseResponse<?> response = saveOrder(userId, taxAmount, finalTotal, commits, finalMainTotal);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private String BuildProductInStrip(String serviceName) {
        String stripProductId = "";

        StripeProducts products = StripeProducts.builder()
                .active(true)
                .name(serviceName)
                .build();

        String productsResponse = createStripProduct(products);
        Map map = gson.fromJson(productsResponse, Map.class);
        Map<String, String> paymentStripId = gsonConverter.convertStringToMapToMapObject(map.get("data"));
        stripProductId = paymentStripId.get("id");
        log.info("paymentId {}", paymentId);

        return stripProductId;
    }

    private String createStripProduct(StripeProducts jsonBody) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(
                "http://second.fertifabenefits.com/stripe/stripe/v1/products/create",
                jsonBody,
                String.class);

        log.info("response Stripe products Created {} ========================>", response);
        return response;
    }

    private String BuildProductPriceStrip(String productId, String price, String userId) {
        String stripPriceId = "";
        StripePrice stripePrice = StripePrice.builder()
                .price(Float.parseFloat(price) * 100)
                .productId(productId)
                .userId(userId)
                .build();
        String priceResponse = createProductPrice(stripePrice);
        Map map = gson.fromJson(priceResponse, Map.class);
        Map<String, String> priceStripId = gsonConverter.convertStringToMapToMapObject(map.get("data"));
        stripPriceId = priceStripId.get("id");
        return stripPriceId;
    }

    private String createProductPrice(StripePrice jsonBody) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(
                "http://second.fertifabenefits.com/stripe/stripe/v1/price/create",
                jsonBody,
                String.class);

        log.info("response Stripe price Created {} ========================>", response);
        return response;
    }

    private BaseResponse<?> saveOrder(String userId, String taxAmount, String finalTotal, List<ProductObject> commits, String finalMainTotal) throws SQLException {
        Map<String, Object> body = new LinkedHashMap<>();
        int result = 0;
        int orderId = 0;
        int chatId = 0;
        String productId = "";
        String priceId = "";
        String role = "user";
        if (null != usersChatService.getUserById(userId)) {
            role = "admin";
        }

        try {
            TimeUnit.SECONDS.sleep(3);
            chatId = stompChatService.getLastId();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<ShoppingCard> shoppingCardList = new ArrayList<>();
        Map<String, Object> response = new LinkedHashMap<>();
        ShoppingCard shoppingCard = new ShoppingCard();
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        //Creating product and price in Stripe
        for (int i = 0; i < commits.size(); i++) {
            productId = BuildProductInStrip(commits.get(i).getServiceName());
            priceId = BuildProductPriceStrip(productId, commits.get(i).getPrice(), userId);
            log.info("ProductId {}, priceId {}", productId, priceId);
        }


        shoppingCard.setChatId(chatId);
        shoppingCard.setProductId(productId);
        shoppingCard.setPriceId(priceId);
        resultId = new ArrayList<>();

        saveOrdersToLocal(commits, userId, taxAmount, finalTotal, chatId, priceId, productId);
        saveOrdersToWeb(commits, userId, finalTotal, chatId, priceId, productId);
        orderId = getLastId(userId);
        saveShoppingCardObject(orderId, commits, userId, finalTotal, chatId, productId, priceId);

        try {
            for (int i = 0; i < resultId.size(); i++) {
                shoppingCardList.addAll(shoppingCartController.getAllOrdersAfterChat(resultId.get(i)));
            }
            response.put("data", shoppingCardList);
            response.put("chatId", chatId);
            response.put("taxAmount", taxAmount);
            response.put("finalTotal", finalTotal);
            response.put("finalMainTotal", finalMainTotal);
            response.put("role", role);
            body = gsonConverter.convertObjectToMapObject(response);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ALREADY_REPORTED.value(), body);
    }

    private ShoppingCard saveShoppingCardObject(int orderId,
                                                List<ProductObject> commits,
                                                String userId,
                                                String finalTotal,
                                                int chatId,
                                                String productId,
                                                String priceId) throws SQLException {
        ShoppingCard shoppingCard = new ShoppingCard();

        int result = 0;
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        for (int i = 0; i < commits.size(); i++) {
            shoppingCard.setUser_id(Long.parseLong(userId));
            shoppingCard.setOrder_id(orderId);
            shoppingCard.setServiceName(commits.get(i).getServiceName());
            shoppingCard.setPrice(Float.parseFloat(commits.get(i).getPrice()));
            shoppingCard.setOrder_date(new Timestamp(new Date().getTime()).toString());
            shoppingCard.setStatus(0);
            shoppingCard.setChatId(chatId);
            shoppingCard.setType(commits.get(i).getType());
            shoppingCard.setPriceId(priceId);
            shoppingCard.setProductId(productId);
            shoppingCard.setServiceId(Integer.parseInt(commits.get(i).getServiceId()));
            if (commits.get(i).getServiceTime() == 0 && commits.get(i).getServiceId().equals("0")) {
                shoppingCard.setServiceTime(0.3f);
            } else {
                shoppingCard.setServiceTime(commits.get(i).getServiceTime());
            }

            result = shoppingCartController.AddNewOrder(shoppingCard);
            if (result != 0) {
                int lastId = shoppingCartController.getLastId();
                resultId.add(lastId);
            }
        }
        return shoppingCard;
    }

    private int getLastId(String userId) throws SQLException {
        OrderChatController orderChatController = new OrderChatController();
        return orderChatController.lastId(Integer.parseInt(userId));
    }

    private int saveOrdersToWeb(List<ProductObject> commits, String userId, String finalTotal, int chatId, String productId, String priceId) throws SQLException {
        OrderChatController orderChatController = new OrderChatController();
        int result = 0;
        for (int i = 0; i < commits.size(); i++) {
            Orders orders = new Orders();
            orders.setUser_id(Integer.parseInt(userId));
            orders.setDate_id(0);
            orders.setService_id(Integer.parseInt(commits.get(i).getServiceId()));
            orders.setAppointment("");
            orders.setClinic("");
            orders.setAddress("");
            orders.setCoast(Float.parseFloat(finalTotal));
            orders.setStatus(0);
            orders.setTimeId(0);
            orders.setCreation_date(new Timestamp(new Date().getTime()));
            orders.setSessionToken("");
            orders.setChatId(chatId);
            orders.setPriceId(priceId);
            orders.setProductId(productId);
            orders.setType(commits.get(0).getType());
            orders.setServiceTime(String.valueOf(commits.get(i).getServiceTime()));
            result = orderChatController.AddNewOrder(orders);
        }
        return result;
    }

    private OrdersResponseDto saveOrdersToLocal(@Nullable List<ProductObject> commits,
                                                @Nullable String userId,
                                                @Nullable String taxAmount,
                                                @Nullable String finalTotal,
                                                @Nullable int chatId,
                                                @Nullable String priceId,
                                                @Nullable String productId) {
        OrdersResponseDto ordersResponseDto = new OrdersResponseDto();
        for (int i = 0; i < commits.size(); i++) {
            OrdersModel ordersModel = new OrdersModel();
            ordersModel.setUser_id(Integer.parseInt(userId));
            ordersModel.setPrice(Float.parseFloat(commits.get(i).getPrice()));
            ordersModel.setOrder_date(new Timestamp(new Date().getTime()).toString());
            ordersModel.setStatus(0);
            ordersModel.setServiceName(commits.get(i).getServiceName());
            ordersModel.setTotal(Double.parseDouble(finalTotal));
            ordersModel.setOrderTitle(commits.get(i).getServiceName());
            ordersModel.setQuantity("1");
            ordersModel.setTax(Double.parseDouble(taxAmount));
            ordersModel.setChatId(chatId);
            ordersModel.setPriceId(priceId);
            ordersModel.setProductId(productId);
            ordersModel.setServiceTime(commits.get(i).getServiceTime());
            ordersModel = ordersRepository.save(ordersModel);
            ordersResponseDto = ordersMapper.fromOrderModelToOrderResponseDto(ordersModel);
        }
        return ordersResponseDto;
    }

    @GetMapping("/products/get/{chatId}/{chatManagerId}")
    public BaseResponse<?> getShopById(@PathVariable("chatId") int chatId,
                                       @PathVariable("chatManagerId") String managerId) throws SQLException {
        String role = "user";
        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> body = new LinkedHashMap<>();
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        if (null != usersChatService.getUserById(managerId)) {
            role = "admin";
        }
        try {
            List<ShoppingCart> shoppingCardsLis = new ArrayList<>();
            shoppingCardsLis = shoppingCartController.getAllByChatId(chatId);
            MessagingStompChat messagingStompChat = stompChatService.getChatById(chatId);
            Map<String, Object> gsonData = gsonConverter.convertObjectToMapObject(gson.fromJson(messagingStompChat.getMessageBody(), Object.class));

            response.put("data", shoppingCardsLis);
            response.put("role", role);
            response.put("chatId", chatId);
            response.put("taxAmount", gsonData.get("taxAmount"));
            response.put("finalTotal", gsonData.get("finalTotal"));
            response.put("finalMainTotal", gsonData.get("finalMainTotal"));

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), response);
    }

    @PostMapping(value = "/products/update/{userId}")
    public BaseResponse<?> updateProductForUser(@RequestBody Map<String, Object> productObject, @PathVariable("userId") String userId) throws SQLException {
        String productId = "";
        String priceId = "";
        log.info("inside saveProductForUser");
        String productName = "";
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        Map<String, Object> gsonMap = gsonConverter.convertObjectToMapObject(productObject);
        List<ProductObject> listObjects = gsonConverter.convertProductObjectToListObject(gsonMap.get("dataArray"));
        ShoppingCard shoppingCard = new ShoppingCard();

        for (int i = 0; i < listObjects.size(); i++) {

            int shoppingCardId = listObjects.get(i).getRowId();
            if (listObjects.get(i).getType() == 1) {
                ProductsResponseDto products = productsMapper.fromProductIdChatToProductResponseDto(productsRepository.getOne(Long.valueOf(listObjects.get(i).getServiceId())));
                productName = products.getTitle();
            } else {
                productName = listObjects.get(i).getServiceName();
            }
            productId = BuildProductInStrip(productName);
            priceId = BuildProductPriceStrip(productId, listObjects.get(i).getPrice(), userId);
            log.info("ProductId {}, priceId {}", productId, priceId);

            shoppingCard.setUser_id(listObjects.get(i).getUserId());
            shoppingCard.setId(listObjects.get(i).getRowId());
            if (listObjects.get(i).getServiceId() == null) {
                shoppingCard.setServiceId(0);
            } else {

                shoppingCard.setServiceId(Integer.parseInt(listObjects.get(i).getServiceId()));
            }
            shoppingCard.setType(listObjects.get(i).getType());
            shoppingCard.setStatus(0);
            shoppingCard.setPrice(Float.parseFloat(listObjects.get(i).getPrice()));
            shoppingCard.setOrder_id(listObjects.get(i).getOrderId());
            shoppingCard.setServiceName(productName);
            shoppingCard.setChatId(listObjects.get(i).getChatId());
            shoppingCard.setServiceTime(listObjects.get(i).getServiceTime());
            shoppingCard.setProductId(productId);
            shoppingCard.setPriceId(priceId);
            shoppingCartController.updateById(shoppingCard, shoppingCardId);
        }

        BaseResponse<?> stompChatResponseDto = stompChatService.updateChatById(gsonMap, String.valueOf(shoppingCard.getChatId()));

        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), stompChatResponseDto);
    }

    @PostMapping(value = "/pay")
    public BaseResponse<?> payShop(@RequestBody PaymentRequest paymentRequest) throws SQLException, ServletException, StripeException, IOException {
        log.info("inside payShop");
        int chatId = paymentRequest.getChatId();
        int userId = paymentRequest.getUserId();
        float tax = paymentRequest.getTax();
        float finalTotal = 0;
        float finalPaymentAmount = 0;
        //get final Amount for pay
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        List<ShoppingCart> shoppingCardList = shoppingCartController.getAllByChatId(chatId);
        for (int i = 0; i < shoppingCardList.size(); i++) {
            finalTotal += shoppingCardList.get(i).getPrice();
        }
        float taxTotal = (finalTotal * tax) / 100;
        finalPaymentAmount = taxTotal + finalTotal;
        UserChatDataController userChatDataController = new UserChatDataController();
        //user stripe issues
        Users user = userChatDataController.getUserObjectById(String.valueOf(userId));
        //get stripe user id
        newStripeUsersResponse = newStripeUsersControllerDao.getStripUserObjectByUserId(userId);
        String customerId = newStripeUsersResponse.getCustomerId();
        //find card by customer id
        newStripeCardResponse = newStripeCardController.getTheDefaultCard(customerId);
        String cardId = newStripeCardResponse.getCardId();
        ResponseEntity<?> responseEntity = makePaymentAction(user, finalPaymentAmount, customerId, cardId);

        if (responseEntity != null) {
            //change shopping card status to 1
            for (int i = 0; i < shoppingCardList.size(); i++) {
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setOrder_id(shoppingCardList.get(i).getOrder_id());
                shoppingCart.setServiceName(shoppingCardList.get(i).getServiceName());
                shoppingCart.setPrice(shoppingCardList.get(i).getPrice());

                shoppingCartController.updateShoppingCardStatus(shoppingCardList.get(i).getId());
                shoppingCartController.AddToFinal(
                        paymentId,
                        shoppingCardList.get(i).getServiceId(),
                        user.getId(),
                        user,
                        shoppingCardList.get(i).getOrder_id(),
                        shoppingCart,
                        finalPaymentAmount,
                        customerId);
            }
        }
        Map<String, String> resultMap = new LinkedHashMap<>();
        resultMap.put("status", "success");
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ALREADY_REPORTED.value(), resultMap);
    }

    private ResponseEntity<?> makePaymentAction(Users usersResponseDto, float finalPaymentAmount, String customerId, String cardId) throws SQLException, ServletException, StripeException, IOException {
        NewStripeChargeShoppingCardRequest newStripeChargeShoppingCardRequest = NewStripeChargeShoppingCardRequest.builder()
                .cardId(cardId)
                .customerId(customerId)
                .description("Chat Payment")
                .price(Math.round(finalPaymentAmount))
                .code("")
                .build();
        String result = chargeSHoppingCard(newStripeChargeShoppingCardRequest);
        Map map = gson.fromJson(result, Map.class);
        Map<String, String> paymentStripId = gsonConverter.convertStringToMapToMapObject(map.get("data"));
        paymentId = paymentStripId.get("id");
        if (null != result) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @POST
    public static String chargeSHoppingCard(Object jsonBody) {

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(
                "http://second.fertifabenefits.com/stripe/stripe/v1/charge/create",
                jsonBody,
                String.class);

        log.info("responseMedia " + response);
        return response;
    }

    @GetMapping(value = "/products/status/{chatid}")
    public ResponseEntity<?> getChatStatusById(@PathVariable("chatid") String chatId) {
        BaseResponse<?> response = getStatus(chatId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getStatus(String chatId) {
        int status = 0;
        try {
            ShoppingCartController shoppingCartController = new ShoppingCartController();
            status = shoppingCartController.getStatusById(chatId);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), status);
    }

    @GetMapping(value = "/shoppingCart/status/{shoppingCardChatId}")
    public ResponseEntity<?> getChatShoppingCardStatusById(@PathVariable("shoppingCardChatId") String shoppingCardChatId) {
        BaseResponse<?> response = getShoppingCardStatus(shoppingCardChatId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getShoppingCardStatus(String chatId) {
        int status = 0;
        try {
            ShoppingCartController shoppingCartController = new ShoppingCartController();
            status = shoppingCartController.getStatusById(chatId);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), status);
    }


    @GetMapping(value = "/order/status/{sessionId}")
    public ResponseEntity<?> getSessionStatus(@PathVariable("sessionId") String sessionId) {
        BaseResponse<?> response = getSessionStatusById(sessionId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> getSessionStatusById(String sessionId) {
        int status = 0;
        try {
            OrdersController ordersController = new OrdersController();
            status = ordersController.getStatusByOrderId(sessionId);
        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), status);
    }

    @PostMapping(value = "/booking/approve/")
    public ResponseEntity<?> approveBooking(@RequestBody String data) throws SQLException {
        Map<String, String> dataMap = gson.fromJson(data, Map.class);
        int addedCount = 0;
        int userId = Integer.parseInt(dataMap.get("userId"));
        int orderId = Integer.parseInt(dataMap.get("orderId"));
        int chatId = Integer.parseInt(dataMap.get("chatId"));
        OrdersController ordersController = new OrdersController();
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        ShoppingCard shoppingCart = new ShoppingCard();

        List<ServiceDateTime> serviceDateTimeArrayList = ordersController.getByUserIdFullDetails(userId, orderId);
        String datesToChange = "";
        if (!serviceDateTimeArrayList.isEmpty()) {
            for (int i = 0; i < serviceDateTimeArrayList.size(); i++) {
                ProductsResponseDto products = productsMapper.fromProductIdChatToProductResponseDto(productsRepository.getOne((long) serviceDateTimeArrayList.get(i).getService_id()));
                String productName = products.getTitle();
                datesToChange = serviceDateTimeArrayList.get(i).getDate_time();
                if (datesToChange.contains("&")) {
                    String[] datesSplit = datesToChange.split("&");
                    for (int j = 0; j < datesSplit.length; j++) {
                        String productId = BuildProductInStrip(productName);
                        String priceId = BuildProductPriceStrip(productId,
                                String.valueOf(serviceDateTimeArrayList.get(i).getGetServicePrice()),
                                String.valueOf(userId));
                        shoppingCart = ShoppingCard
                                .builder()
                                .chatId(chatId)
                                .order_id(serviceDateTimeArrayList.get(i).getOrder_id())
                                .user_id(serviceDateTimeArrayList.get(i).getUser_id())
                                .serviceName(productName)
                                .price(serviceDateTimeArrayList.get(i).getGetServicePrice())
                                .order_date(datesSplit[j])
                                .status(0)
                                .type(2)
                                .productId(productId)
                                .priceId(priceId)
                                .serviceId(serviceDateTimeArrayList.get(i).getService_id())
                                .build();

                        addedCount = shoppingCartController.AddNewOrder(shoppingCart);
                    }
                } else {
                    String productId = BuildProductInStrip(productName);
                    String priceId = BuildProductPriceStrip(productId,
                            String.valueOf(serviceDateTimeArrayList.get(i).getGetServicePrice()),
                            String.valueOf(userId));
                    shoppingCart = ShoppingCard
                            .builder()
                            .chatId(chatId)
                            .order_id(serviceDateTimeArrayList.get(i).getOrder_id())
                            .user_id(serviceDateTimeArrayList.get(i).getUser_id())
                            .serviceName(productName)
                            .price(serviceDateTimeArrayList.get(i).getGetServicePrice())
                            .order_date(serviceDateTimeArrayList.get(i).getDate_time())
                            .status(0)
                            .type(2)
                            .productId(productId)
                            .priceId(priceId)
                            .serviceId(serviceDateTimeArrayList.get(i).getService_id())
                            .build();
                    addedCount = shoppingCartController.AddNewOrder(shoppingCart);
                }
            }
            if (addedCount > 0) {
                return new ResponseEntity<>(addedCount, HttpStatus.ACCEPTED);
            }
        } else {
            return new ResponseEntity<>(addedCount, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(addedCount, HttpStatus.ACCEPTED);
    }


    @PostMapping(value = "/booking/update/")
    public ResponseEntity<?> updateBooking(@RequestBody String data) throws SQLException {
        log.info("update booking =======> {}", data);
        int result = 0;
        OrdersController ordersController = new OrdersController();
        ShoppingCartController shoppingCartController = new ShoppingCartController();
        ShoppingCard shoppingCart = new ShoppingCard();
        UsersController usersController = new UsersController();
        Map dataMap = gson.fromJson(data, Map.class);
        int chatId = Integer.parseInt(dataMap.get("data_chatId").toString());
        int documentId = Integer.parseInt(dataMap.get("data_documentId").toString());
        int userId = Integer.parseInt(dataMap.get("data_userId").toString());
        String gsonString = gson.toJson(dataMap.get("dates"));
        Type type = new TypeToken<ArrayList<Map<String, String>>>() {
        }.getType();
        ArrayList<Map<String, String>> datesMapList = gson.fromJson(gsonString, type);
        List<String> finalTimeList = new ArrayList<>();
        for (int i = 0; i < datesMapList.size(); i = i + 3) {
            finalTimeList.add(datesMapList.get(i).get("value") + " " +datesMapList.get(i + 1).get("value") + " - "+datesMapList.get(i + 2).get("value"));
        }
        log.info("final time List {}", finalTimeList);
        List<Users> users = usersController.getCompanyById(userId);
        List<Orders> ordersList = ordersController.getAllByOrderId(documentId);
        ProductsResponseDto products = productsMapper.fromProductIdChatToProductResponseDto(productsRepository.getOne((long) ordersList.get(0).getService_id()));
        String productName = products.getTitle();
        double productPrice = products.getPrice();
        int serviceId = ordersList.get(0).getService_id();
        for (int j = 0; j < finalTimeList.size(); j++) {
            String productId = BuildProductInStrip(productName);
            String priceId = BuildProductPriceStrip(productId,
                    String.valueOf(productPrice),
                    String.valueOf(userId));
            shoppingCart = ShoppingCard
                    .builder()
                    .chatId(chatId)
                    .order_id(documentId)
                    .user_id(userId)
                    .serviceName(productName)
                    .price((float) productPrice)
                    .order_date(finalTimeList.get(j))
                    .status(0)
                    .type(2)
                    .productId(productId)
                    .priceId(priceId)
                    .serviceId(serviceId)
                    .build();
            result = shoppingCartController.AddNewOrder(shoppingCart);
        }
        String newMessage = "Hello \n" + users.get(0).getFirstName() + " " + users.get(0).getLastName() + "\n " +
                "We just changed your bookings dates for " + productName + ", to new Dates as follow ";
        for (int i = 0; i < finalTimeList.size(); i++) {
            newMessage += (finalTimeList.get(i) + " ");
        }
        newMessage += ", please to confirm go to your shopping cart.";
        log.info("Message to send {}", newMessage);
        MessagingStompChat messagingStompChat = stompChatService.getChatById(chatId);
        messagingStompChat.setMessageBody(newMessage);
        ResponseEntity<BaseResponse<?>> response = stompChatService.save(messagingStompChat);
        Map<String,Object> body = gsonConverter.convertObjectToMapObject(response);
        if (result != 0) {
            return new ResponseEntity<>(gson.toJson(body), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(gson.toJson(body), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/booking/decline/{chatId}")
    public ResponseEntity<?> declineBooking(@RequestBody String orderId, @PathVariable String chatId) {
        BaseResponse<?> response = declineBookingByOrderId(orderId, chatId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    private BaseResponse<?> declineBookingByOrderId(String orderId, String chatId) {
        int declinedResponseResult = 0;
        try {
            OrdersController ordersController = new OrdersController();
            ShoppingCartController shoppingCartController = new ShoppingCartController();

            List<Orders> orderByIdList = ordersController.orderById(Integer.parseInt(orderId));
            log.info("Order List ==================> {}", orderByIdList);
            //add to shopping card
            ProductsResponseDto products = productsMapper.fromProductIdChatToProductResponseDto(productsRepository.getOne((long) orderByIdList.get(0).getService_id()));
            String productName = products.getTitle();

            ShoppingCard shoppingCart = new ShoppingCard();
            shoppingCart.setUser_id(orderByIdList.get(0).getUser_id());
            shoppingCart.setStatus(2);
            shoppingCart.setChatId(Integer.parseInt(chatId));
            shoppingCart.setOrder_date(String.valueOf(orderByIdList.get(0).getDate_id()));
            shoppingCart.setServiceId(orderByIdList.get(0).getService_id());
            shoppingCart.setPrice(orderByIdList.get(0).getCoast());
            shoppingCart.setServiceName(productName);

            shoppingCartController.AddNewOrder(shoppingCart);

            declinedResponseResult = ordersController.changeStatusToDecline(orderId);

        } catch (Exception e) {
            return new BaseResponse<>(new Date(), HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
        return new BaseResponse<>(new Date(), HttpStatus.ACCEPTED, HttpStatus.ACCEPTED.value(), declinedResponseResult);
    }
}
