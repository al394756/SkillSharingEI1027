package com.example.SkillSharingEI1027.controller;


import com.example.SkillSharingEI1027.dao.ChatDao;
import com.example.SkillSharingEI1027.dao.MessageDao;
import com.example.SkillSharingEI1027.dao.StudentDao;
import com.example.SkillSharingEI1027.modelo.Chat;
import com.example.SkillSharingEI1027.modelo.Message;
import com.example.SkillSharingEI1027.modelo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MessageValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return Message.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Message message = (Message) obj;
        if (message.getContent().trim().equals(""))
            errors.rejectValue("content","compulsory","Introduce a message");

    }
}
@Controller
@RequestMapping("/chat")
public class ChatController {

    private ChatDao chatDao;
    private MessageDao messageDao;
    private StudentDao studentDao;

    @Autowired
    public void setChatDao(ChatDao chatDao){ this.chatDao=chatDao;}

    @Autowired
    public void setMessageDao(MessageDao messageDao){ this.messageDao=messageDao;}

    @Autowired
    public void setStudentDao(StudentDao studentDao){ this.studentDao=studentDao;}

    //Métodos para listar todos los chats del usuario que está autenticado
    @RequestMapping("/list")
    public String listChats(Model model, HttpSession session){
        Student user = (Student) session.getAttribute("user");
        if (user == null || !user.isActiveAccount()){
            return "welcome";
        }
        List<Chat> chats=chatDao.getChatsDeStudentSinLeer(user);
        chats.addAll(chatDao.getChatsDeStudentLeidos(user));
        almacenarNombrePersona(chats, user.getIdStudent());
        model.addAttribute("chats", chats);
        return "chat/list";
    }

    private void almacenarNombrePersona(List<Chat> chats, String user){
        for (Chat chat:chats ) {
            if (user.equals(chat.getUser1())){
                chat.setNombreOtraPersona(studentDao.getStudentUsingId(chat.getUser2()).getName());
            } else {
                chat.setNombreOtraPersona(studentDao.getStudentUsingId(chat.getUser1()).getName());
            }
        }

    }

    //Revisar mensajes entre 2 students
    @RequestMapping(value="/messages/{chatId}")
    public String checkMessages(Model model,@PathVariable String chatId, HttpSession session){
        Student user = (Student) session.getAttribute("user");
        if (user == null || !user.isActiveAccount()){
            return "welcome";
        }

        List<Chat> chats = chatDao.getChatsDeStudent(user);
        almacenarNombrePersona(chats,user.getIdStudent());
        model.addAttribute("chats", chats);
        Chat chat = chatDao.getChatConId(chatId);

        List<String> listStudents=chatDao.getStudents(chat);
        model.addAttribute("messages", messageDao.getMessagesFromChat(chatId));
        String idUser2;
        if (listStudents.get(0).equals(user.getIdStudent())){
            chat.setNewMsgParaStudent1(false);
            idUser2=chat.getUser2();
        } else {
            chat.setNewMsgParaStudent2(false);
            idUser2=chat.getUser1();
        }
        chatDao.updateChat(chat);

        messageDao.setChat(chat);
        session.setAttribute("UnreadMsg", chatDao.getCantidadChatsSinLeer(user));

        model.addAttribute("user2",studentDao.getStudentUsingId(idUser2).getName());
        model.addAttribute("user", user.getIdStudent());
        model.addAttribute("newMessage", new Message());
        return "chat/messages";
    }

    @RequestMapping(value ="/messages", method = RequestMethod.POST)
    public String processMessage(@ModelAttribute("newMessage") Message newMessage, HttpSession session, BindingResult bindingResult){
        MessageValidator validator= new MessageValidator();
        validator.validate(newMessage,bindingResult);
        if (bindingResult.hasErrors())
            return "redirect:/chat/messages/"+messageDao.getChat().getIdChat();


        Student user = (Student) session.getAttribute("user");
        newMessage.setDate(LocalDate.now());
        newMessage.setStudent(user.getIdStudent());
        newMessage.setIdChat(messageDao.getChat().getIdChat());
        List<String> listStudents=chatDao.getStudents(messageDao.getChat());
        Chat chat = messageDao.getChat();


        newMessage.setNumber(messageDao.messageNumber(newMessage.getIdChat()));


        messageDao.addMessage(newMessage);
        if (listStudents.get(0).equals(user.getIdStudent())){
            chat.setNewMsgParaStudent2(true);
        } else {
            chat.setNewMsgParaStudent1(true);

        }


        chatDao.updateChat(chat);
        return "redirect:/chat/messages/"+newMessage.getIdChat();
    }
}
