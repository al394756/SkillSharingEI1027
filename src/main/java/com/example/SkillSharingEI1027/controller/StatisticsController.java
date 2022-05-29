package com.example.SkillSharingEI1027.controller;

import com.example.SkillSharingEI1027.dao.CollaborationDao;
import com.example.SkillSharingEI1027.dao.OffeRequestDao;
import com.example.SkillSharingEI1027.dao.SkillDao;
import com.example.SkillSharingEI1027.dao.StatisticsDao;
import com.example.SkillSharingEI1027.modelo.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StatisticsController {
    private StatisticsDao statisticsDao;
    private SkillDao skillDao;
    private OffeRequestDao offeRequestDao;
    private CollaborationDao collaborationDao;

    @Autowired
    public void setOffeRequestDao(StatisticsDao statisticsDao, SkillDao skillDao, OffeRequestDao offeRequestDao, CollaborationDao collaborationDao) {
        this.statisticsDao=statisticsDao;
        this.skillDao=skillDao;
        this.offeRequestDao=offeRequestDao;
        this.collaborationDao=collaborationDao;
    }

    @RequestMapping("/statistics")
    public String statistics(Model model){
        float pOfDDistinctSkillsOffer;
        float pOfDDistinctSkillsRequest;
        float pOfDDistinctSkillsCollaboration;
        if(skillDao.getSkills().isEmpty()){
            pOfDDistinctSkillsOffer=0;
            pOfDDistinctSkillsRequest=0;
            pOfDDistinctSkillsCollaboration=0;
        }
        else {
            pOfDDistinctSkillsOffer = statisticsDao.numberOfDistinctSkillsInOffeRequest("offer");
            pOfDDistinctSkillsRequest = statisticsDao.numberOfDistinctSkillsInOffeRequest("request");
            pOfDDistinctSkillsCollaboration = statisticsDao.numberOfDistinctSkillsInCollaboration();
        }

        model.addAttribute("pOfDDistinctSkillsOffer",String.format("%.2f", pOfDDistinctSkillsOffer));
        model.addAttribute("pOfDDistinctSkillsRequest",String.format("%.2f", pOfDDistinctSkillsRequest));
        model.addAttribute("pOfDDistinctSkillsCollaboration",String.format("%.2f",pOfDDistinctSkillsCollaboration));

        List<Statistics> timesOfSkillUsedOffer=new ArrayList<>();
        List<Statistics> timesOfSkillUsedRequest=new ArrayList<>();
        List<Statistics> timesOfSkillUsedCollaboration=new ArrayList<>();

        List<Statistics> timesMVPStudentOffer=new ArrayList<>();
        List<Statistics> timesMVPStudentRequest=new ArrayList<>();
        List<Statistics> timesMVPStudentCollaboration=new ArrayList<>();

        if (!offeRequestDao.getActiveOffeRequests("offer").isEmpty()){
            timesOfSkillUsedOffer=statisticsDao.timesOfSkillUsedOffeRequest("offer");
            timesMVPStudentOffer=statisticsDao.timesMVPStudentOffeRequest("offer");
        }
        if (!offeRequestDao.getActiveOffeRequests("request").isEmpty()){
            timesOfSkillUsedRequest=statisticsDao.timesOfSkillUsedOffeRequest("request");
            timesMVPStudentRequest=statisticsDao.timesMVPStudentOffeRequest("request");
        }
        if (!collaborationDao.getCollaborationsActivas().isEmpty()){
            timesOfSkillUsedCollaboration=statisticsDao.timesOfSkillUsedCollaboration();
            timesMVPStudentCollaboration=statisticsDao.timesMVPStudentCollaboration();
        }

        model.addAttribute("timesOfSkillUsedOffer",timesOfSkillUsedOffer);
        model.addAttribute("timesOfSkillUsedRequest",timesOfSkillUsedRequest);
        model.addAttribute("timesOfSkillUsedCollaboration",timesOfSkillUsedCollaboration);

        model.addAttribute("timesMVPStudentOffer",timesMVPStudentOffer);
        model.addAttribute("timesMVPStudentRequest",timesMVPStudentRequest);
        model.addAttribute("timesMVPStudentCollaboration",timesMVPStudentCollaboration);

        return "statistics";
    }
}
