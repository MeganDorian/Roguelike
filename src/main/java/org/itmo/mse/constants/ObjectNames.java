package org.itmo.mse.constants;

import java.util.List;

public interface ObjectNames {
    String wall = "Wall";
    String map = "Map";
    String player = "Student";
    List<String> aggressiveMobs = List.of("Deadline", "Contest", "Session");
    List<String> passiveMob = List.of("Coursework", "Graduate Work", "Research work");
    List<String> cowardlyMob = List.of("C++ homework", "Internship", "Bug in code");
    List<String> usualAids = List.of("Watch film aid", "Coffee aid", "Reschedule the deadline aid");
    List<String> legendaryAids =
        List.of("Extra holidays aid", "Energy drink aid", "9 hours sleep aid");
    
    List<String> usualLightArmor =
        List.of("Conspectus armor", "Scholarship armor", "IDE License armor");
    List<String> usualMediumArmor = List.of("Lecture recordings armor", "Sponsor scholarship armor",
                                            "Answers to the control work armor");
    List<String> legendaryArmor =
        List.of("Automatic exam pass armor", "Job armor", "Exam ticket answers armor");
    
    List<String> usualLightWeapon =
        List.of("Book with theory weapon", "Computer mouse weapon", "Pencil weapon");
    List<String> usualMediumWeapon =
        List.of("Book with tasks weapon", "Lunch snack weapon",
                "Charger for laptop weapon");
    List<String> legendaryWeapon =
        List.of("Backpack with notebooks weapon", "Shawarma weapon", "Laptop weapon");
}
