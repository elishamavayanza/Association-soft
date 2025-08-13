package com.org.testApi.services;

import com.org.testApi.models.Activity;
import com.org.testApi.models.FinancialTransaction;
import com.org.testApi.models.Member;
import com.org.testApi.repository.ActivityRepository;
import com.org.testApi.repository.FinancialTransactionRepository;
import com.org.testApi.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private MemberRepository memberRepository;

    private List<Observer<String>> observers = new ArrayList<>();

    @Override
    public List<Object> generateActivityReport(LocalDateTime startDate, LocalDateTime endDate) {
        // Récupérer les données des activités entre les dates spécifiées
        List<Object> reportData = new ArrayList<>();

        // Obtenir les activités dans la période
        List<Activity> activities = activityRepository.findByStartDateTimeBetween(startDate, endDate);

        // Compter le nombre d'activités par type
        Map<String, Long> activitiesByType = activities
                .stream()
                .collect(Collectors.groupingBy(
                        activity -> activity.getType() != null ? activity.getType().toString() : "UNKNOWN",
                        Collectors.counting()
                ));

        // Compter le nombre total de participants
        long totalParticipants = activities
                .stream()
                .mapToLong(activity -> activity.getParticipants() != null ? activity.getParticipants().size() : 0)
                .sum();

        // Créer un objet de rapport
        Map<String, Object> activityReport = new HashMap<>();
        activityReport.put("reportType", "Activity Report");
        activityReport.put("periodStart", startDate);
        activityReport.put("periodEnd", endDate);
        activityReport.put("activitiesByType", activitiesByType);
        activityReport.put("totalActivities", activities.size());
        activityReport.put("totalParticipants", totalParticipants);

        reportData.add(activityReport);

        // Notifier les observateurs qu'un rapport d'activité a été généré
        notifyObservers("ACTIVITY_REPORT_GENERATED", "Activity report from " + startDate + " to " + endDate);

        return reportData;
    }

    @Override
    public List<Object> generateFinancialReport(LocalDateTime startDate, LocalDateTime endDate) {
        // Récupérer les données financières entre les dates spécifiées
        List<Object> reportData = new ArrayList<>();

        // Obtenir les transactions dans la période
        List<FinancialTransaction> allTransactions = financialTransactionRepository.findByTransactionDateBetween(
                startDate.toLocalDate(), endDate.toLocalDate());

        // Séparer les revenus et les dépenses
        List<FinancialTransaction> incomeTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getAmount().compareTo(java.math.BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());

        List<FinancialTransaction> expenseTransactions = allTransactions.stream()
                .filter(transaction -> transaction.getAmount().compareTo(java.math.BigDecimal.ZERO) < 0)
                .collect(Collectors.toList());

        // Calculer les totaux par catégorie pour les revenus
        Map<String, Double> incomeByCategory = incomeTransactions
                .stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory() != null ? transaction.getCategory().getName() : "Uncategorized",
                        Collectors.summingDouble(transaction -> transaction.getAmount().doubleValue())
                ));

        // Calculer les totaux par catégorie pour les dépenses
        Map<String, Double> expensesByCategory = expenseTransactions
                .stream()
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory() != null ? transaction.getCategory().getName() : "Uncategorized",
                        Collectors.summingDouble(transaction -> Math.abs(transaction.getAmount().doubleValue()))
                ));

        // Calculer les totaux
        double totalIncome = incomeByCategory.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expensesByCategory.values().stream().mapToDouble(Double::doubleValue).sum();
        double netIncome = totalIncome - totalExpenses;

        // Créer un objet de rapport
        Map<String, Object> financialReport = new HashMap<>();
        financialReport.put("reportType", "Financial Report");
        financialReport.put("periodStart", startDate);
        financialReport.put("periodEnd", endDate);
        financialReport.put("incomeByCategory", incomeByCategory);
        financialReport.put("expensesByCategory", expensesByCategory);
        financialReport.put("totalIncome", totalIncome);
        financialReport.put("totalExpenses", totalExpenses);
        financialReport.put("netIncome", netIncome);

        reportData.add(financialReport);

        // Notifier les observateurs qu'un rapport financier a été généré
        notifyObservers("FINANCIAL_REPORT_GENERATED", "Financial report from " + startDate + " to " + endDate);

        return reportData;
    }

    @Override
    public List<Object> generateMembershipReport() {
        // Récupérer les données sur les membres
        List<Object> reportData = new ArrayList<>();

        // Compter les membres actifs
        long activeMembers = memberRepository.countByIsActiveTrue();

        // Obtenir tous les membres
        List<Member> allMembers = memberRepository.findAll();

        // Compter les membres par type
        Map<String, Long> membersByType = allMembers
                .stream()
                .collect(Collectors.groupingBy(
                        member -> member.getType() != null ? member.getType().toString() : "UNKNOWN",
                        Collectors.counting()
                ));

        // Créer un objet de rapport
        Map<String, Object> membershipReport = new HashMap<>();
        membershipReport.put("reportType", "Membership Report");
        membershipReport.put("reportDate", LocalDateTime.now());
        membershipReport.put("totalMembers", (long) allMembers.size());
        membershipReport.put("activeMembers", activeMembers);
        membershipReport.put("inactiveMembers", (long) allMembers.size() - activeMembers);
        membershipReport.put("membersByType", membersByType);

        reportData.add(membershipReport);

        // Notifier les observateurs qu'un rapport de membre a été généré
        notifyObservers("MEMBERSHIP_REPORT_GENERATED", "Membership report");

        return reportData;
    }

    @Override
    public void addObserver(Observer<String> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<String> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, String entity) {
        for (Observer<String> observer : observers) {
            observer.update(event, entity);
        }
    }
}
