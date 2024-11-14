package com.ishaank.Projects.GuardianBank.utils;

import org.springframework.stereotype.Service;

@Service
public class AccountTypeUtil {

    public String normalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Replace underscores and hyphens with spaces
        input = input.replaceAll("[-_]", " ");

        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            } else {
                c = Character.toLowerCase(c);
            }
            titleCase.append(c);
        }

        return titleCase.toString();
    }


}
