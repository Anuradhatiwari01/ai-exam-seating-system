package com.exam.strategy;

import com.exam.model.Allocation;
import com.exam.model.Seat;
import com.exam.model.Student;
import java.util.List;

public interface SeatingStrategy {
    List<Allocation> allocate(long examId, List<Student> students, List<Seat> seats);
}
