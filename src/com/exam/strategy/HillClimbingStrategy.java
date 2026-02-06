package com.exam.strategy;

import com.exam.model.Allocation;
import com.exam.model.Seat;
import com.exam.model.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HillClimbingStrategy implements SeatingStrategy {

    private static final int MAX_ITERATIONS = 5000; // How many times we try to swap

    @Override
    public List<Allocation> allocate(long examId, List<Student> students, List<Seat> seats) {
        System.out.println("--- Running AI Hill Climbing Strategy ---");
        List<Student> currentPlacement = new ArrayList<>(students);
        Collections.shuffle(currentPlacement);

        int currentCost = calculateTotalRisk(currentPlacement, seats);
        System.out.println("Initial Risk Score: " + currentCost);

        Random random = new Random();

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            if (currentCost == 0) break;
            int idx1 = random.nextInt(currentPlacement.size());
            int idx2 = random.nextInt(currentPlacement.size());

            Collections.swap(currentPlacement, idx1, idx2);
            int newCost = calculateTotalRisk(currentPlacement, seats);
            if (newCost < currentCost) {
                currentCost = newCost;
                // System.out.println("Iteration " + i + ": Improved Score to " + currentCost);
            } else {
                Collections.swap(currentPlacement, idx1, idx2);
            }
        }

        System.out.println("Final Risk Score: " + currentCost);
        List<Allocation> finalAllocations = new ArrayList<>();
        for (int i = 0; i < currentPlacement.size(); i++) {
            if (i < seats.size()) {
                finalAllocations.add(new Allocation(examId, currentPlacement.get(i).getId(), seats.get(i).getId()));
            }
        }
        return finalAllocations;
    }
    private int calculateTotalRisk(List<Student> students, List<Seat> seats) {
        int score = 0;
        for (int i = 0; i < students.size(); i++) {
            for (int j = i + 1; j < students.size(); j++) {
                if (i >= seats.size() || j >= seats.size()) continue;

                Student s1 = students.get(i);
                Student s2 = students.get(j);
                Seat seat1 = seats.get(i);
                Seat seat2 = seats.get(j);

                if (isAdjacent(seat1, seat2)) {
                    if (s1.getBranch().equals(s2.getBranch())) {
                        score += 100; // Penalty for cheating risk
                    }
                }
            }
        }
        return score;
    }

    private boolean isAdjacent(Seat s1, Seat s2) {
        int rowDiff = Math.abs(s1.getRow() - s2.getRow());
        int colDiff = Math.abs(s1.getCol() - s2.getCol());
        return rowDiff <= 1 && colDiff <= 1;
    }
}
