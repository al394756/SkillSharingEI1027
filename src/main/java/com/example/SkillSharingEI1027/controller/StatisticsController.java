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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        float numberOfDDistinctSkillsOffer=statisticsDao.numberOfDistinctSkillsInOffeRequest("offer");
        float numberOfDDistinctSkillsRequest=statisticsDao.numberOfDistinctSkillsInOffeRequest("request");
        float numberOfDDistinctSkillsCollaboration=statisticsDao.numberOfDistinctSkillsInCollaboration();

        float nSkills=statisticsDao.numberOfDistinctSkills();

        float pOfDDistinctSkillsOffer=numberOfDDistinctSkillsOffer/nSkills*100;
        float pOfDDistinctSkillsRequest=numberOfDDistinctSkillsRequest/nSkills*100;
        float pOfDDistinctSkillsCollaboration=numberOfDDistinctSkillsCollaboration/nSkills*100;

        model.addAttribute("pOfDDistinctSkillsOffer",String.format("%.2f", pOfDDistinctSkillsOffer));
        model.addAttribute("pOfDDistinctSkillsRequest",String.format("%.2f", pOfDDistinctSkillsRequest));
        model.addAttribute("pOfDDistinctSkillsCollaboration",String.format("%.2f",pOfDDistinctSkillsCollaboration));

        float nActiveOffers=offeRequestDao.getActiveOffeRequests("offer").size();
        float nActiveRequests=offeRequestDao.getActiveOffeRequests("request").size();
        float nActiveCollaborations=collaborationDao.getActiveCollaborations().size();
        List<Statistics> timesOfSkillUsedOffer=statisticsDao.timesOfSkillUsedOffeRequest("offer");
        List<Statistics> timesOfSkillUsedRequest=statisticsDao.timesOfSkillUsedOffeRequest("request");
        List<Statistics> timesOfSkillUsedCollaboration=statisticsDao.timesOfSkillUsedCollaboration();

        for (Statistics statistics:timesOfSkillUsedOffer)
            statistics.setCount(statistics.getCount()/nActiveOffers*100);
        for (Statistics statistics:timesOfSkillUsedRequest)
            statistics.setCount(statistics.getCount()/nActiveRequests*100);
        for (Statistics statistics:timesOfSkillUsedCollaboration)
            statistics.setCount(statistics.getCount()/nActiveCollaborations*100);

        model.addAttribute("timesOfSkillUsedOffer",timesOfSkillUsedOffer);
        model.addAttribute("timesOfSkillUsedRequest",timesOfSkillUsedRequest);
        model.addAttribute("timesOfSkillUsedCollaboration",timesOfSkillUsedCollaboration);

        return "statistics";
    }
}
