package com.cvsnewsletter.constants;

import java.util.List;
import java.util.Map;

public class DropdownConstants {

    public static final List<String> PRIMARY_SKILLS = List.of(
            "Java Backend", "Java Fullstack", "Angular", ".NET", "DevOps",
            "iOS", "jQuery", "QA Manual", "QA Automation", "Mainframe"
    );

    public static final List<String> LOCATIONS = List.of(
            "Bangalore", "Noida", "Hyderabad", "Kolkata", "US"
    );

    public static final List<String> TOWERS = List.of(
            "PCW T1", "PCW T2", "PCW T3", "PCW T4", "PSS", "NA"
    );

    public static final List<String> DESIGNATION_BANDS = List.of(
            "5B - Senior Associate",
            "4A - Consultant",
            "4B - Lead Consultant",
            "4C - Principal Consultant",
            "4D - Senior Principal Consultant",
            "3 - Assistant Vice President",
            "2 - Vice President"
    );

    public static Map<String, List<String>> getDropdownValues() {
        return Map.of(
                "primarySkills", PRIMARY_SKILLS,
                "locations", LOCATIONS,
                "towers", TOWERS,
                "designationBands", DESIGNATION_BANDS
        );
    }
}

