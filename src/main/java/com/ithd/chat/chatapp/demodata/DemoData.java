package com.ithd.chat.chatapp.demodata;


import com.ithd.chat.chatapp.repository.admin.AdminModelRepository;
import com.ithd.chat.chatapp.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class DemoData {

    private final UsersRepository usersChatRepository;
    private final AdminModelRepository adminModelRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) throws InterruptedException {
//
//        List<AdminModel> adminModels = adminModelRepository.findAll();
//        if (adminModels.isEmpty()) {
//            AdminModel adminModel = new AdminModel();
//            adminModel.setAddress("Arkishti 7/6");
//            adminModel.setAdminStatus(1l);
//            adminModel.setEmail("info@fertifa.com");
//            adminModel.setFirstName("Shant");
//            adminModel.setLastName("Khayalian");
//            adminModel.setPhoneNumber("+37498229898");
//            adminModel.setRole("ADMIN");
//            adminModel.setCreated(new Timestamp(new Date().getTime()-20));
//            adminModel.setUpdated(new Timestamp(new Date().getTime()));
//
//            adminModelRepository.save(adminModel);
//            AdminModel adminNurse = new AdminModel();
//            adminNurse.setAddress("Somewhere 7/6");
//            adminNurse.setAdminStatus(1l);
//            adminNurse.setEmail("nurse@fertifa.com");
//            adminNurse.setFirstName("Armine");
//            adminNurse.setLastName("Kaytagoxyan");
//            adminNurse.setPhoneNumber("+37498229898");
//            adminNurse.setRole("NURSE");
//            adminNurse.setCreated(new Timestamp(new Date().getTime()-10));
//            adminNurse.setUpdated(new Timestamp(new Date().getTime()));
//            adminModelRepository.save(adminNurse);
//        }
//        AdminModel adminModel =  new AdminModel();
//        adminModel.setId(1l);
//        List<UsersModel> usersChatModels = usersChatRepository.findAll();
//        if (usersChatModels.isEmpty()) {
//            for (int i = 0; i < 30; i++) {
//                //TimeUnit.SECONDS.sleep(20);
//                UsersModel usersChatModel = new UsersModel();
//
//                usersChatModel.setCreated(new Timestamp(new Date().getTime()));
//                usersChatModel.setUpdated(new Timestamp(new Date().getTime()));
//                usersChatModel.setAge(18L + i);
//                usersChatModel.setEmail("skhayalian" + i + "@gmail.com");
//                usersChatModel.setPhoneNumber("+374 -" + randomNumber());
//                usersChatModel.setFullName(randomString(4) + " " + randomString(12));
//                usersChatModel.setRole("EMPLOYEE");
//                usersChatModel.setCompanyName(randomString(5));
//                usersChatModel.setStatus(1L);
//                usersChatModel.setUserId(i + 1L);
//                usersChatModel.setUserImage("upload/" +i + ".jpg");
//                usersChatModel.setChatRoom(ChatRoom.builder().chatRoomId(randomString(20)).build());
//                usersChatModel.setAdminModels(adminModel);
//                usersChatRepository.save(usersChatModel);
//            }
//        }
    }

//    private String randomString(int count) {
//        return RandomStringUtils.randomAlphabetic(count);
//    }

    private int randomNumber() {
        return new Random().nextInt(111111) + 999999;

    }
}
